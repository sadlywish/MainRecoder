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
import android.media.MediaRecorder;
import android.os.Bundle;
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
	private MediaRecorder mediaRecorder;

	public class TimeStep {
		private long timestep = 0;
		private boolean isRecoding = false;

		public boolean isRecoding() {
			return isRecoding;
		}

		public void setRecoding(boolean isRecoding) {
			this.isRecoding = isRecoding;
		}

		public long getTimestep() {
			return timestep;
		}

		public void setTimestep(long timestep) {
			this.timestep = timestep;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final Activity activity = this;
		final TimeStep recoderTime = new TimeStep();
		setContentView(R.layout.activity_main);
		Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (recoderTime.isRecoding) {
					return;
				}
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				if (recoderTime.getTimestep() < 1) {
					chronometer.setBase(SystemClock.elapsedRealtime());
				} else {
					chronometer.setBase(SystemClock.elapsedRealtime()
							- recoderTime.getTimestep());
				}
				TextView textView = (TextView) activity
						.findViewById(R.id.stauts);
				textView.setText("录音中");
				recoderTime.setTimestep(1);
				recoderTime.setRecoding(true);
				try {
					File file = new File("/sdcard/mediarecorder.amr");
					if (file.exists()) {
						// 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
						file.delete();
					}
					mediaRecorder = new MediaRecorder();
					// 设置音频录入源
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					// 设置录制音频的输出格式
					mediaRecorder
							.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					// 设置音频的编码格式
					mediaRecorder
							.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					// 设置录制音频文件输出文件路径
					mediaRecorder.setOutputFile(file.getAbsolutePath());

					// 准备、开始
					mediaRecorder.prepare();
					mediaRecorder.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
				chronometer.start();
			}
		});
		Button pause = (Button) findViewById(R.id.pause);
		pause.setEnabled(false);
		pause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!recoderTime.isRecoding) {
					return;
				}
				// TODO Auto-generated method stub
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				long thistime = chronometer.getBase();
				recoderTime.setTimestep(SystemClock.elapsedRealtime()
						- thistime);
				TextView textView = (TextView) activity
						.findViewById(R.id.stauts);
				textView.setText("录音暂停");
				chronometer.stop();
				recoderTime.setRecoding(false);
			}
		});
		Button stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (recoderTime.getTimestep() < 1) {
					return;
				}
				Chronometer chronometer = (Chronometer) findViewById(R.id.timestep);
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.stop();
				recoderTime.setTimestep(0);
				LayoutInflater inflater = LayoutInflater.from(activity);
				final View view = inflater.inflate(R.layout.renamedialog, null);
				TextView textView = (TextView) view.findViewById(R.id.rename);
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				textView.setText(df.format(new Date()));
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle("保存录音").setView(view)
						.setNegativeButton("保存", new  OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
					            // 如果正在录音，停止并释放资源
					            mediaRecorder.stop();
					            mediaRecorder.release();
					            mediaRecorder = null;
								
							}
						})
						.setPositiveButton("放弃", null).create().show();
				recoderTime.setRecoding(false);
				TextView textView1 = (TextView) activity
						.findViewById(R.id.stauts);
				textView1.setText("");
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
