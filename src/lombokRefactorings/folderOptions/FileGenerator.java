package lombokRefactorings.folderOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;

import lombok.delombok.Delombok;
import lombokRefactorings.activator.LombokPlugin;
import lombokRefactorings.projectOptions.ProjectManager;
import lombokRefactorings.regex.SearchAndCallRefactorings;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

public class FileGenerator {
	
	private static ProjectManager projectManager = new ProjectManager();
	
	/**
	 * Recursively delomboks the files in the inputFolder and places the delomboked files in the outputFolder.
	 * 
	 * @param folder Folder used to look through and call back recursively.
	 * @throws CoreException
	 * @throws IOException
	 * @throws URISyntaxException 
	 */	
	public static void delombokFilesInFolder(IFolder inputFolder, IFolder outputFolder) throws CoreException, IOException, URISyntaxException {
		
		File outputFile = null;
		File inputFile = null;
		String name = "";
		
		for (IResource resource :inputFolder.members()) {
			if (resource.getClass().getSimpleName().equals("Folder")) {
				for (IResource outputResource :outputFolder.members()) {
					if (resource.getClass().getSimpleName().equals("Folder") &&
							resource.getName().equals(outputResource.getName())) {
						delombokFilesInFolder((IFolder) resource, (IFolder) outputResource);
					}
				}
			}
			else{
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				try {
					Delombok delombok = new Delombok();
					delombok.setVerbose(false);
					delombok.setForceProcess(true);
					
					delombok.setCharset("UTF-8");
					delombok.setFeedback(new PrintStream(stream));
					
					outputFile = new File(outputFolder.getRawLocation().toFile(), resource.getName());
					name = resource.getName();
					inputFile = new File(inputFolder.getRawLocation().toFile().toString());
					
					if (!"".equals(name)) {
						FileWriter writer = new FileWriter(outputFile);
						delombok.addFile(inputFile, name);
						delombok.setWriter(writer);
						if (!delombok.delombok()) {
							LombokPlugin.getDefault().getAstManager().addFailure(resource.getName(), "Delombok failed");
						}
						projectManager.refreshProjects();
					}
//					String delombokFeedback = stream.toString();
				}
				finally {
					stream.close();
				}
			}
		}
	}
	
	public static void refactorFilesInFolder(IFolder inputFolder, IFolder outputFolder) throws JavaModelException, CoreException, IOException {
		refactorFilesInFolder(inputFolder, inputFolder, outputFolder);
	}

	/**
	 * Recursively executes the refactorings in the files in the folder.
	 * 
	 * @param inputFolder Folder used to look through and call back recursively.
	 * @param writer 
	 * @throws CoreException
	 * @throws IOException
	 * @throws JavaModelException
	 */
	private static void refactorFilesInFolder(IFolder inputFolder, IFolder parent, IFolder outputFolder) throws CoreException, IOException,
			JavaModelException {
		for (IResource inputResource : inputFolder.members()) {
			
			if (inputResource.getType() == IResource.FOLDER) {
				for (IResource outputResource : outputFolder.members()) {
					if (outputResource.getType() == IResource.FOLDER && inputResource.getName().equals(outputResource.getName())) {
						refactorFilesInFolder((IFolder) inputResource, parent, (IFolder) outputResource);
					}
				}
			}
			else {
				ICompilationUnit iCompilationUnit = JavaCore.createCompilationUnitFrom((IFile)inputResource);

				if (inputFolder.getName().equals(outputFolder.getName())) {
					File outputFile = new File(outputFolder.getRawLocation().toFile(), inputResource.getName());

					FileWriter outputWriter = new FileWriter(outputFile);
					outputWriter.write(iCompilationUnit.getSource());
					outputWriter.close();
				}
			}
		}
		
		projectManager.refreshProjects();
		
		for (IResource inputResource : inputFolder.members()) {
			if (inputResource.getType() == IResource.FILE && inputFolder.getName().equals(outputFolder.getName())) {
				String sourceName = outputFolder.getProjectRelativePath().toString() + "." + inputResource.getName();
				
				sourceName = sourceName.replaceAll("/", ".")
					.substring(0, sourceName.lastIndexOf(".java"))
					.substring(4);				// to strip off "src"
			
				String projectName = outputFolder.getProject().getName();
				
				SearchAndCallRefactorings searchAndCallRefactorings = new SearchAndCallRefactorings(projectName, sourceName);
				searchAndCallRefactorings.runRefactorings(searchAndCallRefactorings.findAllTags());
			}
		}
	}
	
	/**
	 * Renames package declaration of files, in the source folder of a project, 
	 * based on their relative position to the source folder of the project.
	 * 
	 * @param folder 
	 * @throws CoreException 
	 */
	public static void correctPackageDeclarations(IProject project) throws CoreException {
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] entries = javaProject.getRawClasspath();
		for (IClasspathEntry entry : entries) {
			if (IClasspathEntry.CPE_SOURCE == entry.getEntryKind()) {
				IFolder folder = project.getWorkspace().getRoot().getFolder(entry.getPath());
				correctPackageDeclarations(folder);
			}
		}
	}
	
	private static void correctPackageDeclarations(IFolder folder) throws CoreException {
		for (IResource resource : folder.members()) {
			if (resource.getType() == IResource.FILE) {
				correctPackageDeclaration((IFile)resource);
			}
			else if (resource.getType() == IResource.FOLDER) {
				correctPackageDeclarations((IFolder)resource);
			}
		}
	}
	
	private static void correctPackageDeclaration(IFile file) throws CoreException {
		String packageName = getPackageNameFromRelativePackagePath(file);
		
		ICompilationUnit iCompilationUnit = JavaCore.createCompilationUnitFrom(file);
		if (iCompilationUnit.getSource().contains("package")) {
			String source = iCompilationUnit.getSource();
			int start = source.indexOf("package");
			int length = source.indexOf(";", source.indexOf("package")) - source.indexOf("package") + 1;
			TextEdit edit = new ReplaceEdit(start, length, packageName);
			iCompilationUnit.applyTextEdit(edit, null);
		}
		else {
			TextEdit edit = new InsertEdit(0, (packageName + "\n\n"));
			iCompilationUnit.applyTextEdit(edit, null);
		}
		iCompilationUnit.getWorkingCopy(null).commitWorkingCopy(true, null);
	}

	//TODO: improve upon this.
	private static String getPackageNameFromRelativePackagePath(IFile file) {
		String packageName = file.getProjectRelativePath().toString();
		packageName = packageName.substring(0, packageName.lastIndexOf(file.getName()) - 1);
		if (packageName.equals("src")) {
			packageName = "";
		}
		else {
			packageName = packageName.replaceAll("/", ".");
			packageName = packageName.substring(4);
			packageName = "package " + packageName + ";";
		}
		return packageName;
	}
}
