package com.group.mainrecoder;

public class FileDetail {
	private String fileName;// 文件名
	private long time;// 录音时长
	private long modiTime;// 最后更改时间
	private long size;// 文件大小

	/**
	 * 利用文件名获取对应信息的构造方法
	 * 
	 * @param filename
	 */
	public FileDetail(String filename) {

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
		return null;
	}

	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return 文本化的创建时间格式（yy-mm-dd-hh：mm）
	 */
	public String getModiTimeTime() {
		return null;
	}

	public void setModiTime(long modiTime) {
		this.modiTime = modiTime;
	}

	public String getSize() {
		return null;
	}

	public void setSize(long size) {
		this.size = size;
	}

}
