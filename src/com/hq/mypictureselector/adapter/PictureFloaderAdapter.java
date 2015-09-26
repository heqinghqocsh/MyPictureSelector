package com.hq.mypictureselector.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hq.mypictureselector.activity.SelectorActivity;
import com.hq.mypictureselector.utils.ImageBucket;
import com.hq.mypictureselector.utils.ImageItem;
import com.hq.mypictureselector.utils.MyBitmapCache;
import com.hq.mypictureselector.utils.MyBitmapCache.ImageCallback;
import com.hq.mypictureselector.utils.Res;
/**
 * 这个是显示包含所有图片的文件夹的适配器
 * @author heqing
 * @date 2015年9月13日上午10:17:13
 */
public class PictureFloaderAdapter extends BaseAdapter{

	private final String TAG = getClass().getSimpleName();
	private Context mContext;
	private MyBitmapCache bitmapCache;
	private ImageView preSelected;
	private int prePosition;
	
	public PictureFloaderAdapter(Context c){
		mContext = c;
		bitmapCache = new MyBitmapCache();
	}
	
	@Override
	public int getCount() {
		return SelectorActivity.contentList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext)
					.inflate(Res.getLayoutID("picture_floder_popup_item"), null);
			viewHolder = new ViewHolder();
			viewHolder.item = convertView.findViewById(Res.getWidgetID("item"));
			viewHolder.cover = (ImageView) convertView
					.findViewById(Res.getWidgetID("coverImage"));
			viewHolder.selectedTip = (ImageView) convertView
					.findViewById(Res.getWidgetID("selected_tip"));
			viewHolder.floderName = (TextView) convertView
					.findViewById(Res.getWidgetID("floderName"));
			viewHolder.fileNum = (TextView) convertView
					.findViewById(Res.getWidgetID("fileCount"));
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.item.setOnClickListener(new MyClickListener(position
				, viewHolder.selectedTip));
		if (position == 0) {
			loadImage(viewHolder, 0);
			viewHolder.floderName.setText("所有图片");
			int sumNum = 0;
			for(ImageBucket b : SelectorActivity.contentList){
				sumNum += b.count;
			}
			viewHolder.fileNum.setText(sumNum+"");
		}else {
			loadImage(viewHolder, position - 1);
		}
		if (SelectorActivity.bucketName.equals(viewHolder.floderName.getText())) {
			viewHolder.selectedTip.setVisibility(View.VISIBLE);
			preSelected = viewHolder.selectedTip;
			prePosition = position;
		}else {
			viewHolder.selectedTip.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	public class MyClickListener implements OnClickListener{
		int position;
		ImageView curImageView;
		public MyClickListener(int position,ImageView cur){
			this.position = position;
			curImageView = cur;
		}
		@Override
		public void onClick(View view) {
			if(prePosition != position){
				curImageView.setVisibility(View.VISIBLE);
				preSelected.setVisibility(View.GONE);
				preSelected = curImageView;
				prePosition = position;
				if (listOnItemClick != null) {
					listOnItemClick.onItemClick(position);
				}
			}else {
				if (listOnItemClick != null) {//不用刷新数据，所以传一个 -1
					listOnItemClick.onItemClick(-1);
				}
			}
		}
	}
	
	private void loadImage(ViewHolder viewHolder,int position){
		String path;
		if (SelectorActivity.contentList.get(position).imageItems != null) {
			path = SelectorActivity.contentList.get(position)
					.imageItems.get(0).getImagePath();
			viewHolder.floderName.setText(SelectorActivity
					.contentList.get(position).bucketName);
			viewHolder.fileNum.setText(SelectorActivity
					.contentList.get(position).imageItems.size()+"");
		}else {
			path = "floder_default";
		}
		if (path.equals("floder_default")) {
			viewHolder.cover.setImageResource(Res.getDrawableID("no_pictures"));
		}else {
			ImageItem imageItem = SelectorActivity.contentList
					.get(position).imageItems.get(0);
			viewHolder.cover.setTag(imageItem.getImagePath());
			bitmapCache.displayBitmap(viewHolder.cover
					, imageItem.getThumbnailPath(), imageItem.getImagePath(), callback);
		}
	}
	
	ImageCallback callback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView iv, Bitmap bitmap, Object... objects) {
			if (iv != null && bitmap != null) {
				String url = (String) objects[0];
				if (url != null && url.equals((String)iv.getTag())) {
					iv.setImageBitmap(bitmap);
				}else {
					Log.i(TAG, "地址不匹配");
				}
			}else {
				Log.i(TAG, "bitmap 为空");
			}
		}
	};
	
	private ListOnItemClick listOnItemClick;
	
	public void setListOnItemClick(ListOnItemClick onItemClick){
		listOnItemClick = onItemClick;
	}
	
	public interface ListOnItemClick{
		public void onItemClick(int position);
	}
	
	private class ViewHolder{
		public View item;
		public ImageView cover;//封面
		public ImageView selectedTip;//选中提示
		public TextView floderName;//文件夹名称
		public TextView fileNum;//文件数量
	}

}
