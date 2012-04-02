package R4;

import java.io.*;
import java.io.InputStream;

import lombok.Cleanup;
/*1: RenameClass(R4CleanUp0_R) :1*/
public class R4CleanUp0 {
/*:1:*/
	
	int oldName = 5;

	public void method() throws IOException {
		
		@Cleanup
		InputStream in = new FileInputStream("");
		@Cleanup
		OutputStream out = new FileOutputStream("");
		
		byte[] b = new byte[10000];
		while (true) {
			int r = in.read(b);
			if (r == -1)
				break;
			out.write(b, oldName, r);
		}
	}
}
