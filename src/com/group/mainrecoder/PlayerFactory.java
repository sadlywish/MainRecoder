package com.group.mainrecoder;

import android.media.MediaPlayer;

/**
 * @author LiuYufei
 * 本类调用时需要进行实例化
 * 请切记在用户离开播放界面时释放资源
 */
public class PlayerFactory {
	/**
	 * 播放器实例，用于执行相关工作
	 */
	static private MediaPlayer mediaPlayer=new MediaPlayer();
	/**
	 * 播放器是否已装载音频文件
	 * 用于防止部分异常和错误的产生
	 */
	static private boolean isprepare = false;

	public static MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public static void setMediaPlayer(MediaPlayer mediaPlayer) {
		PlayerFactory.mediaPlayer = mediaPlayer;
	}
	
	public static boolean isIsprepare() {
		return isprepare;
	}

	public static void setIsprepare(boolean isprepare) {
		PlayerFactory.isprepare = isprepare;
	}

	/**
	 * 初始化播放器
	 * @param path 音频文件路径 
	 */
	private void inti(String path){
		
	}
	/**
	 * 播放音频，包括继续播放、初次播放
	 * @param path 音频文件路径
	 */
	public void play(String path){
		
	}
	/**
	 * 更换播放音频，用于上一曲下一曲或顺序播放
	 * 直接调用本方法，无需手动停止和初始化播放器
	 * @param path
	 */
	public void changePlay(String path){
		
	}
	public void pause(){
		
	}
	/**
	 * 终止播放并释放播放器资源
	 */
	public void stop(){
		
	}
	/**
	 * 当播放器准备好时，返回当前播放进度（0-10000）
	 * 当播放器未准备好时，返回0
	 * @return 播放进度值（0-10000）
	 */
	public int getPlayRate(){
		return 0;
	}
	/**
	 * 彻底释放该类资源，无视所在状态
	 */
	public void release(){
		
	}
	
}
