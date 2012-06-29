package lombokRefactorings.folderOptions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import lombok.SneakyThrows;
import lombokRefactorings.TestTypes;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.ltk.core.refactoring.PerformRefactoringOperation;

public class LombokTestRunner {
	private IFolder sourceFolder;
	private FolderManager manager;
	
	private static FileWriter writer = null;

	public static LombokTestRunner create(FolderManager manager, TestTypes source) {
		return new LombokTestRunner(manager, source);
	}
	
	@SneakyThrows(IOException.class)
	private LombokTestRunner(FolderManager manager, TestTypes source) {
		this.manager = manager;
		this.sourceFolder = manager.getFolder(source);
		
		File file = ((IProject) manager.getResource().getParent()).getFile("lombokrefactor.log").getLocation().toFile();
		writer = new FileWriter(file);
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
		FileGenerator.refactorFilesInFolder(sourceFolder, outputFolder);
		sourceFolder = outputFolder;
	}

	@SneakyThrows(IOException.class)
	public void close() {
		writer.close();
	}
	
	@SneakyThrows(IOException.class)
	public static void logToFile(String s) {
		if (writer != null) {
			writer.write(s);
			writer.flush();
		}
	}

	public static void logResultToFile(PerformRefactoringOperation op) {
		logToFile(op.getConditionStatus() != null && op.getConditionStatus().hasError() ?
					" FAILED: " + op.getConditionStatus().getEntryWithHighestSeverity().getMessage() : " OK");
	}

	public static void logToFile(String string, Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stacktrace = sw.toString();
		logToFile(string + stacktrace);
	}
}
