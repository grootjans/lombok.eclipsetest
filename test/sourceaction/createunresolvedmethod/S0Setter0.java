package S0;


import lombok.Setter;

public class S0Setter0 {

	@Setter int field = 0;
	
	/*1: CreateUnresolvedMethod() :1*/
	private void setField(int newField) {
		
		this.field = /*1 here 1*/createdMethod(newField);;
	}
	/*:1:*/
}
