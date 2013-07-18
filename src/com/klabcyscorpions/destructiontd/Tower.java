package com.klabcyscorpions.destructiontd;


import android.graphics.Canvas;

public class Tower  {
	public int centerX, centerY;
	protected int numProjectiles;
	protected Projectiles[]  projectiles;

	private int tickCount;
	private int shotIndex;
	
	public void drawProjectiles(Canvas canvas) {
		for(int i = 0; i < numProjectiles; i++) projectiles[i].draw(canvas);
	
	
		
		for(int i = 0; i < numProjectiles; i++) projectiles[i].update(tickCount);
	}
	public void fireProjectiles() {
		projectiles[shotIndex].fireProjectiles();
		shotIndex++;
		if(shotIndex >= numProjectiles) shotIndex = 0;
	}
	
	public void rotateTower() {
		//xe - xt, ye - yt
		double radians = Math.atan2(centerX , centerY);
		int heading = (int) (-radians * (180 / Math.PI)) + 180;
	}
}
