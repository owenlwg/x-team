package com.xteam.war3.activity;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.xteam.war3.R;
import com.xteam.war3.utils.TextUtils;
import com.xteam.war3.utils.YoukuUrlUtils;

public class MediaPlayActivity extends Activity implements SurfaceHolder.Callback, 
				MediaPlayer.OnPreparedListener, OnErrorListener, OnCompletionListener {
	
	private static final int WHAT_DELAY = 10;
	
	private int index;
	private Context mContext;
	private MediaPlayer mediaPlayer;
	private SurfaceHolder surfaceHolder;
	private ProgressDialog mProgressDialog;
	private VideoView videoView;
	private LinearLayout linearLayout;
	private ArrayList<String> UrlList;
	private Message message;
	private MediaController mediaController;
	private TextView mCurrentTv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.play_video_mediaplayer);
		
		Intent intent = getIntent();
		if (intent != null) {
			index = intent.getIntExtra("index", 0);
		}

		mediaController = new MediaController(this);
		message = mHandler.obtainMessage(WHAT_DELAY);
//		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
//		surfaceHolder = surfaceView.getHolder();
//		surfaceHolder.addCallback(this);
//		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		linearLayout = (LinearLayout) findViewById(R.id.ll_top);
		videoView = (VideoView) findViewById(R.id.videoView);
		videoView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (!mediaController.isShowing()) {
					linearLayout.setVisibility(View.VISIBLE);
				} else {
					linearLayout.setVisibility(View.GONE);
				}
				mHandler.removeMessages(WHAT_DELAY);
				message = mHandler.obtainMessage(WHAT_DELAY);
				mHandler.sendMessageDelayed(message, 3000);
				return false;
			}
		});
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
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DELAY:
				if (linearLayout.getVisibility() == View.VISIBLE) {
					linearLayout.setVisibility(View.INVISIBLE);
				}
				return;
			default:
				break;
			}
		}
		
	};
	
	private void initButton() {
		linearLayout.removeAllViews();
		if (UrlList != null && UrlList.size() > 0) {
			LayoutParams lp = new LayoutParams(48, 38);
			lp.setMargins(20, 0, 0, 0);
			for (int i = 0; i < UrlList.size(); i++) {
				TextView tv = new TextView(this);
				tv.setLayoutParams(lp);
				tv.setGravity(Gravity.CENTER);
				tv.setId(i);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundResource(R.drawable.round_icon_selector);
				tv.setText("" + (i + 1));
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View view) {
						if (mCurrentTv.getId() == view.getId()) {
							return;
						}
						mCurrentTv.setBackgroundResource(R.drawable.round_icon_normal);
						mCurrentTv = (TextView) view;
						view.setBackgroundResource(R.drawable.round_icon_select);
						playVideo2(view.getId());
					}
				});
				
				if (i == 0) {
					mCurrentTv = tv;
					mCurrentTv.setBackgroundResource(R.drawable.round_icon_select);
				}
				
				linearLayout.addView(tv, i);
			}
			
			playVideo2(0);
		} else {
			finish();
		}
	}
	
	
	private void playVideo2(int index) {
		if (index < UrlList.size()) {
			Log.e("owen", "playVideo url: " + UrlList.get(index));
			videoView.setVideoURI(Uri.parse(UrlList.get(index))); 
			videoView.setOnPreparedListener(this);
			videoView.setMediaController(mediaController);
			videoView.setOnCompletionListener(this);
			videoView.setOnErrorListener(this);
		}
	}
	
	@Override
	public void onCompletion(MediaPlayer mp) {
		if (mCurrentTv.getId() < (UrlList.size() - 1)) {
			linearLayout.getChildAt((mCurrentTv.getId() + 1)).performClick();
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Toast.makeText(mContext, R.string.can_not_play, Toast.LENGTH_LONG).show();
		finish();
		return true;
	}
	
    class PlayVideoAsyn extends AsyncTask<Void, Void, Void> {
    	
		@Override
		protected void onPreExecute() {
			mProgressDialog = ProgressDialog.show(mContext, "", getString(R.string.please_wait));
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				UrlList = YoukuUrlUtils.getYoukuRealUrl(TextUtils.getInstance(mContext).getGameUrl(index));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			initButton();
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
	
	@Override
	public void onPrepared(MediaPlayer mp) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		mp.start();
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
