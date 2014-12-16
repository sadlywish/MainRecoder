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
	private static String[] statusList={"本地文件","云端文件","完成同步"};
	@Override
	public void onCreate(Bundle savedInstanceState) {
		String fileName = getActivity().getIntent().getStringExtra("filename");
		int status = getActivity().getIntent().getIntExtra("status",0);
		boolean conflict = getActivity().getIntent().getBooleanExtra("conflict", false);
		String[] titles = {"文件名:", "时长:", "最后修改时间:","文件体积","存储状态" };
		FileDetail detail = new FileDetail(fileName, status);
		String[] details = {detail.getFileName(), detail.getSringTime(), detail.getModiTimeTime(),detail.getSize() ,statusList[status]};
		System.out.println(detail.getsizeall());
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
