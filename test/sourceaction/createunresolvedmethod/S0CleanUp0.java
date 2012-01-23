package S0;


import java.io.*;

import lombok.Cleanup;

public class S0CleanUp0 {
	
	/* 1: CreateUnresolvedMethod() :1 */
	private static void main(String[] args) throws IOException {
		@Cleanup
		InputStream in = new FileInputStream(args[0]);
		@Cleanup
		OutputStream out = new FileOutputStream(args[1]);
		byte[] b = new byte[10000];
		while (true) {
			int r = /*1 here 1*/createdMethod();
			if (r == -1) break;
			out.write(b, 0, r);
		}
	}
	/* :1: */
}
