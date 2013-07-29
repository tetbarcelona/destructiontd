package com.klabcyscorpions.destructiontd;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.util.DisplayMetrics;
import android.util.Log;

public class HUD implements Renderable {

	private static final int TEXT_COUNTER_TOP_MARGIN = 15;
	private static final int TEXT_COUNTER_TOP_MARGIN2 = 35;
	private static final int TEXT_INFO_LEFT_MARGIN = 10;
	private static final int TEXT_SIZE = 15;

	private int towerAttacked;
	private int Score;

	LinearGradient gradient;

	float scale;

	DisplayMetrics dm;

	Paint paint;


	public HUD(DisplayMetrics d){
		paint = new Paint();
		dm=d;

		scale = (float) dm.densityDpi/160;

		
		towerAttacked = 10;
		Score = 0;
}

	public void draw(Canvas c){
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(0);
		paint.setAntiAlias(true);
		paint.setTextSize(TEXT_SIZE*scale);
		c.drawText("Tower Hp: "+ towerAttacked, TEXT_INFO_LEFT_MARGIN * scale, TEXT_COUNTER_TOP_MARGIN * scale, paint);
		c.drawText("Score: "+ Score, TEXT_INFO_LEFT_MARGIN * scale, TEXT_COUNTER_TOP_MARGIN2 * scale, paint);

	}

	public void update(long elapsedTime) {



	}

	public void towerAttacked() {
		towerAttacked--;
	}
	public void scoreIncreased() {
		Score+=100;
	}


}
