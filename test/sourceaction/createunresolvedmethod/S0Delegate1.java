package S0;


import java.util.ArrayList;
import java.util.Collection;

import lombok.Delegate;


public class S0Delegate1 {

	private interface SimpleCollection {
		boolean add(String item);
		/*1: CreateUnresolvedMethod() :1*/
		boolean remove(Object o);
		/*:1:*/
	}

	@Delegate(types=SimpleCollection.class)
	private final Collection<String> collection = /*1 here 1*/createdMethod();;

	
	
}
