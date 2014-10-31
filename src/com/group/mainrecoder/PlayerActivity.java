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
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

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
	
	//定义文本框
	private TextView textView ;

	// 定义进度条
	private SeekBar seekBar;

	// 声明Player
	private MediaPlayer mPlayer = null;

	// 声明播放文件名
	private String soundName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		soundName = "";// 这里是获取的文件名，还没完！！！
		textView.setText(soundName);
		PlayerFactory.selectByName(soundName);
		setContentView(R.layout.activity_player);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("录音播放");

		init();
		findView();
		setListener();
		
	}
	
	//脱离播放界面时释放资源
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		PlayerFactory.release();
		super.onDestroy();
	}
	
	// 构建Player对象
	private void init() {
		mPlayer = new MediaPlayer();

	}

	// 构建Button对象
	private void findView() {
		DeleteButton = (Button) this.findViewById(R.id.DeleteButton);
		LastButton = (Button) this.findViewById(R.id.LastButton);
		SPButton = (Button) this.findViewById(R.id.SPButton);
		StopButton = (Button) this.findViewById(R.id.StopButton);
		NextButton = (Button) this.findViewById(R.id.NextButton);

		seekBar = (SeekBar) this.findViewById(R.id.SeekBar);
		// System.out.println("1");//断点测试
		// 获得录音长度，并设置为进度条的最大值
		seekBar.setMax(10000);
		// System.out.println("2");//断点测试
	}

	Handler handler = new Handler();

	Runnable updateThread = new Runnable() {
		public void run() {
			// 获得现在歌曲播放的位置并设置为进度条的值
			seekBar.setProgress(PlayerFactory.getPlayRate());
			// 每次延迟100毫秒启动线程
			handler.postDelayed(updateThread, 100);
		}
	};

	private void setListener() {
		// DeleteButton按钮(未完成……)
		DeleteButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				FileManagement.deleteMusicFlie(PlayerFactory.getFileName());
				PlayerFactory.nextMusic();//删除后自动播放下一曲
				textView.setText(PlayerFactory.getFileName());
			}
		});

		// 停止按钮--停止播放操作
		StopButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				PlayerFactory.stop();
				handler.removeCallbacks(updateThread);
			}
		});

		// 开始/暂停按钮--开始、暂停切换操作
		SPButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (!PlayerFactory.getMediaPlayer().isPlaying()) {
					SPButton.setText("暂停");					
					PlayerFactory.play();
				} else {
					SPButton.setText("播放");
					PlayerFactory.pause();
				}
				handler.post(updateThread);
			}
		});

		// Next按钮--下一曲操作
		NextButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				PlayerFactory.nextMusic();
				textView.setText(PlayerFactory.getFileName());
			}
		});

		// 拖动进度条的监听器
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
				if (fromUser == true) {
					// 如果是用户拖动进度条改变滑块值
					PlayerFactory.seekTo(progress);
					// if (!PlayerFactory.seekTo(progress)) {
					// seekBar.setProgress(0);//當沒有準備好的時候進度條归零
					// }
				}
			}
		});

		// Last按钮--上一曲操作
		LastButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				PlayerFactory.LastMusic();
				textView.setText(PlayerFactory.getFileName());
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
