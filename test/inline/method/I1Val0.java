package I1;

import lombok.val;

public class I1Val0 {

	public void someMethod() {
		/*1: InlineMethod(anotherMethod) :1*/
		val greeting = anotherMethod();
		/*:1:*/
		System.out.println(greeting);
	}
	public String anotherMethod(){
		return "Hello";
	}
	
}
