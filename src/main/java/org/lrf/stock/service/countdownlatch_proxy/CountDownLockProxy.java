package org.lrf.stock.service.countdownlatch_proxy;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Component;

@Component
public class CountDownLockProxy {

	public void procced(int countDownLatchNumber, CountDownLatchUser user,boolean isBlock) throws InterruptedException {

		System.out.println("*** create CountDownLatch ***");

		CountDownLatch cdl = null;
		if (isBlock)
			cdl = new CountDownLatch(countDownLatchNumber);

		user.useCountDownLatch(cdl);

		if (isBlock) {
			System.out.println("*** current thread wait *** " + Thread.currentThread());
			cdl.await();
			System.out.println("*** current thread start *** " + Thread.currentThread());
		}
	}
}
