package I1;
import java.util.Collection;

import lombok.*;


@EqualsAndHashCode
public class I1EqualsHash0 {
	
	public void someMethod() {
		/*1: InlineMethod(anotherMethod) :1*/
		String oldName = anotherMethod();
		/*:1:*/
		System.out.println(oldName);
	}
	public String anotherMethod(){
		/*2: InlineLocalVariable(oldName) :2*/
		String oldName = "inlined";
		/*:2:*/
		return oldName;
	}
}
