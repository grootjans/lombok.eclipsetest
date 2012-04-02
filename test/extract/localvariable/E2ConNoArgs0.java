package E2;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public class E2ConNoArgs0 {

	public void someMethod(){
		int oldName;
		/*1: ExtractLocalVariable(5, newName) :1*/
		oldName = 5;
		/*:1:*/
	}
}
