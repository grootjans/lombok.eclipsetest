package E2;

import lombok.extern.java.Log;
import java.util.logging.*;

@Log
public class E2Log0 {
	
	public static void main(String[] args) {
		int otherName = 0;
		int oldName;
		/*1: ExtractLocalVariable(otherName, newName) :1*/
		oldName = otherName;
		/*:1:*/
		log.warning(String.valueOf(oldName));
	}
}
