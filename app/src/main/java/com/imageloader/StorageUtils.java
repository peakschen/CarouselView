package com.imageloader;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class StorageUtils {
	/**缓存目录**/
	private static final String CAROUSEL_CACHE_DIR = "com.carousel";
	/**
	 *     获取缓存的目录
	 *     1. 判断是否有sdcard
	 *     2. 没有sdcard 直接缓存到内存
	 * @param context
	 * @return
	 */
	public static File getCacheDirectory(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
		    File tmpFile = null;
	        try {
	            tmpFile = Environment.getExternalStorageDirectory();
	        } catch (Exception e) {
	        }
	        
	        if (tmpFile != null) {
	            StringBuilder builder = new StringBuilder(tmpFile.getAbsolutePath());
	            builder.append(File.separator).append(CAROUSEL_CACHE_DIR).append(File.separator).append("cache/images");
	            return new File(builder.toString());
	        }
		}	
		return new File(com.nostra13.universalimageloader.utils.StorageUtils.getCacheDirectory(context).getPath());
	}
}
