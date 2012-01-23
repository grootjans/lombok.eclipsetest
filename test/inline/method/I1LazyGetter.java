package I1;
import lombok.*;


public class I1LazyGetter {
	@Getter(lazy=true)
	private final int oldName = 1;

	
	public void someMethod() {
		/*1: InlineMethod(anotherMethod) :1*/
		String oldName = anotherMethod();
		/*:1:*/
		System.out.println(oldName);
	}
	public String anotherMethod(){
		String oldName="inlined";
		return oldName;
	}
}
