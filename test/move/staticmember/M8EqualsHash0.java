package move.moveStaticMember;

import lombok.*;

@EqualsAndHashCode
public class M8EqualsHash0 {
	public targetClass targetfield = new targetClass();
	/*1: MoveStaticElements(field, targetfield) :1*/
	public static int a = 0;
	/*:1:*/
	
	/*2: MoveStaticElements(parameter, targetparam) :2*/
	public static int method(targetClass targetparam){
		return a;
	}
	/*:2:*/
}
