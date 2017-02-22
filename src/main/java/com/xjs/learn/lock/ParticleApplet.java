package com.xjs.learn.lock;

import java.applet.Applet;

public class ParticleApplet extends Applet {
	protected Thread[] threads = null;
	
	protected final ParticleCanvas canvas = new ParticleCanvas(100);
	
	public void init() {
		add(canvas);
	}
	
	protected Thread makeThread(final Particle p) {
		Runnable runloop = new Runnable() {
			
			public void run() {
				try {
					for (;;) {
						p.move();
						canvas.repaint();
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
					System.out.println("I am interrupted");
					System.out.println(Thread.currentThread().isInterrupted());
					return;
				}
			}
		};
		
		return new Thread(runloop);
	}
	
	public synchronized void start() {
		int n = 1;
		
		if (threads == null) {
			Particle[] particles = new Particle[n];
			for (int i = 0; i < n; ++i) {
				particles[i] = new Particle(50, 50);
			}
			canvas.setParticles(particles);
			
			threads = new Thread[n];
			for (int i = 0; i < n; ++i) {
				threads[i] = makeThread(particles[i]);
				threads[i].start();
			}
		}
	}
	
	public synchronized void stop() {
		if (threads != null) {
			for (Thread thread : threads) {
				thread.interrupt();
				System.out.println(thread.isInterrupted());
			}
			threads = null;
		}
	}
}
