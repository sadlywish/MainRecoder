package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.media.audiofx.Equalizer;
import android.os.SystemClock;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.exception.app.BadRequestException;
import com.kii.cloud.storage.exception.app.ConflictException;
import com.kii.cloud.storage.exception.app.ForbiddenException;
import com.kii.cloud.storage.exception.app.NotFoundException;
import com.kii.cloud.storage.exception.app.UnauthorizedException;
import com.kii.cloud.storage.exception.app.UndefinedException;
import com.kii.cloud.storage.query.KiiClause;
import com.kii.cloud.storage.query.KiiQuery;
import com.kii.cloud.storage.query.KiiQueryResult;
import com.kii.cloud.storage.resumabletransfer.AlreadyStartedException;
import com.kii.cloud.storage.resumabletransfer.InvalidHolderException;
import com.kii.cloud.storage.resumabletransfer.KiiDownloader;
import com.kii.cloud.storage.resumabletransfer.KiiRTransfer;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferProgressCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;
import com.kii.cloud.storage.resumabletransfer.StateStoreAccessException;
import com.kii.cloud.storage.resumabletransfer.SuspendedException;
import com.kii.cloud.storage.resumabletransfer.TerminatedException;

public class KiiUtil {

	/**
	 * KiiObject 内键值对标准对应表 name:文件名 uploadTime:最后上传更新时间 size:文件大小 time:音频时长
	 */

	/**
	 * 返回在线文件列表 方法内将文件信息转换为内部通用格式
	 * 
	 * @return 通用格式的在线文件文件信息表
	 */
	public static List<FileDetail> getOnlineFileList() {
		return null;
	}

	/**
	 * @param fileName
	 *            需上传文件名
	 * @return 上传是否成功
	 */
	public static boolean uploadFile(Context activity, String fileName) {
		FileDetail fileInfo = new FileDetail(fileName);
		// TODO Auto-generated method stub
		KiiBucket bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		KiiObject object = bucket.object();
		object.set("name", fileInfo.getFileName());
		object.set("uploadTime", SystemClock.elapsedRealtime());
		object.set("size", fileInfo.getsizeall());
		object.set("time", fileInfo.getTime());
		File baseFile = new File(FileManagement.getPlayerDir() + fileName);
		KiiUploader uploader = object.uploader(activity, baseFile);
		try {
			uploader.transfer(new KiiRTransferProgressCallback() {

				@Override
				public void onProgress(KiiRTransfer arg0, long arg1, long arg2) {
					// TODO Auto-generated method stub

				}
			});
		} catch (AlreadyStartedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SuspendedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (TerminatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (StateStoreAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param fileName
	 *            需下载文件名
	 * @return 下载是否成功
	 */
	public static boolean downloadFlie(Context activity, String fileName) {
		KiiObject object = findOnlineFileByName(fileName);
		System.out.println("1");
		try {
			object.refresh();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ForbiddenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (UndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("1");
		KiiDownloader downloader =null;
		try {
			downloader = object.downloader(activity, new File(
					FileManagement.getPlayerDir() + object.getString("name")));
		} catch (InvalidHolderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("1");
		try {
			downloader.transfer(new KiiRTransferProgressCallback() {
				
				@Override
				public void onProgress(KiiRTransfer arg0, long arg1, long arg2) {
					// TODO Auto-generated method stub
					
				}
			});
		} catch (AlreadyStartedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (SuspendedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (TerminatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (StateStoreAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param oldName
	 *            需重命名在线文件名
	 * @param newName
	 *            重命名后文件名
	 * @return 重命名是否成功
	 */
	public static boolean renameFlie(String oldName, String newName) {
		return false;
	}

	/**
	 * 根据文件名寻找完整在线文件信息
	 * 
	 * @param fileName
	 *            待查查找的文件名
	 * @return 完整信息
	 */
	public static FileDetail findOnlineFileDedailByName(String fileName) {
		return null;
	}

	/**
	 * 根据文件名寻找完整在线文件信息
	 * 
	 * @param fileName
	 *            待查查找的文件名
	 * @return 完整信息
	 */
	public static KiiObject findOnlineFileByName(String fileName) {
		KiiBucket bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		KiiQuery query=null;
		try {
			query = new KiiQuery(KiiClause.equals("name", fileName));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		KiiQueryResult<KiiObject> result = null;
		try {
			result = bucket.query(query);
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ForbiddenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		List<KiiObject> list = result.getResult();
		return list.get(0);
	}
}
