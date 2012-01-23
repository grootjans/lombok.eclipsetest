package S0;


import lombok.Getter;

public class S0LazyGetter0 {

	@Getter(lazy=true) int fieldt = 0;
	@Getter(lazy=true) int a;
	
	/*1: CreateUnresolvedMethod() :1*/
	private int getFieldt(){
		return fieldt;
	}
	/*:1:*/
	
	private int getA(){
		/*1 here 1*/createdMethod();
		return a;
	}
	
}
