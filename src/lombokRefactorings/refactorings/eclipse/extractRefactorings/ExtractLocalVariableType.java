package lombokRefactorings.refactorings.eclipse.extractRefactorings;

import java.util.regex.Matcher;

import lombokRefactorings.refactorings.RefactoringUtils;
import lombokRefactorings.regex.RefactoringRequest;

import org.eclipse.jdt.internal.corext.refactoring.code.ExtractTempRefactoring;
@SuppressWarnings("restriction")
/**
 * Performs the extract local variable refactoring
 * 
 * @param start The start of the initialization which should be extracted to a local variable.
 * @param length The length of the initialization which should be extracted to a local variable
 * @param newName The name which should be given to the extracted local variable
 * 
 * @author SaskiaW
 * 
 * @throws CoreException
 */
public class ExtractLocalVariableType extends AbstractExtractRefactoring {
	@Override
	public void run(RefactoringRequest request) throws Exception {
		Matcher targetMatch = RefactoringUtils.findTarget(request); 
		int start = targetMatch.start();
		int length = targetMatch.end() - start; 
		ExtractTempRefactoring refactor= new ExtractTempRefactoring(request.getCompilationUnit(), start, length);
		refactor.setTempName(RefactoringUtils.getNewName(request));
		RefactoringUtils.performRefactoring(refactor);
	}
}
