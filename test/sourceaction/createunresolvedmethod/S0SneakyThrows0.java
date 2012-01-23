package S0;


import lombok.SneakyThrows;

public class S0SneakyThrows0 {

	
	/*1: CreateUnresolvedMethod() :1*/
	@SneakyThrows
	private void go() {
		/*1 here 1*/createdMethod();
		throw new Throwable();
	}
	/*:1:*/
}
