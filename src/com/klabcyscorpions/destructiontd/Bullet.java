package com.klabcyscorpions.destructiontd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.example.destructiontd.R;

public class Bullet{
	static final float BULLET_SPEED = 5;
	private float directionX, directionY;
	private Bitmap bulletBitmap;
	private long stateTime = 0;
	private float launchPointX, launchPointY;
	private float x, y;
	private float slope;
	float yIntercept;
	float mx;
	boolean releaseBullet;


	private ProjectileListener listener;


	public Bullet(Context context, float initialX, float initialY, float directionX, float directionY) {
		BitmapFactory.Options options = new BitmapFactory.Options();  
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		bulletBitmap = BitmapFactory.decodeResource(context.getResources(),
                									R.drawable.bullet);
		launchPointX = initialX;
		launchPointY = initialY;
		x = initialX;
		y = initialY;
		this.directionX = directionX;
		this.directionY = directionY;
		try{
			slope = (directionY - launchPointY) / (directionX - launchPointX);
		} catch(ArithmeticException a){
			slope = 0;
		}
	}

	public interface ProjectileListener {
		public void onProjectileChanged(float delta, float angle);
	}

	public void setProjectileListener(ProjectileListener l) {
		listener = l;
	}
	public float getSlope(){
		return slope;
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}
	public float getLaunchPointX(){
		return launchPointX;
	}
	public float getLaunchPointY(){
		return launchPointY;
	}
	
	public void update(float delta){
		stateTime += delta;
		if(stateTime < 100){
		
			mx = slope*x;
			
			yIntercept = y - mx;
					
			if(launchPointX < directionX)
				x += BULLET_SPEED;
			else if(launchPointX > directionX)
				x -= BULLET_SPEED;
			
			y = slope * x + yIntercept;
			stateTime -= 100;
			
			if(GameThread.getScreenWidth() - Math.abs(x) < 0){
				releaseBullet = true;
				Log.d("tet", "bullet release");
			}
		}
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bulletBitmap, x, y, null);
	}
	
	public boolean isReleaseBullet(){
		return releaseBullet;
	}

}