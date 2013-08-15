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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.war_slide);

        initPosition = getIntent().getIntExtra("initPosition", 0);
        
        final ActionBar actionBar = getSupportActionBar();
        
        
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });
        mPager.setCurrentItem(initPosition);
    }

    @Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
//    	getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
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
        public static final String ARG_PAGE = "page";

        private int mPageNumber;
        private String[] mTitles;
        private String[] mDescriptions;
        private String[] mTitlePres;
        private TextView mTvTitle;
        private TextView mTvDescription;

        public ScreenSlidePageFragment() {}

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mPageNumber = getArguments().getInt(ARG_PAGE);
            mTitles = getResources().getStringArray(R.array.game_title);
            mDescriptions = getResources().getStringArray(R.array.game_text);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.war_slide_fragment, 
            													container, false);

            mTvTitle = ((TextView) rootView.findViewById(android.R.id.text1));
            mTvDescription = ((TextView) rootView.findViewById(R.id.description));
            
            int resId = getResources().getIdentifier("a" + (mPageNumber + 1), "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.imageview)).setImageResource(resId);
            mTvTitle.setText(mTitles[mPageNumber]);
            mTvDescription.setText("        " + mDescriptions[mPageNumber]);
            
            return rootView;
        }

        public int getPageNumber() {
            return mPageNumber;
        }

    }
}
