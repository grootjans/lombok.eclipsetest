package I2;
import lombok.ToString;

@ToString
public class I2ToString0 {
	
	public static void method() {
		/*1: InlineLocalVariable(object) :1*/
		I2ToString0 object = new I2ToString0();
		/*:1:*/
		System.out.println(object.toString());
	}

}
