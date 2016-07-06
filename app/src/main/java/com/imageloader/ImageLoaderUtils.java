
package com.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.test.MainApplication;

import java.io.File;

/**
 * @author: cgf
 * Date: 2016/7/6.
 */
public class ImageLoaderUtils {

    /**
     * 加载SDCARD资源图片
     */
    public static void loadBitmapFromSD(String url, ImageView imageView, int defResid) {
        judgeInit();
        ImageLoader.getInstance().displayImage("file://" + url, imageView, initDisplayOptions(defResid, true));
    }

    public static void loadBitmapFromSD(String url, ImageView imageView) {
        judgeInit();
        ImageLoader.getInstance().displayImage("file://" + url, imageView, initLoadOptions(true));
    }

    /**
     * 加载网络图片
     * 
     * @param url
     * @param imageView
     * @param defResid
     */
    public static void loadBitmap(String url, ImageView imageView, int defResid) {
        judgeInit();
        ImageLoader.getInstance().displayImage(url, imageView, initDisplayOptions(defResid, true));
    }

    /**
     * 加载网络图片 无默认图片
     * 
     * @param url
     * @param imageView
     */
    public static void loadBitmap(String url, ImageView imageView) {
        judgeInit();
        if (!TextUtils.isEmpty(url)) {
            ImageLoader.getInstance().displayImage(url, imageView, initLoadOptions(true));
        }
    }
    
    public static void loadBitmap(String uri, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(uri, initLoadOptions(true), listener);
    }
    
    /**
     * 加载网络图片 带回调
     * 
     * @param url
     * @param imageView
     */
    public static void loadBitmap(String url, ImageView imageView, ImageLoadingListener imageLoadingListener) {
        judgeInit();
        if (!TextUtils.isEmpty(url)) {
            if (imageLoadingListener != null) {
            	ImageLoader.getInstance().displayImage(url, imageView, initLoadOptions(true), imageLoadingListener);
			}
            else {
            	ImageLoader.getInstance().displayImage(url, imageView, initLoadOptions(true));
			}
        }
    }

    private static void judgeInit() {
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader(MainApplication.getInstance());
        }
    }

    /**
     * 加载本地图片 限制最大图片大小
     */
    public static void loadBitmap(String url, final ImageView imageView, final int defResid, int width, int height) {
        judgeInit();
        ImageSize imageSize = new ImageSize(width, height);
        ImageLoader.getInstance().loadImage(url, imageSize, initDisplayOptions(defResid, true), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    imageView.setImageBitmap(loadedImage);
                } else {
                    loadBitmapFromDrawable(defResid, imageView);
                }
            }
        });
    }

    /**
     * 加载本地图片
     * 
     * @param id
     * @param imageView
     */
    public static void loadBitmapFromDrawable(int id, ImageView imageView) {
        judgeInit();
        ImageLoader.getInstance().displayImage("drawable://" + id, imageView, initLoadOptions(false));
    }

    public static void loadBitmapFromDrawableCallBack(int id, ImageView imageView, final ImageLoaderCallBack callBack) {
        judgeInit();
        ImageLoader.getInstance().displayImage("drawable://" + id, imageView, initLoadOptions(false), new ImageLoadingListener() {

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                callBack.loadSuccess();
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                callBack.loadFaile();
            }

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }
        });
    }

    public static void loadBitmapCallBack(String url, ImageView imageView, final ImageLoaderCallBack callBack) {
        judgeInit();
        ImageLoader.getInstance().displayImage(url, imageView, initLoadOptions(false), new ImageLoadingListener() {

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                callBack.loadSuccess();
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                callBack.loadFaile();
            }

            @Override
            public void onLoadingStarted(String arg0, View arg1) {
            }
        });
    }

    public static void initImageLoader(Context context) {
//        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen
                                                   // dimensions
                .threadPoolSize(5) // default
                .threadPriority(Thread.NORM_PRIORITY - 1) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                // 设定只保存同一尺寸的图片在内存
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(200)
//                .diskCache(new UnlimitedDiscCache(cacheDir))
                // 设定缓存到SDCard目录的文件命名
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }

    /***
     * @param resId 设置默认显示的图片
     * @return
     */
    public static DisplayImageOptions initDisplayOptions(int resId, boolean cacheOnDisk) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(resId)
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId)
                .cacheInMemory(true)
                .cacheOnDisk(cacheOnDisk)
                .considerExifParams(true)
                .build();
        return options;
    }

    public static DisplayImageOptions initLoadOptions(boolean cacheOnDisk) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//不设保持默认大小
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheInMemory(true)
                .cacheOnDisk(cacheOnDisk)
                .considerExifParams(true)
                .build();
        return options;
    }
}
