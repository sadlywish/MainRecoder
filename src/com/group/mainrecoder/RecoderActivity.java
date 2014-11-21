package com.group.mainrecoder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mainrecoder.R;
import com.example.mainrecoder.R.id;
import com.example.mainrecoder.R.layout;
import com.example.mainrecoder.R.menu;
import com.group.mainrecoder.RecoderFactory.testInterface;

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
import android.os.Process;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecoderActivity extends ActionBarActivity implements testInterface {
	final Activity activity = this;
	private SharedPreferences pref;
	private EditText textView;
	private Button start;
	private Button stop;
	private Button pause;
	private long exitTime = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		RecoderFactory.setPref(pref);
		RecoderFactory.setInterface1(this);
		// SharedPreferences sharedPref = new
		// Activity().getPreferences(activity.MODE_ENABLE_WRITE_AHEAD_LOGGING);
		start = (Button) findViewById(R.id.start);
		stop = (Button) findViewById(R.id.stop);
		pause = (Button) findViewById(R.id.pause);
		if (RecoderFactory.isRecoding()) {
			Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
			chronometer.setBase(RecoderFactory.getBaseTime());
			chronometer.start();
			start.setEnabled(false);
			TextView textView = (TextView) activity.findViewById(R.id.stauts);
			if (RecoderFactory.ispause()) {
				textView.setText("录音暂停");
			} else {
				textView.setText("录音中");
			}
		} else {
			stop.setEnabled(false);
			pause.setEnabled(false);
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
				start.setEnabled(false);
				stop.setEnabled(true);
				if (pref.getString("recoderType", null).equals("amr")) {
					pause.setEnabled(true);
				}

			}
		});
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
				RecoderFactory.pause();
				start.setEnabled(true);
				stop.setEnabled(false);
				pause.setEnabled(false);
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
				TextView suffix = (TextView) view
						.findViewById(R.id.rename_Suffix);
				suffix.setText("."+pref.getString("recoderType", "amr"));
				textView = (EditText) view.findViewById(R.id.rename);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				textView.setText(df.format(new Date()));
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("保存录音").setView(view)
						.setNegativeButton("保存", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String name = RecoderFactory.save(textView
										.getText().toString(), activity);
								Toast.makeText(activity, name,
										Toast.LENGTH_SHORT).show();
							}
						}).setPositiveButton("放弃", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								RecoderFactory.cancel();

							}
						}).create().show();

				TextView textView1 = (TextView) activity
						.findViewById(R.id.stauts);
				textView1.setText("");
				start.setEnabled(true);
				stop.setEnabled(false);
				pause.setEnabled(false);
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
		if (id == R.id.Setting) {
			Intent settingIntent = new Intent(this, FragmentPreferences.class);
			startActivity(settingIntent);
		}
		if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.addCategory(Intent.CATEGORY_HOME);  
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
            startActivity(intent);  
            android.os.Process.killProcess(Process.myPid());  
		}
		return;
	}

	
	@Override
	public void test() {
		System.out.println("test");
		TextView textView = (TextView) activity
				.findViewById(R.id.stauts);
		textView.setText("从接口过来的操作");
	}

}
