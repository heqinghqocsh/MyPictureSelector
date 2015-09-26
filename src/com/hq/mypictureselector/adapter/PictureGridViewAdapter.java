package com.hq.mypictureselector.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.hq.mypictureselector.utils.ImageItem;
import com.hq.mypictureselector.utils.MyBitmapCache;
import com.hq.mypictureselector.utils.MyBitmapCache.ImageCallback;
import com.hq.mypictureselector.utils.Res;

/**
 * 这个是显示某个文件夹下的所有图片的适配器
 * @author heqing
 * @date 2015年9月12日下午3:13:13
 */
public class PictureGridViewAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<ImageItem> imageList;
	private ArrayList<ImageItem> selectedImageList;
	private DisplayMetrics metrics;
	private MyBitmapCache bitmapCache;

	public PictureGridViewAdapter(Context c,ArrayList<ImageItem> imageItems
			,ArrayList<ImageItem> selectedImageList){
		context = c;
		imageList = imageItems;
		this.selectedImageList = selectedImageList;
		bitmapCache = new MyBitmapCache();
		metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay()
		.getMetrics(metrics);
	}
	
	public void changeImageList(ArrayList<ImageItem> imageItems){
		imageList = imageItems;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return imageList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return imageList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context)
					.inflate(Res.getLayoutID("girdview_item"), parent, false);
			viewHolder.chooseToggle = (ImageView) convertView
					.findViewById(Res.getWidgetID("chooseImage"));
			viewHolder.imageView = (ImageView) convertView
					.findViewById(Res.getWidgetID("image"));
			viewHolder.toggleButton = (ToggleButton) convertView
					.findViewById(Res.getWidgetID("toggle"));
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (imageList != null && imageList.size() > position) {
			path = imageList.get(position).getImagePath();
		}else {
			path = "picture_default";
		}
		if (path.equals("picture_default")) {
			viewHolder.imageView.setImageResource(Res.getRawID("no_pictures"));
		}else {
			ImageItem imageItem = imageList.get(position);
			viewHolder.imageView.setTag(imageItem.getImagePath());
			bitmapCache.displayBitmap(viewHolder.imageView
					, imageItem.getThumbnailPath(), imageItem.getImagePath(), callback);
		}
		viewHolder.chooseToggle.setTag(position);
		viewHolder.toggleButton.setTag(position);
		viewHolder.toggleButton.setOnClickListener(new
				ToggleClickListener(viewHolder.chooseToggle));
		if (selectedImageList.contains(imageList.get(position))) {
			viewHolder.chooseToggle.setVisibility(View.VISIBLE);
			viewHolder.toggleButton.setChecked(true);
		}else {
			viewHolder.chooseToggle.setVisibility(View.GONE);
			viewHolder.toggleButton.setChecked(false);
		}
		return convertView;
	}

	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView iv, Bitmap bitmap, Object... objects) {
			if (iv != null && bitmap != null) {
				String url = (String) objects[0];
				if (url != null && url.equals((String)iv.getTag())) {
					iv.setImageBitmap(bitmap);
				}
			}
		}
	};
	
	private class ToggleClickListener implements OnClickListener{
		ImageView chooseBtn;
		public ToggleClickListener(ImageView btn){
			this.chooseBtn = btn;
		}
		@Override
		public void onClick(View v) {
			if (v instanceof ToggleButton) {
				ToggleButton toggleButton = (ToggleButton)v;
				int position = (Integer) toggleButton.getTag();
				if (imageList != null && mItemClickListener != null
						&& position < imageList.size()) {
					mItemClickListener.onItemClick(toggleButton, position
							, toggleButton.isChecked(), chooseBtn);
				}
			}
			
		}
	}
	
	public interface MyOnItemClickListener{
		public void onItemClick(ToggleButton tbtn,int position
				,boolean isChecked,ImageView chooseBtn);
	}
	
	private MyOnItemClickListener mItemClickListener;
	
	public void setmItemClickListener(MyOnItemClickListener mItemClickListener) {
		this.mItemClickListener = mItemClickListener;
	}
	
	private class ViewHolder{
		public ImageView imageView;
		public ToggleButton toggleButton;
		public ImageView chooseToggle;
	}


}
