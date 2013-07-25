package com.klabcyscorpions.destructiontd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;

import com.example.destructiontd.R;

public class Enemy {
	static final float ENEMY_SPEED = 3;
	private Bitmap enemyBitmap;
	private long stateTime = 0;
	private float startX, startY;
	boolean releaseEnemy;
	boolean releaseEnemy2;
	int spawnArea;
	float destinationX, destinationY;
	
	public Enemy(Context context, int spawnArea, float spawnX, float spawnY){
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		enemyBitmap = BitmapFactory.decodeResource(context.getResources(),
                									R.drawable.enemy);
		startX = spawnX;
		startY = spawnY;
		this.spawnArea = spawnArea;
		destinationX = GameThread.getScreenWidth()/2;
		destinationY = GameThread.getScreenHeight()/2;
	}
	
	public boolean isReleaseEnemy() {
		// TODO Auto-generated method stub
		return releaseEnemy;
	}


	public void update(float delta) {
		// TODO Auto-generated method stub
		stateTime += delta;
		if(stateTime < 100){
			
			switch (spawnArea){
			case GameThread.SPAWN_BOTTOM:
				startY -= ENEMY_SPEED;
				if(destinationY - Math.abs(startY) > 0)
				{
				releaseEnemy = true;
			}	
				break;
			case GameThread.SPAWN_RIGHT:
				startX -= ENEMY_SPEED;
				if((destinationX - Math.abs(startX) > 0))
				{
				releaseEnemy = true;
			}
				break;
			case GameThread.SPAWN_TOP:
				startY += ENEMY_SPEED;
				if((destinationY - Math.abs(startY) < 0))
				{
				releaseEnemy = true;
			}
				break;
			case GameThread.SPAWN_LEFT:
				startX += ENEMY_SPEED;
				if(destinationX - Math.abs(startX) < 0)
				{
				releaseEnemy = true;
			}
				break;

			}
		}
	stateTime -= 100;
}
	
	
	public void draw(Canvas c) {
		// TODO Auto-generated method stub
		c.drawBitmap(enemyBitmap, startX, startY, null);
	}
}