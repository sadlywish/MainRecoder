package com.group.mainrecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.mainrecoder.R;

import android.R.string;
import android.app.Activity;
import android.app.ApplicationErrorReport.AnrInfo;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DetailListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		String[] titles = {"文件名:", "时长:", "录制时间:" };
		String[] details = {"1.mp3", "00：04：23.00", "2014-10-16" };
		List<Map<String, String>> textList = new ArrayList<Map<String,String>>();
		for (int i = 0; i < details.length; i++) {
			Map<String, String> allText = new HashMap<String, String>();
			allText.put("title",titles[i]);
			allText.put("detail", details[i]);
			textList.add(allText);
		}
		setListAdapter(new SimpleAdapter(getActivity(),textList, R.layout.listadpater, 
				new String[]{"title","detail"},new int[]{R.id.title,R.id.text}));
//		setListAdapter(new ArrayAdapter<String>(getActivity(),
//				android.R.layout.simple_list_item_activated_1,
//				android.R.id.text1, details));
		// setListAdapter(baseAdapter);
		super.onCreate(savedInstanceState);
	}

}
