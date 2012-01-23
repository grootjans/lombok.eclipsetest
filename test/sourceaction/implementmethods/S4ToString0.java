package S4;


import lombok.ToString;

@ToString
public class S4ToString0 implements IS4{

	int field = 0;
	
	/*1: CreateUnresolvedMethod() :1*/
	private String method(){
		return new String(/*1 here 1*/String.valueOf(createdMethod()) + String.valueOf(field);
	}
	/*:1:*/
	
}
