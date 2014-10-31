package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.media.MediaPlayer;
import android.net.Uri;

public class FileDetail {
	private String fileName;// 文件名
	private long time;// 录音时长
	private long modiTime;// 最后更改时间
	private long size;// 文件大小
	private String[] suffix = {"B","KB","MB","GB","TB"};

	/**
	 * 利用文件名获取对应信息的构造方法
	 * 
	 * @param filename
	 */
	public FileDetail(String filename) {
		File file = new File(Uri.encode(FileManagement.getPlayerDir()+filename));
		this.fileName = filename;
		this.modiTime = file.lastModified();
		this.size = file.length();
		MediaPlayer mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(file.getAbsolutePath());
			mediaPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.time = mediaPlayer.getDuration();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getTime() {
		return time;
	}

	/**
	 * @return 字符串化的录音时长
	 */
	public String getSringTime() {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		return df.format(new Date(time));
	}

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return 文本化的创建时间格式（yyyy/mm/dd-hh：mm）
	 */
	public String getModiTimeTime() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd-HH:mm");
		return df.format(new Date(time));
	}

	public void setModiTime(long modiTime) {
		this.modiTime = modiTime;
	}

	public String getSize() {
		double output = this.size;
		int i =0;
		for (i = 0; i < suffix.length; i++) {
			if (output>1024f) {
				output/=1024f;
			}else {
				break;
			}
		}
		DecimalFormat df = new DecimalFormat("#.000");
		return df.format(output)+" "+suffix[i];
	}

	public void setSize(long size) {
		this.size = size;
	}

}
