package com.klabcyscorpions.destructiontd;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import com.example.destructiontd.R;

public class TowerAnimation extends Activity {
	private TankView tankView;
	private float tempAngle;
	private int width;
	private int height;
	private float angle = 0f;
	private float theta_old = 0f;
	private List<Projectile> projectiles;
	private final Object lock = new Object();

//	
//	Ourview v;
//	Bitmap bullet;
	float x,y;
	Bitmap bulletBitmap;
	
	@Override
	public void onCreate(Bundle cannon) {
		super.onCreate(cannon);
		setContentView(R.layout.tower_animation);
		width = getWindowManager().getDefaultDisplay().getWidth();
		height = getWindowManager().getDefaultDisplay().getHeight();
		
		projectiles = new ArrayList<Projectile>();
		
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_holder);
		/*
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.v("tet", "temp angle " + tempAngle);
				
				
			}
		});
		*/
		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int actionCode = action & MotionEvent.ACTION_MASK;
				/*if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
					float x = event.getX(0);
					float y = event.getY(0);
					theta_old = getTheta(x, y);
				} else if (actionCode == MotionEvent.ACTION_MOVE) {
//					invalidate();

					float x = event.getX(0);
					float y = event.getY(0);

					float theta = getTheta(x, y);
					float delta_theta = theta - theta_old;

					theta_old = theta;
					angle = theta - 270;
					tankView.updateTank((theta + 90) % 360);
				}*/
				if (action == MotionEvent.ACTION_DOWN) {
					int x = (int) event.getX(0);
					int y = (int) event.getY(0);
					theta_old = getTheta(x, y);
//					synchronized (lock) {
						Projectile p = new Projectile(TowerAnimation.this);
						p.setProjectileDirection(x, y, 50);
						projectiles.add(p);
						Log.d("tet", "projectile Added");
						Canvas c = new Canvas();
						p.onDraw(c);
						
//					}
				} else if (actionCode == MotionEvent.ACTION_MOVE) {
					float x = event.getX(0);
					float y = event.getY(0);
					Log.d("tet", "x "+x+"     y "+y);
					tankView.updateTank((getTheta(x, y) + 90) % 360);
				}
				return true;
			}

		});

		tankView = (TankView) findViewById(R.id.tower_image);
		/*
		tankView.setTankListener(new TankView.TankListener() {
			@Override
			public void onTankChanged(float delta, float angle) {
				Log.v("tet", "angle " + angle);
				tempAngle = angle;
			}
		});
		*/

	}
	
	private float getTheta(float x, float y) {
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

}