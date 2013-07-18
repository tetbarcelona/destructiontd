package com.klabcyscorpions.destructiontd;

import com.example.destructiontd.R;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;



public class TowerAnimation extends Activity {
	private TankView tankView;
	@Override
	public void onCreate(Bundle cannon) { 
        super.onCreate(cannon); 
        setContentView(R.layout.tower_animation);
        tankView = (TankView) findViewById(R.id.tower_image);
        tankView.setTankListener(new TankView.TankListener() {
					@Override
					public void onTankChanged(float delta, float angle) {
						if (delta > 0) {
							// forward Media Player 1
							
						} else {
							// rewind MediaPlayer1
						
						}
					}
				});
//        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), 
//               R.drawable.tower_sprite); 
//        
//        // make a Drawable from Bitmap to allow to set the BitMap 
//        // to the ImageView, ImageButton or what ever 
//        BitmapDrawable bmd = new BitmapDrawable(bitmapOrg); 
//        
//        ImageView imageView = (ImageView) findViewById(R.id.tower_image); 
//        
//        // set the Drawable on the ImageView 
//        imageView.setImageDrawable(bmd); 
//      
//        // center the Image 
//        imageView.setScaleType(ScaleType.CENTER); 
//        
//        animate(imageView);
    } 

//	private void animate(ImageView view){
//		
//		Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
//		view.startAnimation(rotate);
//	}
}
