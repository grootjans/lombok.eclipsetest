/**
 * 
 */
package lombokRefactorings.refactorings.eclipse.moveRefactorings;

import lombokRefactorings.refactorings.RefactoringUtils;
import lombokRefactorings.regex.RefactoringRequest;
import lombokRefactorings.regex.RegexUtilities;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.internal.corext.refactoring.structure.MoveStaticMembersProcessor;
import org.eclipse.jdt.internal.ui.preferences.JavaPreferencesSettings;
import org.eclipse.ltk.core.refactoring.participants.ProcessorBasedRefactoring;
/**
 * Moves all static elements tagged by <code>/*n: here :n*&#47;</code>; syntax for the refactoring request is <code>/*n: MoveStaticElements(package.targetClass) :n*&#47;</code>.
 * @author MaartenT
 *
 */
@SuppressWarnings("restriction")
public class MoveStaticElementsType extends AbstractMoveRefactoring{

	@Override
	public void run(RefactoringRequest request) throws Exception {
		CodeGenerationSettings settings = JavaPreferencesSettings.getCodeGenerationSettings(request.getCompilationUnit().getJavaProject());
		
		ICompilationUnit iCompilationUnit = request.getCompilationUnit();
		int firstChar = RegexUtilities.findRegex("\\s*?([^\\s])",
				iCompilationUnit.getSource(),
				request.getOpeningTagMatcher().end(),
				request.getClosingTagMatcher().start()).start();
		IMember member = (IMember) iCompilationUnit.getElementAt(firstChar);
		
		MoveStaticMembersProcessor processor = new MoveStaticMembersProcessor(new IMember[] {member}, settings);
		processor.setDestinationTypeFullyQualifiedName(request.getParameter(0));
		processor.checkInitialConditions(new NullProgressMonitor());
		ProcessorBasedRefactoring refactoring = new ProcessorBasedRefactoring(processor);
		RefactoringUtils.performRefactoring(refactoring);
	}
}
