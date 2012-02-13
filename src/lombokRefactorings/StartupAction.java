package lombokRefactorings;

import java.io.PrintStream;

import lombok.SneakyThrows;
import lombokRefactorings.activator.LombokPlugin;
import lombokRefactorings.folderOptions.FolderManager;
import lombokRefactorings.folderOptions.TestFolderBuilderImpl;
import lombokRefactorings.folderOptions.TestFolderBuilder.FolderBuilderException;
import lombokRefactorings.projectOptions.ProjectManager;
import lombokRefactorings.unitTestOptions.AstManager;
import lombokRefactorings.unitTestOptions.TestRunnerBuilder;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.junit.launcher.JUnitLaunchShortcut;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class StartupAction implements IStartup {
	
	public static final PrintStream DEBUG_PRINTER = createPrinter("Debug console");
	
	@SneakyThrows(CoreException.class)
	@Override public void earlyStartup() {
		String hostProjectName = "testNewProj"; //TODO: Transfer this config via -D or some other mechanism.
		
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
			hostProject = workspaceRoot.getProject(hostProjectName);
			hostProject.create(null);
			hostProject.open(null);
			IProjectDescription description = hostProject.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			hostProject.setDescription(description, null);
		} else {
			hostProject.open(null);
		}
		
		IFolder testFolder = hostProject.getFolder("foobar");
		if (!testFolder.exists()) testFolder.create(true, true, null);
		
		buildProjectsAndTest(testFolder);
	}
	
	public void buildProjectsAndTest(IResource resource) {
		
		try {
			LombokPlugin.getDefault().setAstManager(new AstManager());
			final ProjectManager projectManager = new ProjectManager();
			final FolderManager folderManager = new FolderManager(resource, projectManager);
			
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
			
			new JUnitLaunchShortcut().launch(new StructuredSelection(runner), "run");
	
	//		projectManager.deleteProjects();
		} catch (CoreException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (FolderBuilderException e) {
			throw new RuntimeException(e.getMessage(), e);
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
