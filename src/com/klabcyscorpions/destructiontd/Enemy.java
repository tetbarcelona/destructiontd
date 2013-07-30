package com.klabcyscorpions.destructiontd;

import java.io.ObjectInputStream.GetField;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.widget.TextView;

import com.example.destructiontd.R;

public class Enemy {
	private HUD hud;
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
	public static boolean towerDamaged;

	
	public Enemy(Context context, int spawnArea, float spawnX, float spawnY){
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		enemyBitmap = BitmapFactory.decodeResource(context.getResources(),
                									R.drawable.enemy);
		enemyWidth = enemyBitmap.getWidth();
        enemyHeight = enemyBitmap.getHeight();

        
		startX = spawnX;
		startY = spawnY;
		this.spawnArea = spawnArea;
		destinationX = GameThread.getScreenWidth()/2;
		destinationY = GameThread.getScreenHeight()/2;
		bounds = new Rect((int)startX, (int)startY , (int)startX + enemyWidth, (int)startY + enemyHeight);
		
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
				towerDamaged = true;
				releaseEnemy = true;			
			}	else towerDamaged = false;
				break;
			case GameThread.SPAWN_RIGHT:
				startX -= ENEMY_SPEED;
				if((destinationX - Math.abs(startX) > 0))
				{
					towerDamaged = true;
					releaseEnemy = true;
			} else towerDamaged = false;
				break;
			case GameThread.SPAWN_TOP:
				startY += ENEMY_SPEED;
				if((destinationY - Math.abs(startY) < 30))
				{
					towerDamaged = true;
					releaseEnemy = true;
			} else towerDamaged = false;
				break;
			case GameThread.SPAWN_LEFT:
				startX += ENEMY_SPEED;
				if(destinationX - Math.abs(startX) < 10)
				{
					towerDamaged = true;
					releaseEnemy = true;
			} else towerDamaged = false;
				break;

			} stateTime -= 100;

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
	return bounds.contains(r);

	}
	
	public Rect getBounds(){
		Log.v("delta", "enemyBounds: " + bounds);
		return bounds;
	}
	public static boolean isTowerDamaged(){
		if(towerDamaged == false){
			Log.v("towerDamaged", "false");
			return false;
		} else 
			Log.v("towerDamaged", "true");
			return true;
	}
	
	
}