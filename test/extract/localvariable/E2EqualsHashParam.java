package E2;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(exclude={"field"})
public class E2EqualsHashParam {

	public void someMethod(){
		int oldName;
		/*1: ExtractLocalVariable(5, newName) :1*/
		oldName = 5;
		/*:1:*/
	}
	
}
