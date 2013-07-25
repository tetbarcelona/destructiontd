package com.klabcyscorpions.destructiontd;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.example.destructiontd.R;

public class DTDGameActivity extends Activity {
	
	/** A handle for the thread running the game main loop */

	private GameThread mGameThread;

	/**A handle to the View containing the game*/
	private GameView mGameView;
	
	public DisplayMetrics metrics = new DisplayMetrics();

	@Override
	public void onCreate(Bundle cannon) {
		super.onCreate(cannon);
		// tell system to use the layout defined in our XML file
		setContentView(R.layout.main_game);

		// get handles to the GameView from XML, and the GameThread
		mGameView = (GameView) findViewById(R.id.game);

		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		mGameThread = mGameView.getThread();
		mGameThread.setDisplayMetrics(metrics);
		
		findViewById(R.id.holder).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				mGameThread.feedInput(event.getAction(), event.getX(0), event.getY(0));
				return true;
			}
		});
	}
	
	

}