package S1;
import nonExistingPackage.*;

import lombok.Getter;

public class S1Getter0 {

	@Getter int fieldt = 0;
	@Getter int a;
	
	/*1: ToggleComment() :1*/
	private int getFieldt(){
		return fieldt;
	}
	/*:1:*/
	
	private int getA(){
		
		return a;
	}
	
}

