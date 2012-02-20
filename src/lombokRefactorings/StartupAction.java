package lombokRefactorings;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import lombok.SneakyThrows;
import lombokRefactorings.activator.LombokPlugin;
import lombokRefactorings.folderOptions.FolderManager;
import lombokRefactorings.folderOptions.TestFolderBuilder.FolderBuilderException;
import lombokRefactorings.folderOptions.TestFolderBuilderImpl;
import lombokRefactorings.projectOptions.ProjectCreator;
import lombokRefactorings.projectOptions.ProjectManager;
import lombokRefactorings.unitTestOptions.AstManager;
import lombokRefactorings.unitTestOptions.TestRunnerBuilder;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;

public class StartupAction implements IStartup {
	
	private static final String HOST_FOLDER_NAME = "testNewProj";
	public static final PrintStream DEBUG_PRINTER = createPrinter("Debug console");
	
	@SneakyThrows(CoreException.class)
	@Override public void earlyStartup() {
		
		DEBUG_PRINTER.append("Startup Eclipse test actions");
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
		
		buildProjectsAndTest(testFolder);
	}
	
	@SneakyThrows({CoreException.class, FolderBuilderException.class})
	public void buildProjectsAndTest(IResource resource) {
		DEBUG_PRINTER.append("Creating projects");
		LombokPlugin.getDefault().setAstManager(new AstManager());
		ProjectManager projectManager = new ProjectManager();
		projectManager.createProjects();
		createAndFillLibFolder();
		FolderManager folderManager = new FolderManager(resource, projectManager);

		LombokPlugin.getDefault().setFolderManager(folderManager);
		LombokPlugin.getDefault().setProjectManager(projectManager);
		
		IFolder refactoredThenDelombokedFolder = TestFolderBuilderImpl.create(folderManager, TestTypes.BEFORE)
				.refactor(TestTypes.REFACTORED)
				.delombok(TestTypes.REFACTORED_THEN_DELOMBOKED)
				.build();
			
		IFolder delombokedThenRefactoredFolder = TestFolderBuilderImpl.create(folderManager, TestTypes.BEFORE)
				.delombok(TestTypes.DELOMBOKED)
				.refactor(TestTypes.DELOMBOKED_THEN_REFACTORED)
				.build();
		
		IFolder expected = folderManager.getFolder(TestTypes.EXPECTED);
			
		AstManager astManager = LombokPlugin.getDefault().getAstManager().initializeMaps(
				refactoredThenDelombokedFolder, 
				delombokedThenRefactoredFolder, 
				expected
		);
		
		IType runner = new TestRunnerBuilder(projectManager.getProject(TestTypes.TESTFILES), astManager)
			.setTests(folderManager.getFolder(TestTypes.DELOMBOKED_THEN_REFACTORED))
			.build();
		DEBUG_PRINTER.append("Starting up tests");
		new JUnitLaunchShortcut().launch(new StructuredSelection(runner), "run");
	}
	

	
	@SneakyThrows({CoreException.class, IOException.class})
	public static void createAndFillLibFolder() {
		for (TestTypes type : TestTypes.values()) {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			IProject project = root.getProject(type.getName());
			IFolder folder = project.getFolder("lib");
			if (!folder.exists()) {
				folder.create(true, true, null);
			}
			File file = new File(folder.getLocation().toString());
			if (!file.exists() || !file.isDirectory()) {
				DEBUG_PRINTER.println("No library folder found to copy test libs to");
				return;
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
		File testFolder = new File(workspaceParent, "test/simple");
		copyRecursively(testFolder, targetFolder.getLocation().toFile());
		targetFolder.refreshLocal(IProject.DEPTH_ONE, null);
	}
	
	private void copyRecursively(File srcFolder, File targetFolder) throws IOException {
		Preconditions.checkArgument(srcFolder.isDirectory());
		Preconditions.checkArgument(targetFolder.isDirectory());
		
		for ( File item : srcFolder.listFiles()) {
			File target = new File(targetFolder, item.getName());
			if (item.isDirectory()) {
				if(!target.mkdir()) {
					throw new IllegalStateException("could not create folder: " + target + " for tests");
				}
				
				copyRecursively(item, target);
			}
			else {
				Files.copy(item, target);
			}
		}
		
	}

	private static PrintStream createPrinter(String name) {
		MessageConsole mc = getOrCreateMessageConsole(name);
		return new PrintStream(mc.newMessageStream());
	}
	
	private static MessageConsole getOrCreateMessageConsole(String name) {
		IConsoleManager consoleManager = ConsolePlugin.getDefault().getConsoleManager();
		for(IConsole mc : consoleManager.getConsoles()) {
			if (name.equals(mc.getName())) {
				return (MessageConsole)mc;
			}
		}
		MessageConsole console = new MessageConsole(name, null);
		consoleManager.addConsoles(new IConsole[] {console});
		return console;
	}
}
