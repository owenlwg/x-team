package com.xteam.war3.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;

public class WebViewActivity extends Activity {
	private WebView mWebView;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// FIXME OWEN Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
    
    
    private void setWebview() {

		Display display = getWindowManager().getDefaultDisplay();
		int mediaHi = 0;
		int wi = (int) (mWebView.getWidth());
		int hi = (int) (mWebView.getHeight()) - mediaHi;
		
		String widthAndHeight = "width='" + wi + "' height='" + hi + "'";
//		String videoURL = linkYouTube.replace("watch?v=", "v/")
//				+ "?fs=0&amp;hl=nl_NL&autoplay=1&rel=0&showinfo=0&autohide=1";
		
//		videoURL = "http://player.youku.com/player.php/sid/XNTM5MjU3NDI0/v.swf";
		String videoURL = "http://player.youku.com/player.php/Type/Folder/Fid/2148378/Ob/1/sid/XMzY5MDU4MjQ=/v.swf";
		String temp = "<div style='padding-top: "
				+ mediaHi
				+ "px'><object "
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
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.loadData(temp, "text/html", "utf-8");
//		mWebView.loadDataWithBaseURL(null, temp, "text/html", "utf-8", null);
//		mWebView.loadUrl("http://v.youku.com/v_show/id_XMzY5MDU4MjQ=.html?f=2148378");
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
