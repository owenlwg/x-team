/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xteam.war3.activity;

import java.util.List;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.xteam.war3.application.XApplication;
import com.xteam.war3.utils.TextUtils;

public class WarSlideActivity extends SherlockFragmentActivity {
	private static final int NUM_PAGES = 10;
	private static final String ARG_PAGE = "ARG_PAGE";
	private ViewPager mPager;
	private PagerAdapter mPagerAdapter;
	private int initPosition = 0;
	private SpannableStringBuilder textSpan;
	private SpannableStringBuilder titleSpan;
	private TextUtils mTextUtils;
	private String[] mTitles;
	private String[] mDescriptions;
	private XApplication xApplication;
	private TextView mTvNumber;
	private ScrollingMovementMethod scrollingMovementMethod;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		xApplication = (XApplication) getApplication();

		setContentView(R.layout.war_slide);
		initPosition = getIntent().getIntExtra("initPosition", 0);

		mTextUtils = new TextUtils(this);
		textSpan = new SpannableStringBuilder();
		titleSpan = new SpannableStringBuilder();
		scrollingMovementMethod = new ScrollingMovementMethod();

		mTitles = getResources().getStringArray(R.array.game_title);
		mDescriptions = getResources().getStringArray(R.array.game_text);

		final ActionBar actionBar = getSupportActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);

		mPager = (ViewPager) findViewById(R.id.pager);
		mTvNumber = (TextView) findViewById(R.id.number);
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mTvNumber.setText((position + 1) + "/10");
				invalidateOptionsMenu();
			}
			
		});
		mPager.setCurrentItem(initPosition);
		mTvNumber.setText((initPosition + 1) + "/10");
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_screen_slide, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate "up" the demo structure to the launchpad activity.
			// See http://developer.android.com/design/patterns/navigation.html for more.
			NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
			return true;
		case R.id.menu_share:

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}

		@Override
		public Fragment getItem(int position) {
			ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_PAGE, position);
			fragment.setArguments(args);
			fragment.setRetainInstance(true);
			return fragment;
		}
		
	}

	public class ScreenSlidePageFragment extends Fragment {
		private int mPageNumber;
		private TextView mTvTitle;
		private TextView mTvDescription;
		private Button buttonPlay;
		private ImageView imageView;

		public ScreenSlidePageFragment() {}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mPageNumber = getArguments().getInt(ARG_PAGE);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.war_slide_fragment, 
					container, false);

			imageView = (ImageView) rootView.findViewById(R.id.imageview);
			mTvTitle = ((TextView) rootView.findViewById(android.R.id.text1));
			mTvDescription = ((TextView) rootView.findViewById(R.id.description));
			buttonPlay = (Button) rootView.findViewById(R.id.button_play);
			buttonPlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new Thread(){
						@Override
						public void run() {
//							if (isFlashEnable()) {
								Intent intent = new Intent(getActivity(), MediaPlayActivity.class);
								intent.putExtra("index", mPageNumber);
								startActivity(intent);
//							} else {
//								goToBrowser(mPageNumber);
//							}
						}
						
					}.start();
				}
			});
			
			loadDate(mPageNumber);

			return rootView;
		}
		
		public void loadDate(int position) {
//			new LoadDataAsync().execute(position);
			int resId = getResources().getIdentifier("a" + (position + 1), "drawable", getActivity().getPackageName());
			mTextUtils.setTitleStyle(titleSpan, mTitles[position]);
			mTextUtils.setTextStyle(textSpan, 7, 8, mDescriptions[position]);
			imageView.setImageResource(resId);
			mTvTitle.setTypeface(xApplication.getBoldTypeface());
			mTvTitle.setText(titleSpan);
			mTvDescription.setTypeface(xApplication.getNormalTypeface());
			mTvDescription.setText(textSpan);
			mTvDescription.setMovementMethod(scrollingMovementMethod);
		}
		
		class LoadDataAsync extends AsyncTask<Integer, Void, Integer> {

			@Override
			protected Integer doInBackground(Integer... mPageNumber) {
				long s1 = System.currentTimeMillis();
				Log.e("owen", "doInBackground start:" + s1);
				int resId = getResources().getIdentifier("a" + (mPageNumber[0] + 1), "drawable", getActivity().getPackageName());
				mTextUtils.setTitleStyle(titleSpan, mTitles[mPageNumber[0]]);
				mTextUtils.setTextStyle(textSpan, 7, 8, mDescriptions[mPageNumber[0]]);
				long s2 = System.currentTimeMillis();
				Log.e("owen", "doInBackground end:" + s2);
				Log.e("owen", "time:" + (s2 - s1));
				
				return resId;
			}

			@Override
			protected void onPostExecute(Integer result) {
				super.onPostExecute(result);
				long s1 = System.currentTimeMillis();
				Log.e("owen", "onPostExecute start:" + s1);
				imageView.setImageResource(result);
				mTvTitle.setTypeface(xApplication.getBoldTypeface());
				mTvTitle.setText(titleSpan);
				mTvDescription.setTypeface(xApplication.getNormalTypeface());
				mTvDescription.setText(textSpan);
				mTvDescription.setMovementMethod(scrollingMovementMethod);
				long s2 = System.currentTimeMillis();
				Log.e("owen", "onPostExecute end:" + s2);
				Log.e("owen", "time:" + (s2 - s1));
			}
		}

	}
	

	
	private boolean isFlashEnable() {
		PackageManager pm = getPackageManager();
		List<PackageInfo> infoList = pm.getInstalledPackages(PackageManager.GET_SERVICES);
		for (PackageInfo info : infoList) {
			if ("com.adobe.flashplayer".equals(info.packageName)) {
				return true;
			}
		}
		return false;
	}
	
	private void goToBrowser(int index) {
		Intent intent = new Intent("android.intent.action.VIEW");  
		intent.setData(Uri.parse("http://www.56.com/u35/v_OTU0NzM2ODg.html"));  
        startActivity(intent); 
	}

}
