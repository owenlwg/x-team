package com.xteam.war3.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button mButton;
	private TextView mTvTitle;
	private LinearLayout mLinearLayout;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.main);
		
		mTvTitle = (TextView) findViewById(R.id.title);
		mTvTitle.setShadowLayer(10F, -5F, 6F, Color.BLACK);
		
		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, ScreenSlideActivity.class));
			}
		});
		
		mLinearLayout = (LinearLayout) findViewById(R.id.head_linerlayout);
		addImageView();
		
		 
	}
	
	private void addImageView() {
	    DisplayMetrics metrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    int width = metrics.widthPixels;
		
		for (int i = 0; i < 10; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams lp = new LayoutParams(width/3, LayoutParams.MATCH_PARENT);
			imageView.setLayoutParams(lp);
			imageView.setImageResource(R.drawable.sky);
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setBackgroundResource(R.drawable.tran_frame);
			
			mLinearLayout.addView(imageView);
		}
	}
	

}
