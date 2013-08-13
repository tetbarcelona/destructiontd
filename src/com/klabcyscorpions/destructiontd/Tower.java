package com.klabcyscorpions.destructiontd;

import com.example.destructiontd.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Based on :
 * http://go-lambda.blogspot.fr/2012/02/rotary-knob-widget-on-android.htm...
 */
public class Tower{

	private float angle = 0f;
	private float theta_old = 0f;
	private static int width;
	private static int height;
	private float x, y;
	private float theta = 0f; 
	private TankListener listener;
	private Bitmap towerBitmap;
	private Matrix matrix;
	private float towerPosition;

	public interface TankListener {
		public void onTankChanged(float delta, float angle);
	}

	public void setTankListener(TankListener l) {
		listener = l;
	}

	public Tower(Context context, Bitmap towerBitmap) {
		this.towerBitmap = towerBitmap;
		width = towerBitmap.getWidth();
		height = towerBitmap.getHeight();
		matrix = new Matrix();
	}
	
	private static float getTheta(float x, float y) {
		
		float sx = x - (width / 2.0f);
		float sy = y - (height / 2.0f);

		float length = (float) Math.sqrt(sx * sx + sy * sy);
		float nx = sx / length;
		float ny = sy / length;
		float theta = (float) Math.atan2(ny, nx);

		final float rad2deg = (float) (180.0 / Math.PI);
		float theta2 = theta * rad2deg;

		return (theta2 < 0) ? theta2 + 360.0f : theta2;

	}

	public void setPosition(float x, float y){
		this.x = x;
		this.y = y;
		Log.d("teka" , "towerPosition X: " + x + " Y: " + y);
	}
	
	public float getTowerX(){
		return x;
	}
	public float getTowerY(){
		return y;
	}

	public int getTowerWidth(){
		return width;
	}
	
	public int getTowerHeight(){
		return height;
	}

	
	public Bitmap getBitmap(){
		return towerBitmap;
	}
	
	private void notifyListener(float delta, float angle) {
		if (null != listener) {
			listener.onTankChanged(delta, angle);
		}
	}
	
	/*public void rotateTower(float x , float y){
		angle = (getTheta(x , y) + 90) % 360;
		Log.v("errors", "X: " + x + " Y: " + y);
	}*/

	public void draw(Canvas c) {
		matrix.reset();
		matrix.postRotate(angle, width/2, height/2);
		matrix.postTranslate(x, y);
		Log.v("here", "TranslateX : " + x + " TranslateY : " + y);
		c.drawBitmap(towerBitmap, matrix, null);
	}
	
	public void updateTower(float angle){
		angle = (getTheta(x ,y) +90) % 360;
	}
	
}