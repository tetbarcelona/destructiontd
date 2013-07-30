package com.klabcyscorpions.destructiontd;

import com.example.destructiontd.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class MainActivity extends Activity {
	Button exit;
	static Button play;
	private GameThread thread;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 	exit = new Button(this);	
		    exit = (Button)findViewById(R.id.button_exit);
		    exit.setOnTouchListener(new OnTouchListener() 
		   
		    {
		    				
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					
					if (event.getAction() == MotionEvent.ACTION_DOWN ) {
						Log.v("gameover", "Exit!");
						moveTaskToBack(true);
	                }

	                return false;
	            }
		       });
		    context = getApplicationContext();
		    play = new Button(this);	
		    play = (Button)findViewById(R.id.button_play);
		    play.setOnTouchListener(new OnTouchListener()   {
				
				
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		if (event.getAction() == MotionEvent.ACTION_DOWN ) {
			Log.v("gameover", "Play Again!");
			Intent i = new Intent(context, DTDGameActivity.class);
			startActivity(i);
        }

        return false;
    }
   });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
