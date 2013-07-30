package com.klabcyscorpions.destructiontd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

import com.example.destructiontd.R;



public class GameThread extends Thread {
	public static final long SPAWN_ENEMY = 1000; // spawn enemy every 1 seconds
	
	public static final int SPAWN_TOP = 0;
	public static final int SPAWN_BOTTOM = 1;
	public static final int SPAWN_LEFT = 2;
	public static final int SPAWN_RIGHT = 3;
	
	private final Point topSpawnArea, bottomSpawnArea,
					leftSpawnArea, rightSpawnArea;

	/** Handle to the surface manager object we interact with */
	private static SurfaceHolder mSurfaceHolder;

	public DisplayMetrics metrics = new DisplayMetrics();

	/** The drawable to use as the background of the animation canvas */
	private static Bitmap mBackgroundImage;

	/** The drawable to use as the tower */
	public static Bitmap towerImg;

	/** The drawable to use as the gunbarrel */
	public static Bitmap bulletImg;
	
	/** The drawable to use as the enemy */
	public static Bitmap enemyImg;

	/** Message handler used by thread to interact with TextView */
	private static Handler mHandler;

	private static Context mContext;

	private Object inputProccessorMutex = new Object();

	private long spawnEnemyTimer;

	/** Holds the input event*/
	Input input;


	/** Indicate whether the surface has been created & is ready to draw */
	public static boolean mRun = false;
	private HUD hud;
	private Enemy enemy;
	private static Tower tower;
	private static ArrayList<Bullet> bullets;
	private static ArrayList<Enemy> enemies;
	
	private long lastUpdateTime;

	public static Resources res;
	public static DisplayMetrics displayMetrics;
	private static int screenWidth, screenHeight;

	public GameThread(SurfaceHolder surfaceHolder, Context context,
			Handler handler) {
		// get handles to some important objects
		mSurfaceHolder = surfaceHolder;
		mContext = context;
		lastUpdateTime = 0;
		Resources res = context.getResources();

		// we don't need to transform it and it's faster to draw this way
		mBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.background_intersection);

		towerImg = BitmapFactory.decodeResource(res, R.drawable.tower_sprite);
//		bulletImg = BitmapFactory.decodeResource(res, R.drawable.bullet);
		enemyImg = BitmapFactory.decodeResource(res, R.drawable.enemy);
		tower = new Tower(context, towerImg);
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		input = new Input();
		
//		SoundManager.loadSound(mContext);
		topSpawnArea = new Point(375, -10);
		bottomSpawnArea = new Point(375, 450);
		leftSpawnArea = new Point(0, 210);
		rightSpawnArea = new Point(770, 210);
//		topSpawnArea = new Point(GameThread.getScreenHeight(), GameThread.getScreenWidth() / 2 ) ;
//		bottomSpawnArea = new Point((GameThread.getScreenHeight() / 2), (GameThread.getScreenWidth()));
//		leftSpawnArea = new Point(0, (GameThread.getScreenWidth() / 2));
//		rightSpawnArea = new Point(GameThread.getScreenHeight(), (GameThread.getScreenWidth() / 2));
		
		
	}
	
	/**
	 * Starts the game
	 */
	public void doStart() {
		synchronized (mSurfaceHolder) {
		}
	}

	/**
	 * Resumes from a pause.
	 */
	public void unpause() {
		// Move the real time clock up to now
		synchronized (mSurfaceHolder) {
			lastUpdateTime = System.currentTimeMillis() + 100;
		}

	}

	@Override
	public void run() {


		while (mRun) {
			Canvas c = null;
			try {
				c = mSurfaceHolder.lockCanvas(null);
				synchronized (mSurfaceHolder) {
					long currentTime = System.currentTimeMillis();
					long delta = (long) (currentTime - lastUpdateTime);
					lastUpdateTime = currentTime;                
					processInput();
					updatePhysics(delta);
//					SoundManager.update(delta);
					doDraw(c);
				}
			} finally {
				// do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}



	/**
	 * Used to signal the thread whether it should be running or not.
	 * Passing true allows the thread to run; passing false will shut it
	 * down if it's already running. Calling start() after this was most
	 * recently called with false will result in an immediate shutdown.
	 * 
	 * @param b true to run, false to shut down
	 */
	public void setRunning(boolean b) {
		mRun = b;
	}




	/**
	 * Draws the tank, bullets, planes, and background to the provided
	 * Canvas.
	 */
	private void doDraw(Canvas canvas) {

		//draw some background 


		canvas.drawBitmap(mBackgroundImage, 0, 0, null);
		

		//draw the bullets
		for(Iterator it = bullets.iterator();it.hasNext();){
			Bullet b = (Bullet) it.next();
			if (b!=null)
				b.draw(canvas);
		}
		//draw enemies
		for(Iterator it = enemies.iterator();it.hasNext();){
			Enemy e = (Enemy) it.next();
			if (e!=null)
				e.draw(canvas);
		}


		//Draw the tower
		tower.draw(canvas);
		hud.draw(canvas);
//		tower.invalidate();

	}

	private void updatePhysics(long delta) {
		delta=(long) (delta); //adjust the overall speed
		int i=0, j=0;
		while (!enemies.isEmpty() && enemies.size()>i){
			j=0;
			while (!bullets.isEmpty() && bullets.size()>j){
				if(enemies.get(i).isColliding(bullets.get(j).getBounds())){
					Log.v("collided", "Collided!");
					hud.scoreIncreased();
					enemies.get(i).setRelease(true);
					bullets.get(j).setRelease(true);
				} 
//				if (enemies.get(i).enemyRight >= bullets.get(i).bulletLeft && enemies.get(i).enemyLeft <= bullets.get(i).bulletRight && enemies.get(i).enemyBottom >= bullets.get(i).bulletTop && enemies.get(i).enemyTop <= bullets.get(i).bulletBottom){
//					 Log.v("collided", "Collided!");
//						enemies.get(i).setRelease(true);
//						bullets.get(j).setRelease(true);
//				if((enemies.get(i).enemyRight >= bullets.get(j).bulletLeft || enemies.get(i).enemyLeft <= bullets.get(j).bulletRight) && (enemies.get(i).enemyBottom >= bullets.get(j).bulletTop|| enemies.get(i).enemyTop <= bullets.get(j).bulletBottom)){
//					 Log.v("collided", "Collided!");
//						enemies.get(i).setRelease(true);
//						bullets.get(j).setRelease(true);
//				} 
				j++;
			}
			i++;
		}
		
		
		//draw the bullets
		i=0;
		Bullet b;
		while (!bullets.isEmpty() && bullets.size()>i){
			b = bullets.get(i);
			if (b.isReleaseBullet())
				bullets.remove(i);
			else {
				b.update(delta);
				i++;
			}
		}
		
		i=0;
		j=0;
		Enemy e;
		while (!enemies.isEmpty() && enemies.size()>i){
			e = enemies.get(i);
			if(Enemy.isTowerDamaged()){
				HUD.towerHp();
				int p = HUD.towerHpValue();
				if(p <= 0){
					isGameOver();
				}
			}
			if (e.isReleaseEnemy()){
				enemies.remove(i);				
			} 
			
			else {
				e.update(delta);				
				i++;
			}
		}
		
		spawnEnemyTimer += delta;
		if(spawnEnemyTimer > SPAWN_ENEMY){
			int spawnAt = (int) (Math.random()*4);
			
			switch(spawnAt){
			case SPAWN_TOP:
				enemies.add(new Enemy(mContext, SPAWN_TOP, topSpawnArea.x, topSpawnArea.y));
				break;
			case SPAWN_BOTTOM:
				enemies.add(new Enemy(mContext, SPAWN_BOTTOM, bottomSpawnArea.x, bottomSpawnArea.y));
				break;
			case SPAWN_LEFT:
				enemies.add(new Enemy(mContext, SPAWN_LEFT, leftSpawnArea.x, leftSpawnArea.y));
				break;
			case SPAWN_RIGHT:
				enemies.add(new Enemy(mContext, SPAWN_RIGHT, rightSpawnArea.x, rightSpawnArea.y));
				break;
			}
			spawnEnemyTimer = 0;
		}
			
	}
	
	public List<Bullet> getBullets(){
		return bullets;
	}
	public List<Enemy> getEnemies(){
		return enemies;
	}

	public void feedInput(int eventAction, float x, float y) {
		synchronized(inputProccessorMutex) {
			input.eventAction = eventAction;
			input.x = x;
			input.y = y;
		}
	}

	private void processInput() {
		synchronized(inputProccessorMutex) {
			if (input.eventAction == MotionEvent.ACTION_DOWN) {
				// TODO Add bullet
				Bullet b = new Bullet(mContext, tower.getTowerX() + tower.getTowerWidth()/2,
												tower.getTowerY() + tower.getTowerHeight()/2,
												input.x, input.y);
				bullets.add(b);
				Log.v("here", "xTAP "+ input.x + "  yTAP " + input.y);
			} else if (input.eventAction == MotionEvent.ACTION_MOVE) {
				tower.rotateTower(input.x,input.y);
				Log.v("teka", "x "+ input.x + "  y " + input.y);
				//tower.rotateTower(tower.getTowerX(), tower.getTowerY());				
			}
		}
	}
		

	public void pause() {
		// TODO Auto-generated method stub


	}
	
	public void setDisplayMetrics(DisplayMetrics m) {
		// TODO Auto-generated method stub
		displayMetrics = m;
		mBackgroundImage=mBackgroundImage.createScaledBitmap(mBackgroundImage,
										displayMetrics.widthPixels, displayMetrics.heightPixels, true);
		
		screenWidth = displayMetrics.widthPixels;
		screenHeight = displayMetrics.heightPixels;
		
		tower.setPosition(screenWidth/2 - tower.getTowerWidth()/2,
				screenHeight/2 - tower.getTowerHeight()/2);
		hud = new HUD(m);
	}
	

	public static int getScreenWidth() {
		return screenWidth;
		
	}
	public static int getScreenHeight() {
		Log.v("tina", "ScreenHeight: " + screenHeight);
		return screenHeight;
	}
	
	public static float towerPositionX(){
		return tower.getTowerX();
	}
	public static float towerPositionY(){
		return tower.getTowerY();
	}
	

	private class Input{
		int eventAction;
		float x, y;
	}
	
	public static void isGameOver() {
		Intent i = new Intent(mContext, GameOver.class);
		GameThread p = new GameThread(mSurfaceHolder, mContext, mHandler);
	//	p.join();
		((Activity)mContext).startActivity(i);
	}
	
	
}
