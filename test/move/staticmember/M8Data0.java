package move.moveStaticMember;

import lombok.*;

@Data
public class M8Data0 {
	/*1: MoveStaticElements(a4, targetClass) :1*/
	public static int a4 = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(method4, targetClass) :2*/
	public static int method4(targetClass targetparam){
		return a4;
	}
	/*:2:*/
}
