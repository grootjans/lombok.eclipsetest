package S4;

import lombok.Synchronized;

public class S4Synchronized0 implements IS4 {
	/* 1: CreateUnresolvedMethod() :1 */
	@Synchronized
	private void go() {
		if (System.currentTimeMillis() / 2 == 0) {

			/* 1 here 1 */createdMethod();
		} else {
			throw new Throwable();
		}
	}
	/* :1: */
}
