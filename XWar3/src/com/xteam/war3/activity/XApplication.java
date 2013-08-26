package com.xteam.war3.activity;

import com.xteam.war3.application.CrashHandler;

import android.app.Application;
import android.graphics.Typeface;

public class XApplication extends Application {

	private static Typeface boldTypeface;
	private static Typeface normalTypeface;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
		
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
