package com.xteam.war3.activity;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import org.json.JSONException;

import com.xteam.war3.utils.TextUtils;
import com.xteam.war3.utils.YoukuUrlUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.PluginState;
import android.widget.MediaController;
import android.widget.VideoView;

public class MediaPlayActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
	private String gameUrl;
	private int index;
	private Context mContext;
	private TextUtils textUtils;
	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private ProgressDialog mProgressDialog;
	private VideoView videoView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.play_video_mediaplayer);
		
		textUtils = new TextUtils(mContext);
		Intent intent = getIntent();
		if (intent != null) {
			index = intent.getIntExtra("index", 0);
		}

//		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//		surfaceHolder = surfaceView.getHolder();
//		surfaceHolder.addCallback(this);
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		videoView = (VideoView) findViewById(R.id.videoView);
		
	}
    
    @Override
	protected void onResume() {
    	super.onResume();
    	new PlayVideoAsyn().execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
    
    
	private void playVideo(String url) {
		Log.e("owen", "playVideo url: " + url);
		
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDisplay(surfaceHolder);
			mediaPlayer.setDataSource(url);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.prepare();
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void playVideo2(String url) {
		Log.e("owen", "playVideo url: " + url);
//		videoView.setVideoURI(Uri.parse(url + ".mp4")); 
		videoView.setVideoURI(Uri.parse(url + ".mp4")); 
		videoView.start();
		videoView.setMediaController(new MediaController(this));
		
	}
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mp.start();
	}

	
    class PlayVideoAsyn extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			String url = "";
			try {
				url = YoukuUrlUtils.getYoukuRealUrl(textUtils.getGameUrl(index));
				if (url != null) {
//					videoView.setVideoURI(Uri.parse(url + ".mp4")); 
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return url;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				playVideo2(result);
			}
		}
		
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		holder.setFixedSize(holder.getSurfaceFrame().height()*4/3, holder.getSurfaceFrame().height());
		mProgressDialog = ProgressDialog.show(this, "", "Please wait...");
		new PlayVideoAsyn().execute();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

}
