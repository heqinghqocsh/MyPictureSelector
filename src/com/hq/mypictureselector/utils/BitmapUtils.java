package com.hq.mypictureselector.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {
	
	public static Bitmap revisionImageSize(String path) throws IOException{
		File file = new File(path);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(bis, null, options);
		bis.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 256) && (options.outHeight >> i <= 256)) {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(bis,null,options);
				break;
			}
			i++;
		}
		return bitmap;
	}
	

}
