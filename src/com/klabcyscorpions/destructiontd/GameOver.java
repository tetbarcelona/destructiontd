package com.klabcyscorpions.destructiontd;

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
import android.widget.TextView;

import com.example.destructiontd.R;

public class GameOver extends Activity {

	private HUD hud;
	TextView scoreText;
	Button back;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
		scoreText = new TextView(this); 
	    scoreText = (TextView)findViewById(R.id.game_over_score_value); 
	    scoreText.setText(HUD.scoreValue() + "");
	    context = getApplicationContext();
	    back = new Button(this);	
	    back = (Button)findViewById(R.id.back_button);
	    back.setOnTouchListener(new OnTouchListener() 
	   
	    {
	    				
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if (event.getAction() == MotionEvent.ACTION_DOWN ) {
					Log.v("gameover", "Clicked!");
					Intent i = new Intent(context, MainActivity.class);
					startActivity(i);
                    return true;
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
