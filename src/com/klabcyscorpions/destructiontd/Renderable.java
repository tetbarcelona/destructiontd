package com.klabcyscorpions.destructiontd;

import android.graphics.Canvas;

public interface Renderable {
	public void draw(Canvas c);

	public void update(long elapsedTime);

}
