package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
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
	private String[] suffix = { "B", "KB", "MB", "GB", "TB" };

	/**
	 * 利用文件名获取对应信息的构造方法
	 * 
	 * @param filename
	 */
	public FileDetail(String filename)  {
		File file = new File(FileManagement.getPlayerDir() + filename);
		this.fileName = filename;
		this.modiTime = file.lastModified();
		this.size = file.length();
		//			this.time = this.getAmrDuration(file) + 57600000;
		try {
			this.time = this.getDuration(file)+576000000;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		return df.format(new Date(modiTime));
	}

	public void setModiTime(long modiTime) {
		this.modiTime = modiTime;
	}

	public String getSize() {
		double output = this.size;
		int i = 0;
		for (i = 0; i < suffix.length; i++) {
			if (output > 1024f) {
				output /= 1024f;
			} else {
				break;
			}
		}
		DecimalFormat df = new DecimalFormat("#.000");
		return df.format(output) + " " + suffix[i];
	}

	public long getDuration(File file) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		// 重置mPlayer
		mediaPlayer.reset();
		// 设置播放文件的路径
		try {
			mediaPlayer.setDataSource(file.getAbsolutePath());
			// 准备播放
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
		long time =  mediaPlayer.getDuration();
		mediaPlayer.release();
		mediaPlayer = null;
		return time;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
				0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			// ///////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			// ///////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return duration;
	}
}
