package move.moveStaticMember;

import lombok.*;

public class M8Setter0 {
	public targetClass targetfield = new targetClass();
	/*1: MoveStaticElements(field, targetfield) :1*/
	@Setter
	public static int a = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(parameter, targetparam) :2*/
	public static int method(targetClass targetparam){
		return a;
	}
	/*:2:*/
}
