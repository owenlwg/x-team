package com.xteam.war3.activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class WarListFragment extends SherlockFragment {
	
	private Context mContext;
    private String[] mTitles;
    private String[] mDescriptions;
    private ListView mListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		mContext = getActivity();
        mTitles = getResources().getStringArray(R.array.game_title);
        mDescriptions = getResources().getStringArray(R.array.game_text);
        
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.war_list, container, false);
		
		mListView = (ListView) rootView.findViewById(R.id.listView);
		mListView.setAdapter(new SimpleAdapter());
		
		return rootView;
	}

	
	class SimpleAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.war_list_item, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) convertView.findViewById(R.id.image);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.description = (TextView) convertView.findViewById(R.id.description);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			int resId = mContext.getResources().getIdentifier("a" + (position + 1), "drawable", mContext.getPackageName());
			holder.imageView.setImageResource(resId);
			holder.title.setText(mTitles[position]);
			holder.description.setText(mDescriptions[position].trim());
			
			return convertView;
		}
		
		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return mTitles[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		public class ViewHolder {
			ImageView imageView;
			TextView title;
			TextView description;
		}
	}
	
	
}