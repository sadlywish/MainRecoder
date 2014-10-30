package com.group.mainrecoder;

import java.util.ArrayList;
import java.util.List;

import android.media.MediaPlayer;

/**
 * @author LiuYufei 本类调用时需要进行实例化 请切记在用户离开播放界面时释放资源
 */
public class PlayerFactory {
	/**
	 * 播放器实例，用于执行相关工作
	 */
	static private MediaPlayer mediaPlayer = new MediaPlayer();
	/**
	 * 播放器是否已装载音频文件 用于防止部分异常和错误的产生
	 */
	static private boolean isPrepare = false;

	// 播放列表
	static private List<String> mList = new ArrayList<String>();
	// 当前播放歌曲的索引
	static int mListItem = 0;

	// 使用文件名定位列表
	/**
	 * @param fileName
	 */
	public static void selectByName(String fileName) {
		
	}

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		PlayerFactory.mediaPlayer = mediaPlayer;
	}

	public static boolean isIsprepare() {
		return isPrepare;
	}

	public static void setIsprepare(boolean isprepare) {
		PlayerFactory.isPrepare = isprepare;
	}

	/**
	 * 初始化播放器
	 * 
	 * @param path
	 *            音频文件路径
	 */
	private static void inti() {
		// 重置mPlayer
		mediaPlayer.reset();
		// 设置播放文件的路径
		mediaPlayer.setDataSource(path);
		// 准备播放
		mediaPlayer.prepare();

		isPrepare = true;
	}

	/**
	 * 播放音频，包括继续播放、初次播放
	 * 
	 * @param path
	 *            音频文件路径
	 */
	public static void play() {
		if (!isPrepare) {
			// 当播放器内没有加载音频时，加载新音频
			inti();// 初始化播放器
		}
		mediaPlayer.start();
	}

	/**
	 * 更换播放音频，用于上一曲下一曲或顺序播放 直接调用本方法，无需手动停止和初始化播放器
	 * 
	 * @param path
	 */
	public static void changePlay(String path) {

	}

	public static void pause() {
		mediaPlayer.pause();
	}

	// 下一首
	public static void nextMusic() {
		stop();
		if (++mListItem >= mList.size()) {
			mListItem = 0;
		}
		play();
	}

	// 上一首
	public static void LastMusic() {
		stop();
		if (--mListItem < 0) {
			mListItem = 0;
		}
		play();
	}

	/**
	 * 终止播放并释放播放器资源
	 */
	public static void stop() {
		// 判断是否在播放
		if (mediaPlayer.isPlaying()) 
		{
			mediaPlayer.reset(); // 重置mediaPlayer到初始化状态
			isPrepare = false; // 将播放器内容设置为空
		}
	}

	/**
	 * 当播放器准备好时，返回当前播放进度（0-10000） 当播放器未准备好时，返回0
	 * 
	 * @return 播放进度值（0-10000）
	 */
	public static int getPlayRate() {
		return 0;
	}

	/**
	 * 彻底释放该类资源，无视所在状态
	 */
	public static void release() {

	}

}