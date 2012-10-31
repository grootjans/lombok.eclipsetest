package move.moveStaticMember;

import lombok.*;

public class M8Synchronized0 {
	/*1: MoveStaticElements(a11, targetClass) :1*/
	public static int a11 = method11();
	/*:1:*/
	
	/*2: MoveStaticElements(method11, targetClass) :2*/
	@Synchronized
	public static int method11(targetClass targetparam){
		return a;
	}
	/*:2:*/

	@Synchronized
	private static int method11() {
		return 0;
	}
}
