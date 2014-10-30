package com.group.mainrecoder;

public class FileDetail {
	private String fileName;
	private long time;
	private long creatTime;
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
	public String getCreatTime() {
		return null;
	}
	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}
	

}
