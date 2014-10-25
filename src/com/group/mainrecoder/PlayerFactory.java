package com.group.mainrecoder;

import android.media.MediaPlayer;

/**
 * @author LiuYufei
 *
 */
public class PlayerFactory {
	static private MediaPlayer mediaPlayer=new MediaPlayer();
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
	public void inti(String path){
		
	}
	public void play(String path){
		
	}
	public void pause(){
		
	}
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
	
}
