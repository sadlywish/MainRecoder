package com.group.mainrecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mainrecoder.R;

import android.R.color;
import android.R.drawable;
import android.R.fraction;
import android.R.raw;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	private float downX;//手指点下时获取的x坐标
	private float upX;  //手指离开时获取的x坐标
	private Button viewBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mData = getData();
		
		
		MyAdapter adapter = new MyAdapter(this.getActivity());
		setListAdapter(adapter);
		
	}

	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		List<FileDetail> details=null;
		try {
			details = FileManagement.getMusicFileList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < details.size(); i++) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("title", details.get(i).getFileName());
			map.put("info", "时间长度:" + details.get(i).getSringTime());
			if (details.get(i).getStatus()==0) {
				map.put("status", "本地");
			}else if (details.get(i).getStatus()==1) {
				map.put("status", "云端");
			} else {
				map.put("status", "同步");
			}
			
			if (details.get(i).isConflict()) {
				map.put("conflict", "冲突");
			} else {
				map.put("conflict","" );
			}

			// map.put("img", R.drawable.i1);
			list.add(map);
		}
		return list;
	}

	public final class ViewHolder {
		// public ImageView img;
		public TextView title;
		public TextView info;
		public Button viewBtn;
		public TextView status;
		public TextView conflict;
		// public Button playBtn;
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
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

//		ViewHolder holder = null;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			final int count = position;			 
			final View view = convertView;
			ViewHolder holder =null;
			if (convertView == null) {

				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.vlist2, null);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				holder.info = (TextView) convertView.findViewById(R.id.info);
				holder.conflict =  (TextView) convertView.findViewById(R.id.conflict);
				holder.status =  (TextView) convertView.findViewById(R.id.status);
				holder.viewBtn = (Button) convertView.findViewById(R.id.view_btn);
				// holder.playBtn = (Button) convertView
				// .findViewById(R.id.play_btn);
				convertView.setTag(holder);

			} else {

				holder = (ViewHolder) convertView.getTag();
			}

			// holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
			holder.title.setText((String) mData.get(position).get("title"));
			holder.info.setText((String) mData.get(position).get("info"));
			holder.conflict.setText((String) mData.get(position).get("conflict"));
			holder.status.setText((String) mData.get(position).get("status"));
			convertView.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					// TODO Auto-generated method stub
					//获取滑动时候相应的ViewHolder，以便获取button按钮
					final ViewHolder holder = (ViewHolder) view.getTag();//获取滑动时候相应的ViewHolder,以便获取button按钮
					switch(event.getAction()){
					//手指按下
					case MotionEvent.ACTION_DOWN:
						
						downX = event.getX();//获取手指按下时的x坐标
						System.out.println("起始位置:"+downX);
						if(viewBtn != null){
							viewBtn.setVisibility(View.GONE);//隐藏显示出来的button
						}
						break;
						
					//手指离开	
					case MotionEvent.ACTION_UP:
						upX = event.getX();//获取手指离开时的x坐标
						System.out.println("結束位置:"+upX);

						break;
					}
					if(holder.viewBtn != null){
						if((downX - upX)>35){
							holder.viewBtn.setVisibility(View.VISIBLE);//显示详细的按钮
							viewBtn = holder.viewBtn;//赋值给全局的button
							return true;//结束事件
						}
						return false;//释放事件，使onListItemClick可以执行
					}
					
					return false;
				}
			} );
			//详细Button的事件监听
			holder.viewBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// AlertDialog.Builder builder = new AlertDialog.Builder(
					// getActivity());
					// builder.setTitle("详细").setMessage("该去哪").create().show();
					System.out.println(count + "");
					Intent intent = new Intent(getActivity(),
							DetailActivity.class);
					intent.putExtra("filename",
							(String) mData.get(count).get("title"));
					if(viewBtn != null){
						viewBtn.setVisibility(View.GONE);//点击详细按钮后，隐藏按钮
					}
					getActivity().startActivity(intent);
				}
			});
			/*
			 * 录音列表播放按钮 holder.playBtn.setOnClickListener(new
			 * View.OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // AlertDialog.Builder
			 * builder = new AlertDialog.Builder( // getActivity()); //
			 * builder.setTitle("播放").setMessage("该是啥是啥").create().show();
			 * Intent intent = new Intent(getActivity(), PlayerActivity.class);
			 * intent.putExtra("filename", (String)
			 * mData.get(count).get("title"));
			 * 
			 * getActivity().startActivity(intent); } });
			 */
			
//			//录音列表项点击播放
//			convertView.setClickable(true);
//			convertView.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//					// TODO Auto-generated method stub
//					Intent intent = new Intent(getActivity(),
//							PlayerActivity.class);
//					intent.putExtra("filename",
//							(String) mData.get(count).get("title"));
//					
//					getActivity().startActivity(intent);
//				}
//			});
			 return convertView;
		}

	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// builder.setTitle("播放").setMessage("该播就播吧").create().show();
		Intent intent = new Intent(getActivity(), PlayerActivity.class);
		intent.putExtra("filename", (String) mData.get(position).get("title"));

		getActivity().startActivity(intent);
	}
}
