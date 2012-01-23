package I1;

import lombok.Setter;

public class I1Setter0 {

	@Setter int name = 1;
	
	public void someMethod() {
		/*1: InlineMethod(setName) :1*/
		String oldName = setName();
		/*:1:*/
		System.out.println(oldName);
	}
	public String setName(){
		String oldName="inlined";
		return oldName;
	}
}
