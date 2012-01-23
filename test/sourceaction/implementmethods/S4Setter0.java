package S4;


import lombok.Setter;

public class S4Setter0 implements IS4{

	@Setter int field = 0;
	
	/*1: CreateUnresolvedMethod() :1*/
	private void setField() {
		
		this.field = /*1 here 1*/createdMethod();;
	}
	/*:1:*/
}
