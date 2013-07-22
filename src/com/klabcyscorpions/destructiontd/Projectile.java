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
import android.widget.RelativeLayout;

import com.example.destructiontd.R;

public class Projectile extends ImageView {

	private int launchPoint;
	private float x, y;
	private Bitmap bulletBitmap;
	private Paint paint;
	private Rect pos;
	
	public enum TowerDirection {
		NORTH, SOUTH, EAST, WEST
	};

	private float angle = 0f;
	private float theta_old = 0f;

	private ProjectileListener listener;
	
	public Projectile(Context context) {
		this(context, null);
	}

	public Projectile(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		
	}

	public Projectile(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		bulletBitmap = BitmapFactory.decodeResource(context.getResources(),
                									R.drawable.bullet);
	}

	public interface ProjectileListener {
		public void onProjectileChanged(float delta, float angle);
	}

	public void setProjectileListener(ProjectileListener l) {
		listener = l;
	}

	public void setProjectileDirection(int x, int y, int size){
		pos = new Rect(x, y, size, size);
		invalidate();
	}

	protected void onDraw(Canvas c) {
		Log.v("tet","bulletbitmap " + bulletBitmap + " pos " + pos + " paint " + paint);
		c.drawBitmap(bulletBitmap, pos, pos, paint);
		super.onDraw(c);
		Log.v("teka", "It is drawn!");
	}
}