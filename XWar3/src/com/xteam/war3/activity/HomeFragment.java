package com.xteam.war3.activity;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.xteam.war3.R;
import com.xteam.war3.application.XApplication;

public class HomeFragment extends Fragment {

//	private TextView mTvTitle;
	private TextView mTvDes;
	private int width;
	private XApplication xApplication;
	private AdView adView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		xApplication = (XApplication) getActivity().getApplication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		width = getWidth();
		
		View rootView = inflater.inflate(R.layout.home, container, false);
		
//		mTvTitle = (TextView) rootView.findViewById(R.id.title);
		mTvDes = (TextView) rootView.findViewById(R.id.description);
		adView = (AdView) rootView.findViewById(R.id.adView);
		mTvDes.setMovementMethod(new ScrollingMovementMethod());
//		mTvDes.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
//		mTvTitle.setTypeface(xApplication.getBoldTypeface());
//		TextPaint tp = mTvTitle.getPaint(); 
//		tp.setFakeBoldText(true);
		mTvDes.setTypeface(xApplication.getNormalTypeface());
		adView.setListener(new AdViewListener() {

			public void onAdSwitch() {
				Log.w("owen", "onAdSwitch");
			}

			public void onAdShow(JSONObject info) {
				Log.w("owen", "onAdShow " + info.toString());
			}

			public void onAdReady(AdView adView) {
				Log.w("owen", "onAdReady " + adView);
				adView.setVisibility(View.VISIBLE);
			}

			public void onAdFailed(String reason) {
				Log.w("owen", "onAdFailed " + reason);
			}

			public void onAdClick(JSONObject info) {
				Log.w("owen", "onAdClick " + info.toString());
			}

			public void onVideoStart() {}

			public void onVideoFinish() {}
			
			@Override
			public void onVideoClickAd() {}

			@Override
			public void onVideoClickClose() {}

			@Override
			public void onVideoClickReplay() {}

			@Override
			public void onVideoError() {}
		});
//		mTvTitle.setShadowLayer(3F, -1F, 1F, Color.BLACK);
		
//		addImageView(getActivity(), rootView);
		
		return rootView;
	}
	
	private int getWidth() {
	    DisplayMetrics metrics = new DisplayMetrics();
	    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    return metrics.widthPixels;
	}
	
	private void addImageView(Context context, View rootView) {
		LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.head_linerlayout);
		LayoutParams lp = new LayoutParams((width/4 - 6), LayoutParams.MATCH_PARENT);
		lp.leftMargin = 3;
		lp.rightMargin = 3;
		for (int i = 0; i < 4; i++) {
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(lp);
			int resId = getResources().getIdentifier("home" + (i + 1), "drawable", getActivity().getPackageName());
			imageView.setImageResource(resId);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			
			layout.addView(imageView);
		}
	}

}
