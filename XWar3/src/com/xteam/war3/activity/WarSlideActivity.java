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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.xteam.war3.utils.TextUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        mPager.setOffscreenPageLimit(4);
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
            return fragment;
		}
		
    }
    
    public class ScreenSlidePageFragment extends Fragment {
        private int mPageNumber;
        private TextView mTvTitle;
        private TextView mTvDescription;

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

            mTvTitle = ((TextView) rootView.findViewById(android.R.id.text1));
            mTvDescription = ((TextView) rootView.findViewById(R.id.description));
            mTvDescription.setMovementMethod(scrollingMovementMethod);
            
            int resId = getResources().getIdentifier("a" + (mPageNumber + 1), "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.imageview)).setImageResource(resId);
            mTvTitle.setTypeface(xApplication.getBoldTypeface());
            mTextUtils.setTitleStyle(titleSpan, mTitles[mPageNumber]);
            mTvTitle.setText(titleSpan);
            mTvDescription.setTypeface(xApplication.getNormalTypeface());
            mTextUtils.setTextStyle(textSpan, 7, 8, mDescriptions[mPageNumber]);
            mTvDescription.setText(textSpan);
            
            return rootView;
        }

        public int getPageNumber() {
            return mPageNumber;
        }

    }
}
