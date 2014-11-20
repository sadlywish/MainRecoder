package com.group.mainrecoder;

import java.util.List;

public class KiiUtil {
	
	/**
	 * KiiObject 内键值对标准对应表
	 * name:文件名
	 * uploadTime:最后上传更新时间
	 * size:文件大小
	 * time:音频时长
	*/
	
	/**
	 * 返回在线文件列表
	 * 方法内将文件信息转换为内部通用格式
	 * @return 通用格式的在线文件文件信息表
	 */
	public static List<FileDetail> getOnlineFileList(){
		return null;
	}
	
	/**
	 * @param fileName 需上传文件名
	 * @return 上传是否成功
	 */
	public static boolean uploadFile(String fileName){
		return false;
	}
	
	/**
	 * @param fileName 需下载文件名
	 * @return 下载是否成功
	 */
	public static boolean downloadFlie(String fileName){
		return false;
	}
	
	/**
	 * @param oldName 需重命名在线文件名
	 * @param newName 重命名后文件名
	 * @return 重命名是否成功
	 */
	public static boolean renameFlie(String oldName,String newName){
		return false;
	}
	

}
