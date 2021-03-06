package lombokRefactorings.refactorings.eclipse.sourceActions;

import lombokRefactorings.refactorings.IRefactoringType;
import lombokRefactorings.refactorings.RefactoringUtils;
import lombokRefactorings.refactorings.eclipse.EditorBasedRefactoringType;
import lombokRefactorings.regex.RefactoringRequest;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.internal.corext.codemanipulation.AddUnimplementedMethodsOperation;
import org.eclipse.jdt.internal.corext.codemanipulation.StubUtility2;
import org.eclipse.jdt.internal.corext.dom.ASTNodes;
import org.eclipse.jdt.internal.ui.actions.SelectionConverter;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;




/**
 * 
 * NOTE: Currently this refactoring implements and overrides all methods of superclasses and interfaces.
 * <p>
 * NOTE: By default the refactoring inserts the methods in the beginning of the class. 
 * if the methods need to another place, <code> true </code> needs to be given as a paramter. 
 * Furthermore a here tag needs to be added to the place where the methods need to be inserted.
 * <p>
 * NOTE: Currently the refactorings does generate comments within the methods
 * 
 * @author SaskiaW
 *
 */

public class OverrideMethodsSourceActionType extends EditorBasedRefactoringType implements IRefactoringType {

	@Override
	public void doRefactoring(JavaEditor editor,
			ICompilationUnit iCompilationUnit, RefactoringRequest request) throws CoreException {

		int position = checkPositionGiven(request);

		final IType type = SelectionConverter.getTypeAtOffset(editor);
		CompilationUnit fUnit = RefactoringUtils.parse(iCompilationUnit);
		final ITypeBinding binding = ASTNodes.getTypeBinding(fUnit, type);
		final IMethodBinding[] methodsToImplement = StubUtility2.getOverridableMethods(fUnit.getAST(), binding, false);

		AddUnimplementedMethodsOperation operation = new AddUnimplementedMethodsOperation(fUnit, binding, methodsToImplement, position, true, true, true);
		operation.run(null);
	}

	private int checkPositionGiven(RefactoringRequest request) throws JavaModelException {

		int position = 0;

		if(request.getParameters().size() > 0) {
			if (request.getParameter(0).equals("true")){
				position = request.findHereTags().get(0).end();
			}
		}

		return position;
	}
}
