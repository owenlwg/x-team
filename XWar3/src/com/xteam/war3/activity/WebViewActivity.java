package com.xteam.war3.activity;

import java.io.IOException;
import java.lang.reflect.Method;

import org.json.JSONException;

import com.xteam.war3.R;
import com.xteam.war3.utils.TextUtils;
import com.xteam.war3.utils.YoukuUrlUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;

public class WebViewActivity extends Activity {
	private WebView mWebView;
	private String gameUrl;
	private int index;
	private Context mContext;
	private MediaPlayer mediaPlayer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.play_video_webview);
		mWebView = (WebView) findViewById(R.id.webView);
		
		Intent intent = getIntent();
		if (intent != null) {
			index = intent.getIntExtra("index", 0);
		}

	}
    
    @Override
	protected void onResume() {
    	super.onResume();
    	new PlayVideoAsyn().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		callHiddenWebViewMethod("onPause");
	}

    private void callHiddenWebViewMethod(String name){
        if( mWebView != null ){
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(mWebView);
            } catch (Exception e) {
            	e.printStackTrace();
            	mWebView.loadData("", "text/html", "utf-8");
            } 
        }
    }
    
    class PlayVideoAsyn extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			playVideo();
		}
		
    }

	private void playVideo() {
		int wi = (int) (mWebView.getWidth());
		int hi = (int) (mWebView.getHeight());
		String widthAndHeight = "width='" + wi + "' height='" + hi + "'";
		
//		videoURL = "http://player.youku.com/player.php/sid/XNTM5MjU3NDI0/v.swf";
		String videoURL = "http://player.youku.com/embed/XNzE0NzEyNA==";
		String temp = "<div><object "
				+ widthAndHeight
				+ ">"
				+ "<param name='allowFullScreen' value='true'>"
				+ "</param><param name='allowscriptaccess' value='always'>"
				+ "</param><embed src='"
				+ videoURL
				+ "'"
				+ " type='application/x-shockwave-flash' allowscriptaccess='always'"
				+ "allowfullscreen='true'" + widthAndHeight
				+ "></embed></object></div>";
		mWebView.setBackgroundColor(Color.BLACK);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
//		mWebView.loadData(temp, "text/html", "utf-8");
//		mWebView.loadDataWithBaseURL(null, temp, "text/html", "utf-8", null);
		mWebView.loadUrl(videoURL);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			public void onPageFinished(WebView view, String url) {

			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {

			}
		}); 
    }
}
