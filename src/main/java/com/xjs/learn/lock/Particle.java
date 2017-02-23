package com.xjs.learn.lock;

import java.awt.Graphics;
import java.util.Random;

/**
 * 永远只是在更新对象的成员变量时加锁
 * 永远只是在访问有可能被更新对象的成员变量时才加锁
 * 永远不要在调用其他对象的方法时加锁
 * @author xjs
 *
 */
public class Particle {
	protected int x;
	protected int y;
	
	protected final Random rnd = new Random();
	
	public Particle(int initalX, int initalY) {
		x = initalX;
		y = initalY;
	}
	
	public synchronized void move() {
		x += rnd.nextInt(10) - 5;
		y += rnd.nextInt(20) - 10;
	}
	
	public void draw(Graphics g) {
		int lx, ly;
		synchronized (this) {
			lx = y; ly =y;
		}
		g.drawRect(lx, ly, 10, 10);
	}
}
