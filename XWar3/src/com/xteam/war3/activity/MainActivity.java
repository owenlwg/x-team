package com.xteam.war3.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener{

	private Context mContext;
	private ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		
		setContentView(R.layout.main);
		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
		SimplePageAdapter simplePageAdapter= new SimplePageAdapter(getSupportFragmentManager());
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(simplePageAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
			
		});
		
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.shouye)).setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(getString(R.string.zhanyi)).setTabListener(this));
	}
	
	private class SimplePageAdapter extends FragmentStatePagerAdapter {

		private HomeFragment homeFragment;
		private WarListFragment warListFragment;
		
		public SimplePageAdapter(FragmentManager fm) {
			super(fm);
			homeFragment = new HomeFragment();
			warListFragment = new WarListFragment();
		}

		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
			case 0:
				return homeFragment;
			case 1:
				return warListFragment;
			default:
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {}
	

}
