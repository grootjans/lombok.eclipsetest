package move.moveStaticMember;

import lombok.*;

@NoArgsConstructor
public class M8ConNoArgs0 {
	/*1: MoveStaticElements(a2, targetClass) :1*/
	public static int a2 = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(method2, targetClass) :2*/
	public static int method2(targetClass targetparam){
		return a2;
	}
	/*:2:*/
}
