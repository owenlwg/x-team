package com.xteam.war3.application;


import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;

public class XApplication extends Application {

	private static Typeface boldTypeface;
	private static Typeface normalTypeface;
	private int maxMemory;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
		
		boldTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		normalTypeface = Typeface.createFromAsset(getAssets(), "FT-Regular.ttf");
		
		maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	}

	
	public int getMaxMemory() {
		return maxMemory;
	}

	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}

	public Typeface getBoldTypeface() {
		return boldTypeface;
	}

	public Typeface getNormalTypeface() {
		return normalTypeface;
	}
	
}
