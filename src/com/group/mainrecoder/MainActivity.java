package com.group.mainrecoder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mainrecoder.R;
import com.example.mainrecoder.R.id;
import com.example.mainrecoder.R.layout;
import com.example.mainrecoder.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	final Activity activity = this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final Activity activity = this;
		// SharedPreferences sharedPref = new
		// Activity().getPreferences(activity.MODE_ENABLE_WRITE_AHEAD_LOGGING);
		Button start = (Button) findViewById(R.id.start);
		Button stop = (Button) findViewById(R.id.stop);	
		Button pause = (Button) findViewById(R.id.pause);
		if (RecoderFactory.isRecoding()) {
			Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
			chronometer.setBase(RecoderFactory.getBaseTime());
			chronometer.start();
			start.setEnabled(false);
			TextView textView = (TextView) activity
					.findViewById(R.id.stauts);
			textView.setText("录音中");
		}else {
				stop.setEnabled(false);
		}
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				if (RecoderFactory.getTimestep() < 1) {
					chronometer.setBase(SystemClock.elapsedRealtime());
				} else {
					chronometer.setBase(SystemClock.elapsedRealtime()
							- RecoderFactory.getTimestep());
				}
				TextView textView = (TextView) activity
						.findViewById(R.id.stauts);
				textView.setText("录音中");
				RecoderFactory.start();
				chronometer.start();
				Button start = (Button) findViewById(R.id.start);
				start.setEnabled(false);
				Button stop = (Button) findViewById(R.id.stop);	
				stop.setEnabled(true);
			}
		});

		pause.setEnabled(false);
		pause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!RecoderFactory.isRecoding()) {
					return;
				}
				// TODO Auto-generated method stub
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				long thistime = chronometer.getBase();

				TextView textView = (TextView) activity
						.findViewById(R.id.stauts);
				textView.setText("录音暂停");
				chronometer.stop();
				RecoderFactory.setRecoding(false);
			}
		});
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.stop();
				RecoderFactory.stop();
				LayoutInflater inflater = LayoutInflater.from(activity);
				final View view = inflater.inflate(R.layout.renamedialog, null);
				TextView textView = (TextView) view.findViewById(R.id.rename);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				textView.setText(df.format(new Date()));
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("保存录音").setView(view)
						.setNegativeButton("保存", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RecoderFactory.setRecoding(false);
							}
						}).setPositiveButton("放弃", null).create().show();

				TextView textView1 = (TextView) activity
						.findViewById(R.id.stauts);
				textView1.setText("");
				Button start = (Button) findViewById(R.id.start);
				start.setEnabled(true);
				Button stop = (Button) findViewById(R.id.stop);	
				stop.setEnabled(false);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_index) {
			Intent fileListIntent = new Intent(this, FileListActivity.class);
			startActivity(fileListIntent);

		}
		return super.onOptionsItemSelected(item);
	}

	
}
