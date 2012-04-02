package E2;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class E2ConReqArgs0 {

	public void someMethod(){
		int oldName;
		/*1: ExtractLocalVariable(5, newName) :1*/
		oldName = 5;
		/*:1:*/
	}
	
}
