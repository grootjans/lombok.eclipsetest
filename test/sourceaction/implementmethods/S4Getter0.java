package S4;


import lombok.Getter;

public class S4Getter0 implements IS4{

	@Getter int fieldt = 0;
	@Getter int a;
	
	/*1: CreateUnresolvedMethod() :1*/
	private int getFieldt(){
		return fieldt;
	}
	/*:1:*/
	
	private int getA(){
		return createdMethod();
	}
	
}

