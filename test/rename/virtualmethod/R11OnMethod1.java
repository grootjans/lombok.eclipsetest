package R11;

import lombok.Getter;

public class R11OnMethod1 {

	@Getter int name = 1;
	
	@Deprecated
	/*1: RenameVirtualMethod(getName, getSomething) :1*/
	public int getName(){
		return name;
	}


	/*:1:*/
	
	
}
