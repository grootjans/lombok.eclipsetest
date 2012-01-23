package S0;


import lombok.ToString;

@ToString
public class S0ToString0 {

	int field = 0;
	
	/*1: CreateUnresolvedMethod() :1*/
	private String method(){
		return new String(/*1 here 1*/createdMethod() + String.valueOf(field);
	}
	/*:1:*/
	
}
