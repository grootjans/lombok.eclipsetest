package I1;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class I1ConReqArgs0 {
		
		public void someMethod() {
			/*1: InlineMethod(anotherMethod) :1*/
			int oldName = anotherMethod();
			/*:1:*/
			System.out.println(oldName);
		}
		public int anotherMethod(){
			String oldName="inlined";
			System.out.println(oldName);
			return oldName.length();
	
	}
	
}
