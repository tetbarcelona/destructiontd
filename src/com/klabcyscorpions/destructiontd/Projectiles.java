package com.klabcyscorpions.destructiontd;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

public class Projectiles {

	protected Drawable drawable;
	protected AnimationDrawable animationDrawable;
	protected int x, y;
	protected int speed; // in terms of pixel speed
	protected Tower tower;
	
	protected Context context;
	private Paint projectilePaint;

	
	public Projectiles( Tower tower, Context context) {
		
		this.tower = tower;
		this.context = context;
		
		projectilePaint = new Paint();
		projectilePaint.setAntiAlias(true);
		projectilePaint.setColor(Color.RED);
	}
	
	public void fireProjectiles() {
		x = tower.centerX;
		y = tower.centerY;
	}
	
	public void update(int tickCounter) {
	
		}
	
	
	public void draw(Canvas canvas) {
		
			canvas.drawCircle(x, y, 3, projectilePaint);
		}
	}

