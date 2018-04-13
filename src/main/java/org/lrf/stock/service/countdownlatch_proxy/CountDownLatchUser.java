package org.lrf.stock.service.countdownlatch_proxy;

import java.util.concurrent.CountDownLatch;

@FunctionalInterface
public interface CountDownLatchUser {
	public void useCountDownLatch(CountDownLatch countDownLatch);
}
