package com.group.mainrecoder;

import java.io.File;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;

public class RecoderFactory {
	private static MediaRecorder mediaRecorder;
	private static long baseTime = 0;
	private static long timestep = 0;
	private static boolean isRecoding = false;

	public static long getTimestep() {
		return timestep;
	}

	public static void setTimestep(long timestep1) {
		timestep = timestep1;
	}

	public static boolean isRecoding() {
		return isRecoding;
	}

	public static void setRecoding(boolean isRecoding1) {
		isRecoding = isRecoding1;
	}

	public static long getBaseTime() {
		return baseTime;
	}

	public static void setBaseTime(long basetime) {
		RecoderFactory.baseTime = basetime;
	}

	public static MediaRecorder getRecoder() {
		return mediaRecorder;
	}

	public static void start() {
		try {
			String pathStr = Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_MUSIC).getPath()
					+ "/test.amr";
			File file = new File(pathStr);
			file.mkdirs();
			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				// 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
				file.delete();
				System.out.println("delete");
			}
			mediaRecorder = new MediaRecorder();
			// 设置音频录入源
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置录制音频的输出格式
			mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
			// 设置音频的编码格式
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
			// 创建一个临时的音频输出文件
			// File audioFile = File.createTempFile("record_", ".amr");

			// 第4步：指定音频输出文件
			mediaRecorder.setOutputFile(file.getAbsolutePath());

			// 准备、开始
			mediaRecorder.prepare();
			mediaRecorder.start();
			setBaseTime(SystemClock.elapsedRealtime());
			setTimestep(1);
			setRecoding(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stop() {
		setTimestep(0);
		setRecoding(false);
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
	}

	public static void pause() {
		setTimestep(SystemClock.elapsedRealtime() - baseTime);
	}

}
