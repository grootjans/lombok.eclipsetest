package move.moveStaticMember;

import lombok.*;

public class M8SneakyThrows0 {
	/*1: MoveStaticElements(a10, targetClass) :1*/
	public static int a10 = method10();
	/*:1:*/
	
	/*2: MoveStaticElements(method10, targetClass) :2*/
	@SneakyThrows
	public static int method10(targetClass targetparam){
		return a10;
	}
	/*:2:*/

	@SneakyThrows
	private static int method10() {
		return 0;
	}
}
