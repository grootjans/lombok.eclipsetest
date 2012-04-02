package E2;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class E2ConAllArgs0 {

	public void someMethod(){
		int oldName;
		/*1: ExtractLocalVariable(5, newName) :1*/
		oldName = 5;
		/*:1:*/
	}
}
