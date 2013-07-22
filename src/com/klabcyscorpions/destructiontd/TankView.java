package com.klabcyscorpions.destructiontd;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.example.destructiontd.R;

/**
 * Based on :
 * http://go-lambda.blogspot.fr/2012/02/rotary-knob-widget-on-android.htm...
 */
public class TankView extends ImageView {

	private float angle = 0f;
	private float theta_old = 0f;
	private int width;
	private int height;

	private TankListener listener;

	public interface TankListener {
		public void onTankChanged(float delta, float angle);
	}

	public void setTankListener(TankListener l) {
		listener = l;
	}

	public TankView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TankView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public TankView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		initialize();
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

	public void initialize() {

		this.setImageResource(R.drawable.tower_sprite);
		setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int action = event.getAction();
				int actionCode = action & MotionEvent.ACTION_MASK;
				if (actionCode == MotionEvent.ACTION_POINTER_DOWN) {
					float x = event.getX(0);
					float y = event.getY(0);
					theta_old = getTheta(x, y);
				} else if (actionCode == MotionEvent.ACTION_MOVE) {
					invalidate();

					float x = event.getX(0);
					float y = event.getY(0);

					float theta = getTheta(x, y);
					float delta_theta = theta - theta_old;

					theta_old = theta;
					angle = theta - 270;
					notifyListener(delta_theta, (theta + 90) % 360);
				}
				return true;
			}

		});
	}

	private void notifyListener(float delta, float angle) {
		if (null != listener) {
			listener.onTankChanged(delta, angle);
		}
	}
	
	public void updateTank(float angle){
		this.angle = angle;
		invalidate();
	}

	protected void onDraw(Canvas c) {
		c.rotate(angle, width / 2, height / 2);
		super.onDraw(c);
	}

}