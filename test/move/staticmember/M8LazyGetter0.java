package move.moveStaticMember;

import lombok.*;

public class M8LazyGetter0 {
	/*1: MoveStaticElements(a7, targetClass) :1*/
	@Getter(lazy=true)
	private final static int a7 = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(method7, targetClass) :2*/
	public static int method7(targetClass targetparam){
		return a7;
	}
	/*:2:*/
}
