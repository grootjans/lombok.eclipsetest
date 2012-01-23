package S0;


import lombok.Synchronized;

public class S0Synchronized0 {
	/*1: CreateUnresolvedMethod() :1*/
	@Synchronized
	private void go() {
		/*1 here 1*/createdMethod();
		throw new Throwable();
	}
	/*:1:*/
}
