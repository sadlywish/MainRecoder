package com.group.mainrecoder;

import java.io.File;
import java.util.ArrayList;
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
	private static String playerDir = "/Recoder/" ;
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
	 * @return 录音文件详细内容列表
	 */
	public static List<FileDetail> getMusicFileList(){
		List<String> nameList = getMusicNameList();
		List<FileDetail> fileList = new ArrayList<FileDetail>();
		for (int i = 0; i < nameList.size(); i++) {
			fileList.add(new FileDetail(nameList.get(i)));
		}
		return fileList;
	}	
	/**
	 * @return 录音文件名列表
	 */
	public static List<String> getMusicNameList(){
		File file = new File(getPlayerDir());
		File[] musics = file.listFiles();
		List<String> nameList = new ArrayList<String>();
		for (int i = 0; i < musics.length; i++) {
			if (musics[i].isFile() && musics[i].getName().endsWith(".amr")) {
				nameList.add(musics[i].getName());
			}
		}
		return nameList;
	}
	/**
	 * 该方法仅能修改录音文件夹内的文件名
	 * @param oldName 所需重命名文件的文件名
	 * @param newName 目标文件名
	 * @return 修改是否成功
	 */
	public static boolean renameMusicFile(String oldName,String newName){
		File oldFile=new File(getPlayerDir()+"/"+oldName); 
        File newFile=new File(getPlayerDir()+"/"+newName); 
        if (newFile.exists()) {
			return false;
		}
        oldFile.renameTo(newFile);
		return true;
	}
	/**
	 * 用于结束录音后将缓存文件重命名并存储至播放文件夹
	 * 当文件重名时将自动添加后缀
	 * @param fileName 录音文件命名
	 * @return 最终存储文件名
	 */
	public static String saveTempFile(String fileName){
		File oldFile=new File(getTempPath()); 
        File newFile=new File(getPlayerDir()+"/"+fileName); 
        int i  =1;
        for (i = 1; i >=1; i++) {
        	if (newFile.exists()) {
               newFile=new File(getPlayerDir()+"/"+fileName+"("+i+")"); 
    		}else {
				break;
			}
		}
        oldFile.renameTo(newFile);
        if (i>1) {
			fileName = fileName+"("+(i-1)+")";
		}
		return fileName;
	}
	/**
	 * 用于拼接经过暂停的复数录音缓存文件，并将其重命名并转移至播放文件夹
	 * @param count 缓存计数器
	 * @param fileName 最终录音文件名
	 * @return 最终存储文件名
	 */
	public static String saveTempFlies(int count, String fileName){
		return null;
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
}
