package lombokRefactorings.projectOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.PreferenceConstants;

public class ProjectCreator {
	
	private static final String[] JAVA_NATURE = new String[] { JavaCore.NATURE_ID };

	public IProject createProject(String projectName, String...libNames) throws CoreException {
		//create and open project
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceRoot.getProject(projectName);
		project.create(null);
		project.open(null);
		
		//set java nature
		IProjectDescription description = project.getDescription();
		description.setNatureIds(JAVA_NATURE);
		project.setDescription(description, null);
		
		//create src&lib folder
		IFolder sourceFolder = project.getFolder("src");
		sourceFolder.create(false, true, null);
		IFolder libFolder = project.getFolder("lib");
		libFolder.create(false, true, null);
		
		//set output location
		IJavaProject javaProject = JavaCore.create(project);
		IFolder binFolder = project.getFolder("bin");
		javaProject.setOutputLocation(binFolder.getFullPath(), null);
		
		//set classpath
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		entries.addAll(Arrays.asList(PreferenceConstants.getDefaultJRELibrary()));
//		if (projectName.equals(TestTypes.TESTFILES.getName())) {
		for (String libName : libNames) {
			entries.add(JavaCore.newLibraryEntry(libFolder.getFile(libName).getLocation(), null, null));
		}
//			entries.add(JavaCore.newLibraryEntry(libFolder.getFile("lombok.jar").getLocation(), null, null));
//		}
		IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(sourceFolder);
		entries.add(JavaCore.newSourceEntry(packageRoot.getPath()));
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[0]), null);
		return project;
	}
}
