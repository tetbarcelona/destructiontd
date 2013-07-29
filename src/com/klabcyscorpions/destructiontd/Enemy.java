package com.klabcyscorpions.destructiontd;

import java.io.ObjectInputStream.GetField;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import com.example.destructiontd.R;

public class Enemy {
	static final float ENEMY_SPEED = 3f;
	private Bitmap enemyBitmap;
	private long stateTime = 0;
	private float startX, startY;
	boolean releaseEnemy;
	boolean releaseEnemy2;
	int spawnArea;
	float destinationX, destinationY;
	Rect bounds;
	private int enemyWidth;
	private int enemyHeight;
	int enemyLeft,enemyRight,enemyTop,enemyBottom;
	boolean towerDamaged;
	
	public Enemy(Context context, int spawnArea, float spawnX, float spawnY){
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		enemyBitmap = BitmapFactory.decodeResource(context.getResources(),
                									R.drawable.enemy);
		enemyWidth = enemyBitmap.getWidth();
        enemyHeight = enemyBitmap.getHeight();
//        enemyBitmap.recycle();
        
		startX = spawnX;
		startY = spawnY;
		this.spawnArea = spawnArea;
		destinationX = GameThread.getScreenWidth()/2;
		destinationY = GameThread.getScreenHeight()/2;
		bounds = new Rect((int)startX, (int)startY , (int)startX + enemyWidth, (int)startY + enemyHeight);
		
//		bounds = new Rect((int)startX, (int)startY , (int)startX + enemyWidth, (int)startY + enemyHeight);
		enemyLeft = (int)startX;
		enemyRight = (int)startX + enemyWidth;
		enemyTop = (int)startY;
		enemyBottom = (int)startY + enemyHeight;
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
				towerDamaged = true;
			}	
				break;
			case GameThread.SPAWN_RIGHT:
				startX -= ENEMY_SPEED;
				if((destinationX - Math.abs(startX) > 0))
				{
				releaseEnemy = true;
				towerDamaged = true;
			}
				break;
			case GameThread.SPAWN_TOP:
				startY += ENEMY_SPEED;
				if((destinationY - Math.abs(startY) < 30))
				{
				releaseEnemy = true;
				towerDamaged = true;
			}
				break;
			case GameThread.SPAWN_LEFT:
				startX += ENEMY_SPEED;
				if(destinationX - Math.abs(startX) < 10)
				{
				releaseEnemy = true;
				towerDamaged = true;
			}
				break;

			} stateTime -= 100;
			/*bounds.left = (int)startX;
			bounds.top = (int)startY + enemyHeight;
			bounds.right = (int)startX + enemyWidth;
			bounds.bottom = (int)startY;*/
			bounds.left = (int)startX;
			bounds.top = (int)startY ;
			bounds.right = (int)startX + enemyWidth;
			bounds.bottom = (int)startY + enemyHeight;
			enemyLeft = (int)startX;
			enemyRight =(int) startX + enemyWidth;
			enemyTop = (int)startY;
			enemyBottom =(int) startY + enemyHeight;
			Log.v("bounds", "update enemy: " + bounds);
		}
	stateTime -= 100;
}
	
	
	public void draw(Canvas c) {
		// TODO Auto-generated method stub
		c.drawBitmap(enemyBitmap, startX, startY, null);
	}
	public void setRelease(boolean release){
		releaseEnemy = release;
	}
	public boolean isColliding(Rect r){
		//return bounds.intersect(r);
//		Log.v("bounds", "enemy left: " + bounds.left + "; top: "+ bounds.top + "; right: " + bounds.right + "; bottom: " + bounds.bottom);
//		Log.v("bounds", "bullet left: " + r.left + "; top: "+ r.top + "; right: " + r.right + "; bottom: " + r.bottom);
//		return bounds.intersects((int)startX,(int) startY ,(int) startX + enemyWidth, (int) startY + enemyHeight);
		return bounds.contains(r);
//		return bounds.intersect(r);
		
	}
	

	public Rect getBounds(){
		Log.v("delta", "enemyBounds: " + bounds);
		return bounds;
	}
	public boolean isTowerDamaged(){
		if(towerDamaged == false){
			return false;
		} else
		return true;
	}
}