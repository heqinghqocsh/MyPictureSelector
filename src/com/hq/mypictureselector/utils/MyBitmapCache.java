package com.hq.mypictureselector.utils;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;

import com.hq.mypictureselector.activity.SelectorActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class MyBitmapCache {

	private final String TAG = getClass().getSimpleName();
	public Handler handler = new Handler();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	private void put(String path,Bitmap bitmap){
		if (!TextUtils.isEmpty(path) && bitmap != null) {
			imageCache.put(path, new SoftReference<Bitmap>(bitmap));
		}
	}

	public void displayBitmap(ImageView iv,String thumbnailPath,
			String sourcePath,ImageCallback callback){
		if (TextUtils.isEmpty(thumbnailPath) && TextUtils.isEmpty(sourcePath)) {
			Log.i(TAG, "没有地址传进来");
			return;
		}
		String path;
		boolean isThumbnailPath;
		if (!TextUtils.isEmpty(thumbnailPath)) {
			path = thumbnailPath;
			isThumbnailPath = true;
		}else if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;
			isThumbnailPath = false;
		}else {
			return;
		}
		if (imageCache.containsKey(path)) {
			SoftReference<Bitmap> reference = imageCache.get(path);
			Bitmap bitmap = reference.get();
			if (bitmap != null) {
				if (callback != null) {
					callback.imageLoad(iv, bitmap, sourcePath);
				}
				iv.setImageBitmap(bitmap);
				return;
			}
		}
		iv.setImageBitmap(null);
		threadLoadImage(isThumbnailPath,thumbnailPath,sourcePath,iv,callback);
	}

	private void threadLoadImage(final boolean isThumbnailPath, final String thumbnailPath,
			final String sourcePath, final ImageView iv, final ImageCallback callback) {
		Thread thread = new Thread(){
			Bitmap bitmap;
			@Override
			public void run() {
				try {
					if (isThumbnailPath) {
						bitmap = BitmapFactory.decodeFile(thumbnailPath);
						if (bitmap == null) {
							bitmap = BitmapUtils.revisionImageSize(sourcePath);
						}
					}else {
						bitmap = BitmapUtils.revisionImageSize(sourcePath);
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (bitmap == null) {
					bitmap = SelectorActivity.bitmap;
				}else {
					put(isThumbnailPath ? thumbnailPath : sourcePath
							, bitmap);
				}
				if (callback != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							callback.imageLoad(iv, bitmap, sourcePath);
						}
					});
				}
			}
		};
		thread.start();
	}

	public interface ImageCallback{
		public void imageLoad(ImageView iv,Bitmap bitmap,Object...objects);
	}

}
