package E2;

import lombok.Data;

@Data
public class E2Data0 {
	private int oldName = 0;
	public void someMethod(){
		int oldName;
		/*1: ExtractLocalVariable(this.oldName, newName) :1*/
		oldName = this.oldName ;
		/*:1:*/
	}

}
