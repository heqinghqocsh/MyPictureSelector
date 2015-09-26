package com.hq.mypictureselector.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
/**
 * 通过查询数据库获取图片 的信息
 * @author heqing
 * @date 2015年9月12日下午2:52:59
 */
public class AlbumHelper {

	final String TAG = getClass().getSimpleName();
	Context context;
	ContentResolver contentResolver;
	HashMap<String, String> thumbnailMap = new HashMap<String, String>();
	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String,String>>();
	HashMap<String, ImageBucket> bucketMap = new HashMap<String, ImageBucket>();
	
	private static AlbumHelper helper;
	
	private AlbumHelper(){
	}
	
	public static AlbumHelper getInstance(){
		if (helper == null) {
			helper = new AlbumHelper();
		}
		return helper;
	}
	
	public void init(Context context){
		if (this.context == null) {
			this.context = context;
			contentResolver = context.getContentResolver();
		}
	}
	
	public void getThumbnail(){
		String[] projection = {Thumbnails._ID,Thumbnails.IMAGE_ID,Thumbnails.DATA};
		Cursor cursor = contentResolver.query(Thumbnails.EXTERNAL_CONTENT_URI
				, projection, null, null, null);
		getThumbnailColumnData(cursor);
	}

	private void getThumbnailColumnData(Cursor cursor) {
		if (cursor.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
			int image__idColumn = cursor.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
			do {
				_id = cursor.getInt(_idColumn);
				image_id = cursor.getInt(image__idColumn);
				image_path = cursor.getString(dataColumn);
				thumbnailMap.put(""+image_id, image_path);
			} while (cursor.moveToNext());
		}
	}
	
	private void getAlbum(){
		String[] projection = {Albums._ID,Albums.ALBUM,Albums.ALBUM_ART
				,Albums.ALBUM_KEY,Albums.ARTIST,Albums.NUMBER_OF_SONGS};
		Cursor cursor = contentResolver.query(Albums.EXTERNAL_CONTENT_URI, projection
				, null, null, null);
		getAlbumColumnData(cursor);
	}

	private void getAlbumColumnData(Cursor cursor) {
		if (cursor.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;
			
			int _idColumn = cursor.getColumnIndex(Albums._ID);
			int albumColumn = cursor.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cursor.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cursor.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cursor.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cursor.getColumnIndex(Albums.NUMBER_OF_SONGS);
			do {
				_id = cursor.getInt(_idColumn);
				album = cursor.getString(albumColumn);
				albumArt = cursor.getString(albumArtColumn);
				albumKey = cursor.getString(albumKeyColumn);
				artist = cursor.getString(artistColumn);
				numOfSongs = cursor.getInt(numOfSongsColumn);
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id+"");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs+"");
				albumList.add(hash);
			} while (cursor.moveToNext());
		}
	}
	
	boolean hasBuildImagesBucketList = false;
	void buildImagesBucketList(){
		long startTime = System.currentTimeMillis();
		getThumbnail();
		String columns[] = new String[]{Media._ID,Media.BUCKET_ID,Media.PICASA_ID
				,Media.DATA,Media.DISPLAY_NAME,Media.TITLE,Media.SIZE
				,Media.BUCKET_DISPLAY_NAME};
		Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI
				, columns, null, null, null);
		if (cursor.moveToFirst()) {
			int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cursor.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cursor.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(
					Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cursor.getColumnIndexOrThrow(Media.PICASA_ID);
			int totalNum = cursor.getCount();
			do {
				String _id = cursor.getString(photoIDIndex);
				String name = cursor.getString(photoNameIndex);
				String path = cursor.getString(photoPathIndex);
				String title = cursor.getString(photoTitleIndex);
				String size = cursor.getString(photoSizeIndex);
				String bucketName = cursor.getString(bucketDisplayNameIndex);
				String bucketId = cursor.getString(bucketIdIndex);
				String picasaId = cursor.getString(picasaIdIndex);
				ImageBucket bucket = bucketMap.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					bucketMap.put(bucketId, bucket);
					bucket.imageItems = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				ImageItem imageItem = new ImageItem();
				imageItem.setImageId(_id);
				imageItem.setImagePath(path);
				imageItem.setThumbnailPath(thumbnailMap.get(_id));
				bucket.imageItems.add(imageItem);
			} while (cursor.moveToNext());
		}
		hasBuildImagesBucketList = true;
		long endTime = System.currentTimeMillis();
		Log.i(TAG, "准备工作一共耗时："+(endTime - startTime)+" ms");
	}
	
	public List<ImageBucket> getImagesBucketList(boolean refresh){
		if (refresh || (!hasBuildImagesBucketList)) {
			buildImagesBucketList();
		}
		List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
		Iterator<Entry<String, ImageBucket>> itr = bucketMap
				.entrySet().iterator();
		while(itr.hasNext()){
			tmpList.add(itr.next().getValue());
		}
		return tmpList;
	}
	
	public String getOriginalImagePath(String image_id){
		String path = null;
		String[] projection = {Media._ID,Media.DATA};
		Cursor cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection
				, Media._ID+"="+image_id, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));
		}
		return path;
	}
	
	
	
	
	
	
	
	
	
	
	
}
