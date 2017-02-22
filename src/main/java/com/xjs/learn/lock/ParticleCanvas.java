package com.xjs.learn.lock;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;

public class ParticleCanvas extends Canvas {
	private Particle[] particles = new Particle[0];
	public ParticleCanvas(int size) {
		setSize(new Dimension(size, size));
	}
	
	protected synchronized void setParticles(Particle[] ps) {
		if (ps == null)
			throw new IllegalArgumentException("Cannot set null");
		
		particles = ps;
	}
	
	protected synchronized Particle[] getParticles() {
		return particles;
	}
	
	public void paint(Graphics g) {
		Particle[] ps = getParticles();
		
		for (Particle particle : ps) {
			particle.draw(g);
		}
	}
}
