package com.xteam.war3.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.xteam.war3.application.XApplication;
import com.xteam.war3.utils.TextUtils;

public class HomeFragment extends SherlockFragment {

	private TextView mTvTitle;
	private TextView mTvDes;
	private int width;
	private XApplication xApplication;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		xApplication = (XApplication) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		width = getWidth();
		
		View rootView = inflater.inflate(R.layout.home, container, false);
		
		mTvTitle = (TextView) rootView.findViewById(R.id.title);
		mTvDes = (TextView) rootView.findViewById(R.id.description);
		mTvDes.setMovementMethod(new ScrollingMovementMethod());
		
		mTvTitle.setTypeface(xApplication.getBoldTypeface());
		mTvDes.setTypeface(xApplication.getNormalTypeface());
//		mTvTitle.setShadowLayer(3F, -1F, 1F, Color.BLACK);
		
		addImageView(getActivity(), rootView);
		
		return rootView;
	}
	
	private int getWidth() {
	    DisplayMetrics metrics = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    return metrics.widthPixels;
	}
	
	private void addImageView(Context context, View rootView) {
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.head_linerlayout);
		
		for (int i = 0; i < 4; i++) {
			ImageView imageView = new ImageView(context);
			LayoutParams lp = new LayoutParams((width/4 - 6), LayoutParams.MATCH_PARENT);
			lp.leftMargin = 3;
			lp.rightMargin = 3;
			imageView.setLayoutParams(lp);
			imageView.setImageResource(R.drawable.owen);
			imageView.setScaleType(ScaleType.FIT_XY);
			
			layout.addView(imageView);
		}
	}

}
