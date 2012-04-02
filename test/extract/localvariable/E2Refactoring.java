package E2;

public class E2Refactoring {
	private int oldName=0;

	public void someMethod() {
		int oldName;
		/* 1: ExtractLocalVariable(this.oldName, newName) :1 */
		oldName = this.oldName;
		/* :1: */
	}
}
