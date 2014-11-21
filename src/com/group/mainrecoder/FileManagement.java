package com.group.mainrecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.rtp.RtpStream;
import android.os.Environment;

/**
 * @author LiuYufei 文件操作工具类 所有对文件的操作和路径获取直接通过调用本方法完成
 */
public class FileManagement {
	/**
	 * 播放器，录音器存储路径后缀
	 */
	private static String playerDir = "/Recoder/";
	/**
	 * 录音器缓存文件路径后缀
	 */
	private static String tempDir = "/Recoder/temp/";

	private static String tempname = "temp";
	private static String suffix;
	private static List<String> suffixs;
	private static SharedPreferences pref;

	public static SharedPreferences getPref() {
		return pref;
	}

	public static void setPref(SharedPreferences pref) {
		FileManagement.pref = pref;
	}

	public static String getSuffix() {
		return "." + pref.getString("recoderType", "amr");
	}

	public static List<String> getSuffixs() {
		return suffixs;
	}

	public static void setSuffixs(String[] suffixList) {
		suffixs = new ArrayList<String>();
		for (int i = 0; i < suffixList.length; i++) {
			suffixs.add(suffixList[i]);
		}
	}

	/**
	 * @return 完整播放器路径，不包括文件名
	 */
	public static String getPlayerDir() {
		return Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MUSIC).getPath()
				+ playerDir;
	}

	/**
	 * @param count
	 *            缓存文件序号
	 * @return 完整单缓存文件路径，包括带序号的文件名
	 */
	public static String getTempsfile(int count) {
		return Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MUSIC).getPath()
				+ tempDir + tempname + count + getSuffix();
	}

	/**
	 * @return 完整缓存文件路径，不包括文件名
	 */
	public static String getTempDir() {
		return Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_MUSIC).getPath()
				+ tempDir;
	}

	/**
	 * @return 录音文件详细内容列表
	 */
	public static List<FileDetail> getMusicFileList() {
		List<String> nameList = getMusicNameList();
		List<FileDetail> fileList = new ArrayList<FileDetail>();
		for (int i = 0; i < nameList.size(); i++) {
			fileList.add(new FileDetail(nameList.get(i)));
		}
		return mixFileList(fileList, KiiUtil.getOnlineFileList());
	}

	/**
	 * @return 录音文件名列表
	 */
	public static List<String> getMusicNameList() {
		List<String> nameList = null;
		File file = new File(getPlayerDir());
		File[] musics = file.listFiles();
		nameList = new ArrayList<String>();
		for (int i = 0; i < musics.length; i++) {
			for (int j = 0; j < suffixs.size(); j++) {
				if (musics[i].isFile()
						&& musics[i].getName().endsWith("." + suffixs.get(j))) {
					nameList.add(musics[i].getName());
				}
			}
		}
		return nameList;
	}

	/**
	 * 该方法仅能修改录音文件夹内的文件名
	 * 
	 * @param oldName
	 *            所需重命名文件的文件名
	 * @param newName
	 *            目标文件名
	 * @return 修改是否成功
	 */
	public static boolean renameMusicFile(String oldName, String newName) {
		File oldFile = new File(getPlayerDir() + oldName);
		File newFile = new File(getPlayerDir() + newName);
		if (newFile.exists()) {
			return false;
		}
		oldFile.renameTo(newFile);
		return true;
	}

	/**
	 * 用于结束录音后将缓存文件重命名并存储至播放文件夹 当文件重名时将自动添加后缀
	 * 
	 * @param fileName
	 *            录音文件命名
	 * @param count
	 * @return 最终存储文件名
	 */
	public static String saveTempFile(String fileName, int count,
			Activity activity) {
		File newFile = new File(getPlayerDir() + fileName + getSuffix());
		int i = 1;
		// 对文件进行重命名检查
		// 如果文件存在，则在文件名后追加(1)、(2)...
		for (i = 1; i >= 1; i++) {
			if (newFile.exists()) {
				newFile = new File(getPlayerDir() + fileName + "(" + i + ")"
						+ getSuffix());
			} else {
				break;
			}
		}
		if (count == 1) {
			File oldFile = new File(getTempsfile(count));
			oldFile.renameTo(newFile);
		} else {
			List<String> list = new ArrayList<String>();
			for (int a = 1; a <= count; a++) {
				list.add(getTempsfile(a));
			}
			getInputCollection(list, newFile, activity);

		}
		if (i > 1) {
			fileName = fileName + "(" + (i - 1) + ")" + getSuffix();
		}
		return fileName;
	}

	/**
	 * 仅可删除录音文件夹内的文件
	 * 
	 * @param fileName
	 *            需删除文件名
	 */
	public static void deleteMusicFlie(String fileName) {
		File file = new File(getPlayerDir() + fileName);
		file.delete();
	}

	/**
	 * 清空缓存文件夹，用于录音准备阶段
	 */
	public static void clearTempDir() {
		delFolder(getTempDir());
	}

	/**
	 * 删除文件夹（递归调用）
	 * 
	 * @param folderPath
	 *            文件夹绝对路径
	 */
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹下所有文件
	 * 
	 * @param path
	 *            文件夹绝对路径
	 * @return
	 */
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
				flag = true;
			}
		}
		return flag;
	}
	
	/**
	 * 将本地文件列表与在线文件列表进行混合
	 * 并标记文件状态（本地/云端/同步）
	 * @param localFileList 本地文件列表
	 * @param onlineFileList 云端文件列表
	 * @return 混合后带状态的总文件列表
	 */
	private static List<FileDetail> mixFileList(List<FileDetail> localFileList , List<FileDetail> onlineFileList) {
		List<FileDetail> list = localFileList;
		boolean needAdd = true;
		for (int i = 0; i < onlineFileList.size(); i++) {
			for (int j = 0; j < list.size(); j++) {
				needAdd =true;
				if (onlineFileList.get(i).getFileName().equals(list.get(j).getFileName())) {
					if (onlineFileList.get(i).getsizeall()==list.get(j).getsizeall()) {
						list.get(j).setStatus(2);
						needAdd =false;
						break;
					}else {
						list.get(j).setConflict(true);
						onlineFileList.get(i).setConflict(true);
						list.add(j, onlineFileList.get(i));
						needAdd =false;
						break;
					}
				}
			}
			if (needAdd) {
				list.add(onlineFileList.get(i));
			}
		}
		return list;
	}
	
	
	public static void getInputCollection(List list, File filename,
			Activity activity) {
		// 创建音频文件,合并的文件放这里
		File file1 = filename;
		FileOutputStream fileOutputStream = null;

		if (!file1.exists()) {
			try {
				file1.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			fileOutputStream = new FileOutputStream(file1, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件

		for (int i = 0; i < list.size(); i++) {
			File file = new File((String) list.get(i));
			try {
				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] myByte = new byte[fileInputStream.available()];
				// 文件长度
				int length = myByte.length;

				// 头文件
				if (i == 0) {
					while (fileInputStream.read(myByte) != -1) {
						fileOutputStream.write(myByte, 0, length);
					}
				}

				// 之后的文件，去掉头文件就可以了
				else {
					while (fileInputStream.read(myByte) != -1) {

						fileOutputStream.write(myByte, 6, length - 6);
					}
				}

				fileOutputStream.flush();
				fileInputStream.close();
				System.out.println("合成文件长度：" + file1.length());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// 结束后关闭流
		try {
			fileOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
