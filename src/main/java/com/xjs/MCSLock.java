package com.xjs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

import sun.misc.Unsafe;

public class MCSLock {

	public static class MCSNode {
		volatile MCSNode next;
		volatile boolean isBlock = true; //默认是在等待锁
	}
	
	volatile MCSNode queue; //指向最后一个申请锁的MCSNode
	private static final AtomicReferenceFieldUpdater<MCSLock, MCSNode>
	UPDATER = AtomicReferenceFieldUpdater.newUpdater(MCSLock.class, MCSNode.class, "queue");
	
	public void lock(MCSNode currentThread) {
		MCSNode predecessor = UPDATER.getAndSet(this, currentThread); //step 1
		if (predecessor != null) {
			predecessor.next = currentThread; //step 2
			while (currentThread.isBlock) { //step 3
				
			}
		} else { //只有一个线程在使用锁，没有前驱来通知他，所以得自己标记自己为非阻塞
			currentThread.isBlock = false;
		}
	}
	
	public void unlock(MCSNode currentThread) {
		if (currentThread.isBlock) { //锁拥有者进行释放锁才有意义
			return;
		}
		
		if (currentThread.next == null) { //检查是否有人排在自己后面
			if (UPDATER.compareAndSet(this, currentThread, null)) { //step 4
				//compareAndSet返回true表示确实没有人排在自己后面
				return;
			} else {
				//突然有人排在自己后面了，可能还不知道是谁，线面是等待后续者
				//这里之所以要忙等待是因为：step 1执行完后，step 2可能还能执行完
				while (currentThread.next == null) { //step 5
					
				}
			}
		}
		
		currentThread.next.isBlock = false;
		currentThread.next = null; //for GC
	}
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException {
		final Unsafe unsafe = Unsafe.getUnsafe();
		System.out.println(unsafe.objectFieldOffset
                (AbstractQueuedSynchronizer.class.getDeclaredField("state")));
//		final MCSLock mcsLock = new MCSLock();
//		ExecutorService es = Executors.newFixedThreadPool(10);
//		for (int i = 0; i < 10; i++) {
//			final int index = i;
//			es.submit(new Runnable() {
//				
//				public void run() {
//					MCSNode node = new MCSNode();
//					mcsLock.lock(node);
//					System.out.println("dead lock" + index);
//					mcsLock.unlock(node);
//				}
//			});
//		}
//		es.shutdown();
	}
}
