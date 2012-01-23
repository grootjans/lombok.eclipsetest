package S0;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class S0ConNoArgs0 {

	int oldName = 0;
	
	/*1: CreateUnresolvedMethod() :1*/
	private void method(){
		oldName = /*1 here 1*/createdMethod();
	}
	/*:1:*/
}
