package com.klabcyscorpions.destructiontd;


import android.content.Context;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	/** Handle to the application context, used to e.g. fetch Drawables. */
	private Context mContext;



	/** The thread that actually draws the animation */
	private static GameThread thread;

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);


		// create thread only; it's started in surfaceCreated()
		thread = new GameThread(holder, context,null);

		setFocusable(true); // make sure we get key events
		setFocusableInTouchMode(true); // make sure we get touch events

	}




	/**
	 * Fetches the animation thread corresponding to this GameView.
	 * 
	 * @return the animation thread
	 */
	public GameThread getThread() {
		return thread;
	}


	/**
	 * Standard window-focus override. Notice focus lost so we can pause on
	 * focus lost. e.g. user switches to take a call.
	 */
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if (!hasWindowFocus) thread.pause();
	}


	/*
	 * Callback invoked when the Surface has been created and is ready to be
	 * used.
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		// start the thread here so that we don't busy-wait in run()
		// waiting for the surface to be created
		thread.setRunning(true);
		thread.start();
	}

	/*
	 * Callback invoked when the Surface has been destroyed and must no longer
	 * be touched. WARNING: after this method returns, the Surface/Canvas must
	 * never be touched again!
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		// we have to tell thread to shut down & wait for it to finish, or else
		// it might touch the Surface after we return and explode

		thread.setRunning(false);
	    Log.v("gameover", "Im here!");
        try {
            thread.join();
        } 
        catch (InterruptedException e) {
        }
	}


	public void saveState(Bundle outState) {
		// TODO Auto-generated method stub

	}




	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		 
	}












}



