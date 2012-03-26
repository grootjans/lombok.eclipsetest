package I1;

import lombok.extern.java.Log;
import java.util.logging.*;

@Log
public class I1Log0 {
	
	public void someMethod() {
		/*1: InlineMethod(anotherMethod) :1*/
		int oldName = anotherMethod();
		/*:1:*/
		System.out.println(oldName);
	}
	public static int anotherMethod() {
		String[] array = {"ABC"};
		String warning = array[0];
		log.warning(warning);
		return array.length;
	}
		
}
