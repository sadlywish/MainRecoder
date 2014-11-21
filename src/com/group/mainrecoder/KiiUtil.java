package com.group.mainrecoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.media.audiofx.Equalizer;
import android.net.Uri;
import android.os.SystemClock;
import android.widget.Toast;

import com.kii.cloud.storage.Kii;
import com.kii.cloud.storage.KiiBucket;
import com.kii.cloud.storage.KiiObject;
import com.kii.cloud.storage.callback.KiiObjectCallBack;
import com.kii.cloud.storage.callback.KiiQueryCallBack;
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
import com.kii.cloud.storage.resumabletransfer.KiiRTransferCallback;
import com.kii.cloud.storage.resumabletransfer.KiiRTransferProgressCallback;
import com.kii.cloud.storage.resumabletransfer.KiiUploader;
import com.kii.cloud.storage.resumabletransfer.StateStoreAccessException;
import com.kii.cloud.storage.resumabletransfer.SuspendedException;
import com.kii.cloud.storage.resumabletransfer.TerminatedException;

public class KiiUtil {

	/**
	 * KiiObject 内键值对标准对应表 
	 * name:文件名 
	 * uploadTime:最后上传更新时间
	 * size:文件大小 
	 * time:音频时长
	 */

	/**
	 * 返回在线文件列表 方法内将文件信息转换为内部通用格式
	 * 
	 * @return 通用格式的在线文件文件信息表
	 */
	public static List<FileDetail> getOnlineFileList() {
		KiiBucket bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		KiiQueryResult<KiiObject> result = null;
		try {
			result = bucket.query(new KiiQuery());
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
		}
		List<KiiObject> objects = result.getResult();
		List<FileDetail> details = new ArrayList<FileDetail>();
		for (int i = 0; i < objects.size(); i++) {
			details.add(new FileDetail(objects.get(i)));
		}
		System.out.println(details.size()+"ge");
		return details;
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
		object.set("uploadTime", SystemClock.currentThreadTimeMillis());
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
		Toast.makeText(activity, "同步完成", Toast.LENGTH_SHORT);
		return true;
	}

	/**
	 * @param fileName
	 *            需下载文件名
	 * @return 下载是否成功
	 */
	public static boolean downloadFlie(final Context activity, String fileName) {
		System.out.println("1");
		Builder builder = new Builder(activity);
//		final AlertDialog dialog = builder.setTitle("下载中").setMessage("请稍等")
//				.setCancelable(false).show();
		
		Toast.makeText(activity, "下载"+fileName+"，请稍候", Toast.LENGTH_SHORT).show();
		KiiObject object = null;
		try {
			object = KiiObject.createByUri(Uri
					.parse((findOnlineFileByName(fileName))));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("1");
		try {
			object.refresh();
		} catch (BadRequestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnauthorizedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ForbiddenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ConflictException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UndefinedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("1");
		KiiDownloader downloader = null;
		try {
			downloader = object.downloader(
					activity,
					new File(FileManagement.getPlayerDir()
							+ object.getString("name")));
		} catch (InvalidHolderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		System.out.println("1");
		downloader.transferAsync(new KiiRTransferCallback() {


			@Override
			public void onStart(KiiRTransfer operator) {
				// TODO Auto-generated method stub
				super.onStart(operator);
				
			}

			@Override
			public void onTransferCompleted(KiiRTransfer operator, Exception e) {
				// TODO Auto-generated method stub
				super.onTransferCompleted(operator, e);
//				dialog.dismiss();
				Toast.makeText(activity, "同步完成", Toast.LENGTH_SHORT).show();
			}
		});
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
		if (isOnlineFileHaveSameName(newName)) {
			return false;
		}
		KiiObject object = KiiObject.createByUri(Uri.parse(findOnlineFileByName(oldName)));
		try {
			object.refresh();
		} catch (BadRequestException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (UnauthorizedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (ForbiddenException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (ConflictException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (NotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (UndefinedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		object.set("name", newName);
		try {
			object.save();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ForbiddenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (UnauthorizedException e) {
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
		}
		return true;
	}

	/**
	 * 根据文件名寻找完整在线文件信息
	 * 
	 * @param fileName
	 *            待查查找的文件名
	 * @return 完整信息
	 */
	public static FileDetail findOnlineFileDedailByName(String fileName) {
		KiiBucket bucket = null;
		bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		KiiQuery query = null;
		try {
			query = new KiiQuery(KiiClause.equals("name", fileName));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<KiiObject> list = null;
		try {
			list = bucket.query(query).getResult();
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
		}
		return new FileDetail(list.get(0));
	}

	/**
	 * 根据文件名寻找完整在线文件uri
	 * 
	 * @param fileName
	 *            待查查找的文件名
	 * @return 字符串化URI
	 */
	public static String findOnlineFileByName(String fileName) {
		KiiBucket bucket = null;
		try {
			bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		KiiQuery query = null;
		try {
			query = new KiiQuery(KiiClause.equals("name", fileName));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final List<String> list = new ArrayList<String>();

		List<KiiObject> result =null;
		try {
			result = bucket.query(query).getResult();
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
		}
		if (result.size()<1) {
			return null;
		}
		return result.get(0).toUri().toString();
	}
	/**
	 * 根据文件名寻找完整在线文件uri
	 * 
	 * @param fileName
	 *            待查查找的文件名
	 * @return 文件名是否存在
	 */
	public static boolean isOnlineFileHaveSameName(String fileName) {
		KiiBucket bucket = null;
		try {
			bucket = Kii.bucket(AppConstants.APP_BUCKET_NAME);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return true;
		}
		KiiQuery query = null;
		try {
			query = new KiiQuery(KiiClause.equals("name", fileName));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return true;
		}
		final List<String> list = new ArrayList<String>();

		List<KiiObject> result =null;
		try {
			result = bucket.query(query).getResult();
		} catch (BadRequestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (UnauthorizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (ForbiddenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (ConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (UndefinedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;
		}
		if (list.size()>0) {
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean deleteOnlineFile(String fileName) {
		KiiObject object  = KiiObject.createByUri(Uri.parse(findOnlineFileByName(fileName)));
		try {
			object.delete();
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
		}
		return true;
	}
}
