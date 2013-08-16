package com.xteam.war3.activity;

import android.app.Application;
import android.graphics.Typeface;

public class XApplication extends Application {

	private static Typeface boldTypeface;
	private static Typeface normalTypeface;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		boldTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		normalTypeface = Typeface.createFromAsset(getAssets(), "FT-Regular.ttf");
		
	}

	public Typeface getBoldTypeface() {
		return boldTypeface;
	}

	public Typeface getNormalTypeface() {
		return normalTypeface;
	}
	
}
