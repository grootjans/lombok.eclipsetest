package lombokRefactorings.folderOptions;

import java.io.FileWriter;

import lombok.SneakyThrows;
import lombokRefactorings.TestTypes;

import org.eclipse.core.resources.IFolder;
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

	@SneakyThrows
	public void delombok(TestTypes folder) {
		IFolder outputFolder = manager.getFolder(folder);
		FileGenerator.delombokFilesInFolder(sourceFolder, outputFolder);
		sourceFolder = outputFolder;
	}

	@SneakyThrows
	public void refactor(TestTypes folder) {
		IFolder outputFolder = manager.getFolder(folder);
		FileGenerator.refactorFilesInFolder(sourceFolder, outputFolder, writer);
		sourceFolder = outputFolder;
	}
}
