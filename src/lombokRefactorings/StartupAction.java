package lombokRefactorings;

import lombok.SneakyThrows;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.ui.IStartup;

public class StartupAction implements IStartup {
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
	}
}
