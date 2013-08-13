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
	
	private static  Point topSpawnArea, bottomSpawnArea,
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

		towerImg = BitmapFactory.decodeResource(res, R.drawable.tower_cannon);
//		bulletImg = BitmapFactory.decodeResource(res, R.drawable.bullet);
		enemyImg = BitmapFactory.decodeResource(res, R.drawable.enemy);
		tower = new Tower(context, towerImg);
		bullets = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		input = new Input();
		
//		SoundManager.loadSound(mContext);
	
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
					if(c != null){
					doDraw(c);
					} 
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




	public void setRunning(boolean b) {
		mRun = b;
	}





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
				Log.v("screens", "LOG LANG NGA EH");
				Bullet b = new Bullet(mContext, tower.getTowerX() + tower.getTowerWidth()/2,
												tower.getTowerY() + tower.getTowerHeight()/2,
												input.x, input.y);
				bullets.add(b);
			} /*else if (input.eventAction == MotionEvent.ACTION_MOVE) {
				tower.rotateTower(input.x,input.y);	*/		
				else if (input.eventAction == MotionEvent.ACTION_MOVE) {
				float x = input.x;
				float y = input.y;
				//Tower.updateTower((getTheta(x, y) + 90) % 360);
				tower.updateTower((getTheta(x,y)+90)% 360);
		}
	}
}

	private static float getTheta(float x, float y) {
		// TODO Auto-generated method stub
		float sx = x - (tower.getTowerWidth() / 2.0f);
		float sy = y - (tower.getTowerHeight() / 2.0f);

		float length = (float) Math.sqrt(sx * sx + sy * sy);
		float nx = sx / length;
		float ny = sy / length;
		float theta = (float) Math.atan2(ny, nx);

		final float rad2deg = (float) (180.0 / Math.PI);
		float theta2 = theta * rad2deg;

		return (theta2 < 0) ? theta2 + 360.0f : theta2;

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
		topSpawnArea = new Point(((screenWidth/2)-10), 0);
		bottomSpawnArea = new Point(((screenWidth/2)-10), screenHeight);
		leftSpawnArea = new Point(0, ((screenHeight/2)-10));
		rightSpawnArea = new Point(screenWidth, ((screenHeight/2)-10));
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
		int eventAction = -1;
		float x, y;
	}

	public static void isGameOver() {
		/*GameThread p = new GameThread(mSurfaceHolder, mContext, mHandler){
		@Override
    public void run() {*/
		Intent i = new Intent(mContext, GameOver.class);	
		((Activity)mContext).startActivity(i);
	/*}
		};*/
	}
}
