package com.group.mainrecoder;

import java.io.File;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class RecoderFactory {
	private static MediaRecorder mediaRecorder;
	private static long baseTime = 0;
	private static long timestep = 0;
	private static boolean isRecoding = false;
	private static int count = 1;//记录暂停数
	private static SharedPreferences pref = null;
	private static testInterface interface1;
	
	
	
	public static testInterface getInterface1() {
		return interface1;
	}

	public static void setInterface1(testInterface interface1) {
		RecoderFactory.interface1 = interface1;
	}

	public static SharedPreferences getPref() {
		return pref;
	}

	public static void setPref(SharedPreferences prefs) {
		pref = prefs;
	}

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
/*		try {
			interface1.test();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		try {
			String pathStr = FileManagement.getTempsfile(count);
			File file = new File(pathStr);
			file.mkdirs();
//			System.out.println(file.getAbsolutePath());
			if (file.exists()) {
				// 如果文件存在，删除它，演示代码保证设备上只有一个录音文件
				file.delete();
//				System.out.println("delete");
			}
			mediaRecorder = new MediaRecorder();
			// 设置音频录入源
			mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置录制音频的输出格式
			if (pref.getString("recoderType", null).equals("amr")) {
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);	
			}
			if (pref.getString("recoderType", null).equals("3gp")) {
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			}

			// 设置音频的编码格式
			mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 创建一个临时的音频输出文件
			// File audioFile = File.createTempFile("record_", ".amr");

			// 第4步：指定音频输出文件
			mediaRecorder.setOutputFile(file.getAbsolutePath());

			// 准备、开始
			mediaRecorder.prepare();
			mediaRecorder.start();
			setBaseTime(SystemClock.elapsedRealtime());
			if (count ==1) {
				setTimestep(1);
			}
			setRecoding(true);
			count++;
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
		setTimestep(getTimestep()+SystemClock.elapsedRealtime() - baseTime);
		setRecoding(false);
		mediaRecorder.stop();
		mediaRecorder.release();
		mediaRecorder = null;
	}

	public static String save (String fileName , Activity activity){
		String finalName = FileManagement.saveTempFile(fileName, count, activity);
		FileManagement.clearTempDir();
		count = 0;
		return finalName;
	}
	public static void cancel(){
		FileManagement.clearTempDir();
		count = 0;
	}
	public static boolean ispause(){
		if (count>1) {
			return true;
		}
		return false;
	}
	
	public interface testInterface{
		public void test ();
	}
}
