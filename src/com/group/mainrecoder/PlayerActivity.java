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
	private boolean stauts = false;
	// 定义按钮
	private Button DeleteButton = null;
	private Button LastButton = null;
	private Button StarButton = null;
	private Button StopButton = null;
	private Button PauseButton = null;
	private Button NextButton = null;
	// 声明Player
	private MediaPlayer mPlayer = null;
	// 播放列表
	private List<String> mList = new ArrayList<String>();
	// 当前播放歌曲的索引
	private int currentListItem = 0;
	// 音乐路径
	private static final String MUSIC_PATH = new String(Environment
			.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
			.getPath()
			+ "/");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mList.add("test.amr");
		setContentView(R.layout.activity_player);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("录音播放");
		// 更新显示播放列表
		// musicList();

		// 构建Player对象，以及Button
		mPlayer = new MediaPlayer();
		SeekBar bar =  (SeekBar)activity.findViewById(R.id.SeekBar);
		System.out.println("1");
		bar.setMax(10000);
		System.out.println("1");

		DeleteButton = (Button) this.findViewById(R.id.DeleteButton);
		LastButton = (Button) this.findViewById(R.id.LastButton);
		StarButton = (Button) this.findViewById(R.id.StarButton);
		StopButton = (Button) this.findViewById(R.id.StopButton);
		PauseButton = (Button) this.findViewById(R.id.PauseButton);
		NextButton = (Button) this.findViewById(R.id.NextButton);

		// DeleteButton按钮(未完成……)
		DeleteButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}

		});

		// 停止按钮
		StopButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否在播放
				if (mPlayer.isPlaying()) {
					mPlayer.reset();// 重置mPlayer到初始化状态
					stauts = false;// 将播放器内容设置为空
				}
			}
		});

		// 开始按钮
		StarButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!stauts) {
					// 当播放器内没有加载音频时，加载新音频
					playMusic(MUSIC_PATH + mList.get(currentListItem));// 录音播放路径的获取……
					stauts = true;
				} else {
					// 当播放器内有音频时，继续播放
					mPlayer.start();
				}

			}

		});

		// 暂停按钮
		PauseButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// if(mPlayer.isPlaying()){
				// 如果在播放，就暂停
				mPlayer.pause();
				// }else{
				// //如果是暂停，就播放
				// mPlayer.start();
				// }

			}

		});

		// Next按钮
		NextButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nextMusic();
			}

		});

		// Last按钮
		LastButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LastMusic();
			}

		});

	}

	// 播放音乐
	private void playMusic(String path) {
		try {
			// 重置mPlayer
			mPlayer.reset();
			// 设置播放文件的路径
			mPlayer.setDataSource(path);
			// 准备播放
			mPlayer.prepare();
			// 开始播放
			mPlayer.start();
			// 播放完毕后设置监听事件
			mPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer arg0) {
					// 播放完成后重置
					mPlayer.reset();
					stauts = false;// 将播放器内容设置为空
				}
			});
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 下一首
	private void nextMusic() {
		if (++currentListItem >= mList.size()) {
			currentListItem = 0;
			playMusic(MUSIC_PATH + mList.get(currentListItem));// 从第一首歌开始播放
		} else {
			playMusic(MUSIC_PATH + mList.get(currentListItem));
		}
	}

	// 上一首
	private void LastMusic() {
		if (--currentListItem > 0) {
			playMusic(MUSIC_PATH + mList.get(currentListItem));
		} else {
			// 如果当前currentListItem等于-1，则设置currentListItem=0
			if (currentListItem < 0) {
				currentListItem = 0;
			}
			playMusic(MUSIC_PATH + mList.get(currentListItem));
		}
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
