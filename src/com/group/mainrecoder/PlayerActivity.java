package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.example.mainrecoder.R;

public class PlayerActivity extends ActionBarActivity {
	final Activity activity = this;
	// 定义播放器状态（真为有文件在播放）
	private boolean isIsprepare = false;
	
	// 定义按钮
	private Button DeleteButton = null;
	private Button LastButton = null;
	private Button SPButton = null;
	private Button StopButton = null;
	private Button NextButton = null;
	
	// 声明Player
	private MediaPlayer mPlayer = null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList.add("test.amr");
		setContentView(R.layout.activity_player);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("录音播放");
		
		init();
		findView();
		setListener();
		
	}
		

	// 构建Player对象
	private void init(){
		mPlayer = new MediaPlayer();
		SeekBar bar =  (SeekBar)activity.findViewById(R.id.SeekBar);
		System.out.println("1");
		bar.setMax(10000);
		System.out.println("1");
	}

	//构建Button对象
	private void findView(){
		DeleteButton = (Button) this.findViewById(R.id.DeleteButton);
		LastButton = (Button) this.findViewById(R.id.LastButton);
		SPButton = (Button) this.findViewById(R.id.SPButton);
		StopButton = (Button) this.findViewById(R.id.StopButton);
		NextButton = (Button) this.findViewById(R.id.NextButton);
	
	}

	private void setListener(){
		// DeleteButton按钮(未完成……)
		DeleteButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {

			}
		});
		

		// 停止按钮--停止播放操作
		StopButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				PlayerFactory.stop();
			}
		});
		

		// 开始/暂停按钮--开始、暂停切换操作
		SPButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(!isIsprepare){
					SPButton.setText("暂停");
					PlayerFactory.play();
				}
			}
		});
		

		// Next按钮--下一曲操作
		NextButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				PlayerFactory.nextMusic();
			}
		});
		

		// Last按钮--上一曲操作
		LastButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				PlayerFactory.LastMusic();
			}
		});
	}	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blankmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return super.onOptionsItemSelected(item);
	}


}
