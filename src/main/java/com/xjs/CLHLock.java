package com.xjs;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CLHLock {
	public static class CLHNode {
		private final String name;
		private volatile boolean isBlock = true;
		public CLHNode(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return name;
		}
	}

	public volatile CLHNode tail;
	public static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER = AtomicReferenceFieldUpdater
			.newUpdater(CLHLock.class, CLHNode.class, "tail");

	public void lock(CLHNode currentThread) {
		CLHNode pre = UPDATER.getAndSet(this, currentThread);
		if (pre != null) {
			while (pre.isBlock) {

			}
		}
	}

	public void unlock(CLHNode currentThread) {
		if (!UPDATER.compareAndSet(this, currentThread, null)) {
			currentThread.isBlock = false;
		}
	}

	public static void main(String[] args) {
		System.out.println(new CLHNode("hello").toString());
//		final CLHLock clhLock = new CLHLock();
//		ExecutorService es = Executors.newFixedThreadPool(10);
//		for (int i = 0; i < 2; i++) {
//			final int index = i;
//			es.submit(new Runnable() {
//
//				public void run() {
//					CLHNode node = new CLHNode(index + "");
//					clhLock.lock(node);
//					System.out.println(index + " acquire lock");
//					clhLock.unlock(node);
//				}
//			});
//		}
//		es.shutdown();
	}
}
