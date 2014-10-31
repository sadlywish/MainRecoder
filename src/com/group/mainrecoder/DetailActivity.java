package com.group.mainrecoder;

import java.lang.reflect.Field;

import com.example.mainrecoder.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		fileName = getIntent().getStringExtra("filename");
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
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("重命名").setView(view)
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							try {
								boolean succese = FileManagement
										.renameMusicFile(fileName,
												 textView.getText()
														+ ".amr");
								if (succese) {
									Toast.makeText(activity, "文件重命名成功",
											Toast.LENGTH_SHORT);
								} else {
									Toast.makeText(activity, "文件重名，重命名失败",
											Toast.LENGTH_SHORT);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
							Intent intent = new Intent(activity,
									DetailActivity.class);
							intent.putExtra("filename", textView.getText()
									+ ".amr");
							activity.startActivity(intent);
						}
					}).setNegativeButton("返回", null).create().show();

			// Dialog dialog = builder.create();
			// dialog.show();
			return true;
		}
		if (id == R.id.action_delete) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("删除").setMessage("删除录音文件")
					.setPositiveButton(R.string.apply, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							FileManagement.deleteMusicFlie(fileName);
							Intent fileListIntent = new Intent(activity, FileListActivity.class);
							startActivity(fileListIntent);
						}
					}).setNegativeButton("返回", null).create().show();
			// Dialog dialog = builder.create();
			// dialog.show();
			return true;
		}
		if (id == R.id.action_share) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("分享").setMessage("分享录音文件")
					.setPositiveButton("确定", null)
					.setNegativeButton("返回", null).create().show();
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

}
