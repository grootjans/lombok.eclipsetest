package S4;


import lombok.Getter;

public class S4OnMethod1 implements IS4{

	@Getter int name = 1;
	
	@Deprecated
	/*1: CreateUnresolvedMethod() :1*/
	private int getName(){
		name = createdMethod();
		return /*1 here 1*/name;
	}


	/*:1:*/
	
	
}
