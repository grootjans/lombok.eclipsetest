package lombokRefactorings.guiAction;

import java.io.PrintStream;

import lombok.SneakyThrows;
import lombokRefactorings.StartupAction;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;

public class LombokResourceAction implements IObjectActionDelegate {
	// TODO remove or close where appropriate 
	public static final PrintStream DEBUG_PRINTER = createPrinter("Debug console");
	private IResource resource;
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null && !selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				resource = ((IResource) ((IStructuredSelection) selection).getFirstElement());
			}
		}
	}
	
	@SneakyThrows
	public void run(IAction action) {	
		DEBUG_PRINTER.println(resource.toString());
		StartupAction.buildProjectsAndTest(resource);
	}

	private static PrintStream createPrinter(String name) {
		MessageConsole myConsole;
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				myConsole = (MessageConsole) existing[i];
		// no console found, so create a new one
		myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		return new PrintStream(myConsole.newMessageStream());
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// We are not interested in parts
	}

	public IResource getResource() {
		return resource;
	}
}
