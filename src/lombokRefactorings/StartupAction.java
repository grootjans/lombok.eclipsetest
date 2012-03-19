package lombokRefactorings;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import lombok.SneakyThrows;
import lombokRefactorings.activator.LombokPlugin;
import lombokRefactorings.folderOptions.FolderManager;
import lombokRefactorings.folderOptions.TestFolderBuilder;
import lombokRefactorings.folderOptions.TestFolderBuilder.FolderBuilderException;
import lombokRefactorings.folderOptions.TestFolderBuilderImpl;
import lombokRefactorings.projectOptions.ProjectCreator;
import lombokRefactorings.projectOptions.ProjectManager;
import lombokRefactorings.unitTestOptions.AstManager;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class StartupAction implements IStartup {
	
	private static final String TEST_FOLDER = "test/simple";
	private static final String HOST_FOLDER_NAME = "testNewProj";
	
	@SneakyThrows({CoreException.class, IOException.class})
	@Override public void earlyStartup() {
		
		try {
			String hostProjectName = HOST_FOLDER_NAME;
			
			IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
			IProject[] projects = workspaceRoot.getProjects();
			IProject hostProject = null;
			if (projects != null) for (IProject project : projects) {
				if (hostProjectName.equals(project.getName())) {
					hostProject = project;
					break;
				}
			}
			
			if (hostProject == null) {
				hostProject = new ProjectCreator().createProject(hostProjectName);
			} else {
				hostProject.open(null);
			}
			
			IFolder testFolder = hostProject.getFolder("test");
			if (!testFolder.exists()) testFolder.create(true, true, null);
			moveTestsToProject(testFolder);
			
			File file = hostProject.getFile("lombokrefactor.log").getLocation().toFile();
			FileWriter writer = new FileWriter(file);
			
			buildProjectsAndTest(testFolder, writer);
			
			writer.close();
		}
		finally {
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				@Override public void run() {
					PlatformUI.getWorkbench().close();
				}
			});
		}
	}
	
	@SneakyThrows({CoreException.class, FolderBuilderException.class, IOException.class})
	public static void buildProjectsAndTest(IResource resource, FileWriter writer) {
		LombokPlugin.getDefault().setAstManager(new AstManager());
		ProjectManager projectManager = new ProjectManager();
		
		//cleanup
		projectManager.deleteProjects();
		cleanupResultFolder(TestTypes.REFACTORED_THEN_DELOMBOKED);
		cleanupResultFolder(TestTypes.DELOMBOKED_THEN_REFACTORED);
		
		//test
		projectManager.createProjects();
		createAndFillLibFolder();
		FolderManager folderManager = new FolderManager(resource, projectManager);

		LombokPlugin.getDefault().setFolderManager(folderManager);
		LombokPlugin.getDefault().setProjectManager(projectManager);
		
		TestFolderBuilder refactorThenDelombokRunner = TestFolderBuilderImpl.create(folderManager, TestTypes.BEFORE, writer);
		refactorThenDelombokRunner.refactor(TestTypes.REFACTORED);
//		refactorThenDelombokRunner.delombok(TestTypes.REFACTORED_THEN_DELOMBOKED);

//		TestFolderBuilderImpl delombokThenRefactorRunner = LombokTestRunner.create(folderManager, TestTypes.BEFORE, writer);
//		delombokThenRefactorRunner.delombok(TestTypes.DELOMBOKED);
//		delombokThenRefactorRunner.refactor(TestTypes.DELOMBOKED_THEN_REFACTORED);
		
		copyResults(folderManager, TestTypes.REFACTORED_THEN_DELOMBOKED);
		copyResults(folderManager, TestTypes.DELOMBOKED_THEN_REFACTORED);
	}

	public static void createAndFillLibFolder() throws IOException, CoreException{
		for (TestTypes type : TestTypes.values()) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(type.getName());
			IFolder folder = project.getFolder("lib");
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
			File file = new File(folder.getLocation().toString());
			if (!file.exists() || !file.isDirectory()) {
				throw new IllegalStateException("No library folder found to copy test libs to");
			}
			File workspaceRootDir = root.getLocation().toFile();
			File workspaceParent = workspaceRootDir.getParentFile();
			copyFile(new File(workspaceParent, "lib/test/junit.jar"), file);
			copyFile(new File(workspaceParent, "lib/test/lombok.jar"), file);
			folder.refreshLocal(IProject.DEPTH_ONE, null);
		}
	}
	
	private static void copyFile(File srcFile, File targetFolder) throws IOException {
		File targetLocation = new File(targetFolder, srcFile.getName());
		Files.copy(srcFile, targetLocation);
	}
	
	
	@SneakyThrows({CoreException.class, IOException.class})
	public void moveTestsToProject(IResource targetFolder){
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspaceRootDir = workspaceRoot.getLocation().toFile();
		File workspaceParent = workspaceRootDir.getParentFile();
		File testFolder = new File(workspaceParent, TEST_FOLDER);
		copyRecursively(testFolder, targetFolder.getLocation().toFile());
		targetFolder.refreshLocal(IProject.DEPTH_ONE, null);
	}
	
	private static void copyRecursively(File srcFolder, File targetFolder) throws IOException {
		Preconditions.checkArgument(srcFolder.isDirectory());
		Preconditions.checkArgument(targetFolder.isDirectory());
		
		for ( File item : srcFolder.listFiles()) {
			File target = new File(targetFolder, item.getName());
			if (item.isDirectory()) {
				if(!target.mkdir()) {
					throw new IllegalStateException("could not create folder: " + target);
				}
				
				copyRecursively(item, target);
			}
			else {
				Files.copy(item, target);
			}
		}
		
	}
	
	private static void copyResults(FolderManager folderManager, TestTypes testType) throws IOException {
		IFolder folder = folderManager.getFolder(testType);
		File testSubFolder = getResultFolder(testType);
		if (!testSubFolder.exists()) {
			Files.createParentDirs(testSubFolder);
			if(!testSubFolder.mkdir()) {
				throw new IllegalStateException("could not create folder: " + testSubFolder);
			}
		}
		copyRecursively(folder.getLocation().toFile(), testSubFolder);
	}
	
	public static void cleanupResultFolder(TestTypes testType) throws IOException {
		File testFolder = getResultFolder(testType);
		if (testFolder.exists()) {
			Files.deleteRecursively(testFolder);
		}
	}

	private static File getResultFolder(TestTypes testType) {
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		File workspaceRootDir = workspaceRoot.getLocation().toFile();
		File workspaceParent = workspaceRootDir.getParentFile();
		File testFolder = new File(workspaceParent, "results/" + testType.getName());
		return testFolder;
	}
}
