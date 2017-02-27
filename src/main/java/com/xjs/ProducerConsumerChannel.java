package com.xjs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.Puttable;
import EDU.oswego.cs.dl.util.concurrent.Takable;

public class ProducerConsumerChannel {
	static class Producer implements Runnable {
		final Puttable chan;
		final AtomicInteger ato = new AtomicInteger(0);

		Producer(Puttable channel) {
			chan = channel;
		}

		public void run() {
			try {
				for (;;) {
					chan.put(produce());
					TimeUnit.MILLISECONDS.sleep(500);
				}
			} catch (InterruptedException ex) {
			}
		}

		Object produce() {
			return ato.getAndIncrement();
		}
	}

	static class Consumer implements Runnable {
		final Takable chan;

		Consumer(Takable channel) {
			chan = channel;
		}

		public void run() {
			try {
				for (;;) {
					consume(chan.take());
				}
			} catch (InterruptedException ex) {
			}
		}

		void consume(Object x) {
			System.out.println(x);
		}
	}
	
	public static void main(String[] args) {
		Channel chan = new LinkedQueue();
		Producer p = new Producer(chan);
		Consumer c = new Consumer(chan);
		new Thread(p).start();
		new Thread(c).start();
	}

}
