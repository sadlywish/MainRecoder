package com.group.mainrecoder;

import java.util.List;

import android.net.rtp.RtpStream;
import android.os.Environment;

/**
 * @author LiuYufei
 * 文件操作工具类
 * 所有对文件的操作和路径获取直接通过调用本方法完成
 */
public class FileManagement {
	/**
	 * 播放器，录音器存储路径后缀
	 */
	private static String playerDir = "/Recoder" ;
	/**
	 * 录音器缓存文件路径后缀
	 */
	private static String tempPath = "/Recoder/temp/temp.amr" ;
	/**
	 * @return 完整播放器路径，不包括文件名
	 */
	public static String getPlayerDir() {
		return Environment.DIRECTORY_MUSIC+playerDir;
	}
	/**
	 * @return 完整缓存文件路径，包括文件名
	 */
	public static String getTempPath() {
		return Environment.DIRECTORY_MUSIC+tempPath;
	} 
	/**
	 * @return 录音文件名表
	 */
	public static List<String> getMusicFileList(){
		return null;
	}
	/**
	 * 该方法仅能修改录音文件夹内的文件名
	 * @param oldName 所需重命名文件的文件名
	 * @param newName 目标文件名
	 */
	public static void renameMusicFile(String oldName,String newName){
		
	}
	/**
	 * 用于结束录音后将缓存文件重命名并存储至播放文件夹
	 * @param fileName 录音文件命名
	 */
	public static void saveTempFile(String fileName){
		
	}
	/**
	 * 用于拼接经过暂停的复数录音缓存文件，并将其重命名并转移至播放文件夹
	 * @param count 缓存计数器
	 * @param fileName 最终录音文件名
	 */
	public static void saveTempFlies(int count, String fileName){
		
	}
	/**
	 * 仅可删除录音文件夹内的文件
	 * @param fileName 需删除文件名
	 */
	public static void deleteMusicFlie(String fileName){
		
	}
	/**
	 * 清空缓存文件夹，用于录音准备阶段
	 */
	public static void clearTempDir(){
		
	}
	/**
	 * @param fileName 需分析文件
	 * @return 文件详情内容实例
	 */
	public static FileDetail getFileDetail(String fileName){
		return null;
	}
}
