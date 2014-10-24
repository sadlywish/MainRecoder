package com.group.mainrecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mainrecoder.R;

import android.R.raw;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RecordingListFragment extends ListFragment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/*
	 * @Override public void onCreate(Bundle savedInstanceState) { String[]
	 * detail = { "Recording1.mp3" }; setListAdapter(new
	 * ArrayAdapter<String>(getActivity(),
	 * android.R.layout.simple_list_item_activated_1, android.R.id.text1,
	 * detail));
	 * 
	 * super.onCreate(savedInstanceState); }
	 */

	private List<Map<String, Object>> mData;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getData();
		MyAdapter adapter = new MyAdapter(this.getActivity());
		setListAdapter(adapter);
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("title", "G1");
		map.put("info", "google 1");
		// map.put("img", R.drawable.i1);
		list.add(map);

		return list;
	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView title;
		public TextView info;
		public Button viewBtn;
		public Button playBtn;
	}

	public class MyAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.vlist2, null);
				// holder.img = (ImageView)convertView.findViewById(R.id.img);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.viewBtn = (Button) convertView
						.findViewById(R.id.view_btn);
				holder.playBtn = (Button) convertView.findViewById(R.id.play_btn);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			// holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));

			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							getActivity());
//					builder.setTitle("详细").setMessage("该去哪").create().show();
					Intent intent = new Intent(getActivity(), DetailActivity.class);
					getActivity().startActivity(intent);
				}
			});
			holder.playBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
//					AlertDialog.Builder builder = new AlertDialog.Builder(
//							getActivity());
//					builder.setTitle("播放").setMessage("该是啥是啥").create().show();
					Intent intent = new Intent(getActivity(), PlayerActivity.class);
					getActivity().startActivity(intent);
				}
			});
			return convertView;
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("播放").setMessage("该播就播吧").create().show();
	}

}
