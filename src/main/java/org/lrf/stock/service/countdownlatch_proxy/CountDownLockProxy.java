package org.lrf.stock.service.countdownlatch_proxy;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountDownLockProxy {

	private  Logger  logger = LoggerFactory.getLogger(CountDownLockProxy.class);
	
	public void procced(int countDownLatchNumber, CountDownLatchUser user,boolean isBlock) throws InterruptedException {


		CountDownLatch cdl = null;
		if (isBlock) {
			logger.debug("*** Create CountDownLatch ***");
			cdl = new CountDownLatch(countDownLatchNumber);
		}
			
		user.useCountDownLatch(cdl);

		if (isBlock) {
			cdl.await();
			logger.debug("***  Wait *** ") ;
		}
	}
}
