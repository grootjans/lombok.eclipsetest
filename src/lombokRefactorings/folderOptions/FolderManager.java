package lombokRefactorings.folderOptions;

import static lombokRefactorings.TestTypes.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombokRefactorings.TestTypes;
import lombokRefactorings.guiAction.LombokResourceAction;
import lombokRefactorings.projectOptions.ProjectManager;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class FolderManager {
	
	private final static Map<TestTypes, IFolder> folders = new HashMap<TestTypes, IFolder>();

	private final IResource resource;

	private final ProjectManager projectManager;
	
	public FolderManager(IResource resource, ProjectManager projectManager) throws CoreException {
		this.resource = resource;
		this.projectManager = projectManager;
		copyFiles();
	}
	
	public FolderManager(LombokResourceAction action, ProjectManager projectManager) throws CoreException {
		this.resource = action.getResource();
		this.projectManager = projectManager;
		copyFiles();
	}
	
	/**
	 * First deletes the existing folders and then creates a new project. 
	 * Creates the test folder and subfolders and copies it to the source of the project. 
	 * @throws CoreException 
	 */
	private void copyFiles() throws CoreException {		
		try {
			initializeFolders();
			
			copyTestsToBeforeFolder(resource);
			
			createSubFolders(folders.get(REFACTORED));
			createSubFolders(folders.get(DELOMBOKED));
			createSubFolders(folders.get(DELOMBOKED_THEN_REFACTORED));
			createSubFolders(folders.get(REFACTORED_THEN_DELOMBOKED));
			
			FileGenerator.correctPackageDeclarations(folders.get(BEFORE));
			
			projectManager.refreshProjects();
		}
		catch (Exception e) {
			System.err.println("Copy files failed: " + e);
			e.printStackTrace();
		}
	}

	private void initializeFolders() {
		for (TestTypes type: TestTypes.values()) {
			IFolder srcFolder = getSrcFolder(type);
			if (srcFolder != null) {
				folders.put(type, srcFolder);
			}
		}
	}
	
	private IFolder getSrcFolder(TestTypes type){
		return (IFolder) projectManager.getProject(type).findMember("src");
	}
	
	public static File toFile(IFolder folder){
		return folder.getRawLocation().toFile();
	}
	
	private void copyTestsToBeforeFolder(IResource resource) throws CoreException {
		if (resource != null) {
			Path path = new Path(getSrcFolder(BEFORE).getFullPath().toString() + "/" + resource.getName());
			resource.copy(path, true, null);
		}
	}
	
	/**
	 * Used by copyFiles() to create the subfolders of the before folder in the other folders.
	 * 
	 * @param folderName : Is the name of the name of the folder to which the folders of before are copied
	 * @throws CoreException
	 * @throws IOException
	 */
	private void createSubFolders(IFolder folderName) throws CoreException, IOException {
		
		File pathFolder = new File(folderName.getLocation().toString());

		createSubFolders(folders.get(BEFORE), pathFolder);
	}

	/**
	 * Creates the subfolders of the folders created by createFolders.
	 * 
	 * @param folder Folder used to look through and call back recursively.
	 * @param pathFolder Used to get the path in which to set the next folder.
	 * @throws CoreException
	 * @throws IOException
	 */
	private void createSubFolders(IFolder folder, File pathFolder) throws CoreException, IOException {
		for (IResource resource : folder.members()) {
			if (resource.getClass().getSimpleName().equals("Folder") && !(resource.getName().equals("expected"))) {
				File folderSub = new File(pathFolder.getCanonicalPath() + "/" + resource.getName());
				folderSub.mkdir();
				createSubFolders((IFolder) resource, folderSub);
			}
		}
	}

	public IFolder getFolder(TestTypes folder) {
		return folders.get(folder);
	}
}
