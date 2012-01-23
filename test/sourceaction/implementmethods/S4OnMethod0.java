package S4;


import lombok.Getter;

public class S4OnMethod0 {

	@Getter(onMethod=@Deprecated) int name = 1;
	/*1: CreateUnresolvedMethod() :1*/
	private int getName(){
		return /*1 here 1*/createdMethod(name);
	}
	/*:1:*/
}
