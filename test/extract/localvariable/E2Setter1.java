package E2;

import lombok.Setter;

public class E2Setter1 {

	@Setter int name = 1;
	
	public void setName(){
		int oldName;
		/*1: ExtractLocalVariable(this.name, newName) :1*/
		oldName = this.name;
		/*:1:*/
	}
}
