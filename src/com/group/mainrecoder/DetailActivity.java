package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import com.example.mainrecoder.R;


import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class DetailActivity extends ActionBarActivity {
	private String fileName;
	private EditText textView;
	private Activity activity;
	private String suffix;
	private int status;
	private boolean conflict;
	private String[] statusList = {"本地","云端","同步"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		fileName = getIntent().getStringExtra("filename");
		status = getIntent().getIntExtra("status",0);
		conflict = getIntent().getBooleanExtra("conflict", false);
		activity = this;
		this.getOverflowMenu();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(R.string.detail);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_rename) {
			LayoutInflater inflater = LayoutInflater.from(this);
			final View view = inflater.inflate(R.layout.renamedialog, null);
			textView = (EditText) view.findViewById(R.id.rename);
			textView.setText(fileName.substring(0, fileName.lastIndexOf(".")));
			int dot = fileName.lastIndexOf('.');
			if ((dot > -1) && (dot < (fileName.length() - 1))) {
				suffix = fileName.substring(dot + 1);
			}
			TextView suffixview = (TextView) view
					.findViewById(R.id.rename_Suffix);
			suffixview.setText("." + suffix);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("重命名").setView(view)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								boolean succese = FileManagement
										.renameMusicFile(fileName,
												textView.getText() + "."
														+ suffix);
								boolean succesOnline = KiiUtil.renameFlie(fileName, 	textView.getText() + "."+ suffix);
								if (succese) {
									Toast.makeText(activity, "文件重命名成功",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(activity, "文件重名，重命名失败",
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							Intent intent = new Intent(activity,
									DetailActivity.class);
							intent.putExtra("filename", textView.getText() + "."+ suffix);
							activity.startActivity(intent);
						}
					}).setNegativeButton("返回", null).create().show();

			// Dialog dialog = builder.create();
			// dialog.show();
			return true;
		}
		if (id == R.id.action_delete) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (status==2) {
				//定义复选框选项  
		        final String[] multiChoiceItems = {"本地文件","云端文件"};  
		        //复选框默认值：false=未选;true=选中 ,各自对应items[i]
		        final boolean[] defaultSelectedStatus = {false,false};
				builder.setTitle("删除").setMessage("请选择需要删除的文件").setMultiChoiceItems(multiChoiceItems, defaultSelectedStatus, new OnMultiChoiceClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						// TODO Auto-generated method stub
						 defaultSelectedStatus[which] = isChecked;
					}
				}).setPositiveButton(R.string.apply, new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (defaultSelectedStatus[0]) {
							FileManagement.deleteMusicFlie(fileName);
						}
						if (defaultSelectedStatus[1]) {
							KiiUtil.deleteOnlineFile(fileName);
						}
						Intent fileListIntent = new Intent(activity,
								FileListActivity.class);
						startActivity(fileListIntent);			
					}
				}).setNegativeButton("返回", null).create().show();
				
			}else{
				builder.setTitle("删除").setMessage("删除"+statusList[status]+"录音文件")
						.setPositiveButton(R.string.apply, new OnClickListener() {
	
							@Override
							public void onClick(DialogInterface dialog, int which) {
								if (status==0) {
									FileManagement.deleteMusicFlie(fileName);
								} else {
									KiiUtil.deleteOnlineFile(fileName);
								}
									Intent fileListIntent = new Intent(activity,
											FileListActivity.class);
									startActivity(fileListIntent);								
							}
						}).setNegativeButton("返回", null).create().show();
				// Dialog dialog = builder.create();
				// dialog.show();
				return true;
			}
		}
		if (id == R.id.action_share) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("同步").setMessage("同步录音文件")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (!conflict) {
								//							KiiUtil.uploadFile(activity, fileName);
								//							rundownload();
								if (status == 0) {
									System.out.println("上传");
									KiiUtil.uploadFile(activity, fileName);
								} else if (status == 1) {
									System.out.println("下载");
									KiiUtil.downloadFlie(activity, fileName);
								} else {
									Toast.makeText(activity, "同步已完成，不需要同步", Toast.LENGTH_SHORT).show();
								}
							}else {
								Toast.makeText(activity, "文件存在冲突，请重命名后再同步", Toast.LENGTH_SHORT).show();
							}
						}
					}).setNegativeButton("返回", null).create().show();
			// Dialog dialog = builder.create();
			// dialog.show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void getOverflowMenu() {

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
private void rundownload() {
	handler.post(runThread);
}
 Handler handler = new Handler();
 Runnable runThread =new Runnable() {
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//		AlertDialog dialog =builder.setTitle("下载中").setMessage("请稍等").setCancelable(false).show();
//		Toast.makeText(activity, "同步中，请稍等。", Toast.LENGTH_LONG).show();
		try {
//			KiiUtil.uploadFile(activity, fileName);
//			KiiUtil.downloadFlie(activity, fileName);
//			if(KiiUtil.renameFlie("2014.amr", "2015.amr")){
//				Toast.makeText(activity, "重命名成功", Toast.LENGTH_SHORT).show();
//			}else {
//				Toast.makeText(activity, "重命名失败", Toast.LENGTH_SHORT).show();
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		dialog.dismiss();
//		Toast.makeText(activity, "同步完成", Toast.LENGTH_SHORT).show();
	}
};
}
