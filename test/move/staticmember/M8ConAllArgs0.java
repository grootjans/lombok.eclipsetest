package move.moveStaticMember;

import lombok.*;

@RequiredArgsConstructor
public class M8ConAllArgs0 {
	/*1: MoveStaticElements(a1, targetClass) :1*/
	public static int a1 = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(method1, targetClass) :2*/
	public static int method1(targetClass targetparam){
		return a1;
	}
	/*:2:*/
}
