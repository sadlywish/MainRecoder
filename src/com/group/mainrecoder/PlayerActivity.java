package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainrecoder.R;
import com.group.mainrecoder.RecordingListFragment.MyAdapter;

public class PlayerActivity extends ActionBarActivity {
	final Activity activity = this;
	private String fileName;
	private int status;
	private String[] statusList = { "本地", "云端", "同步" };
	// 定义按钮
	private Button LastButton = null;
	private Button SPButton = null;
	private Button StopButton = null;
	private Button NextButton = null;

	// 定义文本框
	private TextView textView;
	private TextView nowTime;
	private TextView totalTime;

	// 定义进度条
	private SeekBar seekBar;

	// 声明播放文件名
	private String soundName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		fileName = getIntent().getStringExtra("filename");
		status = getIntent().getIntExtra("status", 0);
		soundName = getIntent().getStringExtra("filename");// 这里是获取的文件名
		textView = (TextView) this.findViewById(R.id.SoundMessage);
		nowTime = (TextView) this.findViewById(R.id.NowTime);
		totalTime = (TextView) this.findViewById(R.id.TotalTime);
		textView.setText(soundName);
		nowTime.setText("00:00");
		totalTime.setText("00:00");
		PlayerFactory.selectByName(soundName);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle("录音播放");
		findView();
		setListener();
		PlayerFactory.play();
		SPButton.setText("暂停");
		handler.post(updateThread);
	}

	// 脱离播放界面时释放资源
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		PlayerFactory.release();
		super.onDestroy();
		handler.removeCallbacks(updateThread);
	}

	// 构建Button、seekBar实例对象
	private void findView() {
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
			// 显示当前播放时间
			nowTime.setText(PlayerFactory.getNowTime());
			// 显示录音总时长
			totalTime.setText(PlayerFactory.getTotalTime());

			// 获得现在歌曲播放的位置并设置为进度条的值
			seekBar.setProgress(PlayerFactory.getPlayRate());
			// System.out.println(PlayerFactory.getPlayRate());

			// System.out.println(""+
			// PlayerFactory.getMediaPlayer().getCurrentPosition());
			// System.out.println(""+
			// PlayerFactory.getMediaPlayer().getDuration());

			// 当录音播放结束后，重置进度条和播放时间
			if (PlayerFactory.getMediaPlayer().getCurrentPosition() + 80 >= PlayerFactory
					.getMediaPlayer().getDuration()) {

				seekBar.setProgress(0);
				nowTime.setText("00:00");
				SPButton.setText("播放");
			}

			// 每次延迟100毫秒启动线程
			handler.postDelayed(updateThread, 100);
		}
	};

	private void setListener() {

		/*
		 * // DeleteButton按钮 DeleteButton.setOnClickListener(new
		 * Button.OnClickListener() { public void onClick(View arg0) {
		 * AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
		 * dialog.setTitle("确认删除") .setMessage("确定要删除吗？")
		 * .setPositiveButton("刪除", new OnClickListener(){
		 * 
		 * @Override public void onClick(DialogInterface arg0, int arg1) {
		 * 
		 * FileManagement.deleteMusicFlie(PlayerFactory.getFileName());
		 * //删除后检查是否有录音，没有就返回录音界面，有就播放下一曲
		 * PlayerFactory.setmList(FileManagement.getMusicNameList());
		 * 
		 * PlayerFactory.stop();
		 * 
		 * //判断指针位置，放置数值溢出 if(PlayerFactory.getmListItem()-1 >=
		 * PlayerFactory.getmList().size()) { PlayerFactory.setmListItem(0);
		 * PlayerFactory.play(); textView.setText(PlayerFactory.getFileName());
		 * handler.post(updateThread); SPButton.setText("暂停"); } else if(
		 * PlayerFactory.getmList().size()==0) {
		 *//**
		 * 
		 * 删除后如果列表为空，跳转到录音界面
		 * 
		 */
		/*
		 * Intent intent = new Intent(activity,RecoderActivity.class);
		 * activity.startActivity(intent); onDestroy(); } else{ // 删除后自动播放下一曲
		 * PlayerFactory.play(); textView.setText(PlayerFactory.getFileName());
		 * handler.post(updateThread); SPButton.setText("暂停"); } } })
		 * .setNegativeButton("取消", null) .create() .show(); } });
		 */

		// 停止按钮--停止播放操作
		StopButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				PlayerFactory.stop();
				SPButton.setText("播放");
				seekBar.setProgress(0);
				nowTime.setText("00:00");
				handler.removeCallbacks(updateThread);
			}
		});

		// 开始/暂停按钮--开始、暂停切换操作
		SPButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if (!PlayerFactory.getMediaPlayer().isPlaying()) {
					SPButton.setText("暂停");
					PlayerFactory.play();
					handler.post(updateThread);
				} else {
					SPButton.setText("播放");
					PlayerFactory.pause();
				}

			}
		});

		// Next按钮--下一曲操作
		NextButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				if (PlayerFactory.getmListItem() + 1 >= PlayerFactory
						.getmList().size()) {
					Toast.makeText(activity, "现在已经是最后一条录音了！",
							Toast.LENGTH_SHORT).show();
					System.out.println("现在是最后一条");
				}
				PlayerFactory.nextMusic();
				textView.setText(PlayerFactory.getFileName());
				SPButton.setText("暂停");
				handler.post(updateThread);
			}
		});

		// Last按钮--上一曲操作
		LastButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View arg0) {
				if (PlayerFactory.getmListItem() == 0) {
					Toast.makeText(activity, "现在已经是第一条录音了！", Toast.LENGTH_SHORT)
							.show();
				}
				PlayerFactory.LastMusic();
				textView.setText(PlayerFactory.getFileName());
				handler.post(updateThread);
				SPButton.setText("暂停");
			}
		});

		// 拖动进度条的监听器
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			// 当用户结束拖动滑块时，调用该方法
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				SPButton.setText("暂停");
				PlayerFactory.play();
				handler.post(updateThread);
			}

			@Override
			// 当用户开始滑动滑块时,调用该方法
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// 判断是否在播放以防报错
				if (PlayerFactory.getMediaPlayer().isPlaying()) {
					PlayerFactory.pause();
				}
				if (!PlayerFactory.isIsprepare()) {
					PlayerFactory.inti();
				}

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser == true) {
					// 如果是用户拖动进度条改变滑块值
					PlayerFactory.seekTo(progress);
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this addsp items to the action bar if it is
		// present.
		getMenuInflater().inflate(R.menu.player_del, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_delete) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			if (status == 2) {
				// 定义复选框选项
				final String[] multiChoiceItems = { "本地文件", "云端文件" };
				// 复选框默认值：false=未选;true=选中 ,各自对应items[i]
				final boolean[] defaultSelectedStatus = { false, false };
				builder.setTitle("删除")
						.setMessage("请选择需要删除的文件")
						.setMultiChoiceItems(multiChoiceItems,
								defaultSelectedStatus,
								new OnMultiChoiceClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which, boolean isChecked) {
										// TODO Auto-generated method stub
										defaultSelectedStatus[which] = isChecked;
									}
								})
						.setPositiveButton(R.string.apply,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

										if (defaultSelectedStatus[1]) {
											KiiUtil.deleteOnlineFile(fileName);
										}
										if (defaultSelectedStatus[0]) {
											FileManagement
													.deleteMusicFlie(fileName);
											PlayerFactory.setmList(FileManagement
													.getMusicNameList());

											PlayerFactory.stop();

											// 判断指针位置，放置数值溢出
											try {
												if (PlayerFactory
														.getmListItem() > PlayerFactory
														.getmList().size() - 1) {
													PlayerFactory
															.setmListItem(0);
													PlayerFactory.play();
													textView.setText(PlayerFactory
															.getFileName());
													handler.post(updateThread);
													SPButton.setText("暂停");
												} else if (PlayerFactory
														.getmList().size() == 0) {
													/**
													 * 
													 * 删除后如果列表为空，跳转到录音界面
													 * 
													 */
													Intent intent = new Intent(
															activity,
															RecoderActivity.class);
													activity.startActivity(intent);
													onDestroy();
												} else {
													// 删除后自动播放下一曲
													PlayerFactory.play();
													textView.setText(PlayerFactory
															.getFileName());
													handler.post(updateThread);
													SPButton.setText("暂停");
												}
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}
										}
									}
								}).setNegativeButton("返回", null).create()
						.show();

			} else {
				builder.setTitle("删除")
						.setMessage("删除" + statusList[status] + "录音文件")
						.setPositiveButton(R.string.apply,
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (status == 0) {
											FileManagement
													.deleteMusicFlie(fileName);
										} else {
											KiiUtil.deleteOnlineFile(fileName);
										}
										PlayerFactory.setmList(FileManagement
												.getMusicNameList());

										PlayerFactory.stop();

										// 判断指针位置，放置数值溢出
										try {
											if (PlayerFactory.getmListItem() > PlayerFactory
													.getmList().size() - 1) {
												PlayerFactory.setmListItem(0);
												PlayerFactory.play();
												textView.setText(PlayerFactory
														.getFileName());
												handler.post(updateThread);
												SPButton.setText("暂停");
											} else if (PlayerFactory.getmList()
													.size() == 0) {
												/**
												 * 
												 * 删除后如果列表为空，跳转到录音界面
												 * 
												 */
												Intent intent = new Intent(
														activity,
														RecoderActivity.class);
												activity.startActivity(intent);
												onDestroy();
											} else {
												// 删除后自动播放下一曲
												PlayerFactory.play();
												textView.setText(PlayerFactory
														.getFileName());
												handler.post(updateThread);
												SPButton.setText("暂停");
											}
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}).setNegativeButton("返回", null).create()
						.show();
				// Dialog dialog = builder.create();
				// dialog.show();
				return true;
			}

			AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
			dialog.setTitle("确认删除").setMessage("确定要删除吗？")
					.setPositiveButton("刪除", new OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							FileManagement.deleteMusicFlie(PlayerFactory
									.getFileName());
							// 删除后检查是否有录音，没有就返回录音界面，有就播放下一曲
							PlayerFactory.setmList(FileManagement
									.getMusicNameList());

							PlayerFactory.stop();

							// 判断指针位置，放置数值溢出
							try {
								if (PlayerFactory.getmListItem() > PlayerFactory
										.getmList().size() - 1) {
									PlayerFactory.setmListItem(0);
									PlayerFactory.play();
									textView.setText(PlayerFactory
											.getFileName());
									handler.post(updateThread);
									SPButton.setText("暂停");
								} else if (PlayerFactory.getmList().size() == 0) {
									/**
									 * 
									 * 删除后如果列表为空，跳转到录音界面
									 * 
									 */
									Intent intent = new Intent(activity,
											RecoderActivity.class);
									activity.startActivity(intent);
									onDestroy();
								} else {
									// 删除后自动播放下一曲
									PlayerFactory.play();
									textView.setText(PlayerFactory
											.getFileName());
									handler.post(updateThread);
									SPButton.setText("暂停");
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}).setNegativeButton("取消", null).create().show();
		}
		return super.onOptionsItemSelected(item);
	}

}
