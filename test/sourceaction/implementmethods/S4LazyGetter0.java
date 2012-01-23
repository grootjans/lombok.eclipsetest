package S4;


import lombok.Getter;

public class S4LazyGetter0 {

	@Getter(lazy=true) int fieldt = 0;
	@Getter(lazy=true) int a;
	
	/*1: CreateUnresolvedMethod() :1*/
	private int getFieldt(){
		return fieldt;
	}
	/*:1:*/
	
	private int getA(){
		a=createdMethod();
		return a;
	}
	
}
