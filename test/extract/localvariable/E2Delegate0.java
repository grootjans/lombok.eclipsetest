package E2;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Delegate;

public class E2Delegate0 {

	@Delegate(types=SimpleCollection.class)
	private final Collection<String> collection = new ArrayList<String>();
	
	private interface SimpleCollection {
		boolean add(String item);
		boolean remove(Object item);
	}
	public void someMethod(){
		Collection<String> localCollection;
		/*1: ExtractLocalVariable(collection, newName) :1*/
		localCollection = collection;
		/*:1:*/
	}
}



