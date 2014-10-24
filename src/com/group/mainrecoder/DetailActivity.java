package com.group.mainrecoder;

import java.lang.reflect.Field;

import com.example.mainrecoder.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class DetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
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
			TextView textView = (TextView) view.findViewById(R.id.rename);
			textView.setText("1");
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("重命名").setView(view).setPositiveButton("确定", null)
					.setNegativeButton("返回", null).create().show();

			// Dialog dialog = builder.create();
			// dialog.show();
			return true;
		}
		if (id == R.id.action_delete) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("删除").setMessage("删除录音文件")
					.setPositiveButton("确定", null)
					.setNegativeButton("返回", null).create().show();
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
