package lombokRefactorings.folderOptions;

import java.io.FileWriter;
import java.io.IOException;

import lombokRefactorings.TestTypes;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.PartInitException;
public class LombokTestRunner {
	private IFolder sourceFolder;
	private FolderManager manager;
	private final FileWriter writer;
	
	public static LombokTestRunner create(FolderManager manager, TestTypes source, FileWriter writer) {
		return new LombokTestRunner(manager, source, writer);
	}
	
	private LombokTestRunner(FolderManager manager, TestTypes source, FileWriter writer) {
		this.manager = manager;
		this.writer = writer;
		this.sourceFolder = manager.getFolder(source);
	}

	public LombokTestRunner delombok(TestTypes folder) throws LombokTestRunnerException {
		sourceFolder = delombok(manager.getFolder(folder));
		return this;
	}

	public LombokTestRunner refactor(TestTypes folder) throws LombokTestRunnerException {
		sourceFolder = refactor(manager.getFolder(folder));
		return this;
	}

	public IFolder build() throws LombokTestRunnerException {
		return sourceFolder;
	}
	
	private IFolder refactor(IFolder outputFolder) throws LombokTestRunnerException {
		try {
			FileGenerator.refactorFilesInFolder(sourceFolder, outputFolder, writer);
		} catch (Exception e) {
			exceptionHandler(e);
		}
		return outputFolder;
	}

	private IFolder delombok(IFolder outputFolder) throws LombokTestRunnerException {
		try {
			FileGenerator.delombokFilesInFolder(sourceFolder, outputFolder);
		} catch (Exception e) {
			exceptionHandler(e);
		}
		return outputFolder;
	}
	
	private Exception exceptionHandler(Exception e) throws LombokTestRunnerException {
		
		if (e instanceof PartInitException
				|| e instanceof JavaModelException
				|| e instanceof CoreException
				|| e instanceof IOException) {
			throw new LombokTestRunnerException(e);
		}
		
		return e;
	}
}
