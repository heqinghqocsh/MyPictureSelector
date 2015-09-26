package com.hq.mypictureselector.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.hq.mypictureselector.adapter.PictureFloaderAdapter;
import com.hq.mypictureselector.adapter.PictureFloaderAdapter.ListOnItemClick;
import com.hq.mypictureselector.adapter.PictureGridViewAdapter;
import com.hq.mypictureselector.adapter.PictureGridViewAdapter.MyOnItemClickListener;
import com.hq.mypictureselector.utils.AlbumHelper;
import com.hq.mypictureselector.utils.ImageBucket;
import com.hq.mypictureselector.utils.ImageItem;
import com.hq.mypictureselector.utils.PublicWay;
import com.hq.mypictureselector.utils.Res;
/**
 * 选择图片的主activity(显示相应文件夹下的所有图片)
 * @author heqing
 * @date 2015年9月12日上午10:05:02
 */
public class SelectorActivity extends Activity {

	public final String TAG = getClass().getSimpleName();
	
	//显示所有图片的列表控件
	private GridView gridView;
	private TextView noPictureTip;//没有图片时 的提示
	private PictureGridViewAdapter gridPictureAdapter;//GridView的 adapter
	private ImageView go_back;//返回按钮
	private TextView finishBtn;//完成按钮
	private TextView floderTip;//选择文件夹的spinner
	
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper albumHelper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	public static String bucketName = "所有图片";
	
	private PopupWindow popupWindow;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Res.init(this);
        setContentView(Res.getLayoutID("activity_selector"));
        mContext = this;
        bitmap = BitmapFactory.decodeResource(getResources()
        		, Res.getDrawableID("no_pictures"));
        findView();
        init();
        initPopupWindow();
        initGridViewListener();
    }

	private void findView() {
		noPictureTip = (TextView) findViewById(Res.getWidgetID("noPictureTip"));
		go_back = (ImageView) findViewById(Res.getWidgetID("go_back"));
		go_back.setOnClickListener(backListener);
		finishBtn = (TextView) findViewById(Res.getWidgetID("finishBtn"));
		finishBtn.setOnClickListener(finishClickListener);
		gridView = (GridView) findViewById(Res.getWidgetID("picture_gridview"));
		floderTip = (TextView) findViewById(Res.getWidgetID("floderTip"));
		floderTip.setOnClickListener(floderSelectedListener);
		floderTip.setText(bucketName);
	}
	
	//完成按钮监听器
	OnClickListener finishClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Log.i(TAG, "所选图片文件的信息存放在 PublicWay 类中的静态变量  tempSelectedItem 中。");
			Log.i(TAG, "使用完 tempSelectedItem 之后记得清空该变量");
			finish();
		}
	};

	//返回按钮监听器
	OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			PublicWay.tempSelectedItem.clear();
			finish();
		}
	};
	
	OnClickListener floderSelectedListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}else {
				popupWindow.setAnimationStyle(Res.getStyleID("popup_window_anim"));
				popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
			}
		}
	};
	
	private void init() {
		albumHelper = AlbumHelper.getInstance();
		albumHelper.init(getApplicationContext());
		contentList = albumHelper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageItems);
		}
		gridPictureAdapter = new PictureGridViewAdapter(this,dataList
				,PublicWay.tempSelectedItem);
		gridView.setAdapter(gridPictureAdapter);
		gridView.setEmptyView(noPictureTip);
	}
	
	private void initPopupWindow(){
		popupWindow = new PopupWindow(mContext);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View defaultView = inflater.inflate(Res.getLayoutID("picture_floder_popup"), null);
		defaultView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		popupWindow.setWidth(LayoutParams.MATCH_PARENT);
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setTouchable(true);
		ListView floderListView = (ListView) defaultView.findViewById(Res.getWidgetID("floder_list"));
		PictureFloaderAdapter floaderAdapter = new PictureFloaderAdapter(mContext);
		floderListView.setAdapter(floaderAdapter);
		popupWindow.setContentView(defaultView);
		ListOnItemClick onItemClick = new ListOnItemClick() {
			@Override
			public void onItemClick(int position) {
				popupWindow.dismiss();
				if (-1 == position) {//如果是-1 不用刷新数据
					return;
				}
				if (0 == position) {
					bucketName = "所有图片";
					floderTip.setText("所有图片");
					gridPictureAdapter.changeImageList(dataList);
				}else {
					floderTip.setText(contentList.get(position - 1).bucketName);
					bucketName = contentList.get(position - 1).bucketName;
					ArrayList<ImageItem> imageItems = (ArrayList<ImageItem>) 
							contentList.get(position - 1).imageItems;
					gridPictureAdapter.changeImageList(imageItems);
				}
			}
		};
		floaderAdapter.setListOnItemClick(onItemClick);
	}
	
	
	
	private void initGridViewListener(){
		MyOnItemClickListener onItemClickListener = new MyOnItemClickListener() {
			@Override
			public void onItemClick(ToggleButton tbtn, int position, boolean isChecked,
					ImageView chooseBtn) {
				if (PublicWay.tempSelectedItem.size() >= PublicWay.max) {
					tbtn.setChecked(false);
					chooseBtn.setVisibility(View.GONE);
					if (!removeOneData(dataList.get(position))) {
						Toast.makeText(mContext, String.format(Res.getString("only_choose_num")
								, PublicWay.max), Toast.LENGTH_SHORT).show();
					}
					return ;
				}
				if (isChecked) {
					chooseBtn.setVisibility(View.VISIBLE);
					PublicWay.tempSelectedItem.add(dataList.get(position));
					finishBtn.setText(Res.getString("finish")+"("
					+PublicWay.tempSelectedItem.size()+"/"+PublicWay.max+")");
				}else {
					PublicWay.tempSelectedItem.remove(dataList.get(position));
					chooseBtn.setVisibility(View.GONE);
					finishBtn.setText(Res.getString("finish")+"("
							+PublicWay.tempSelectedItem.size()+"/"+PublicWay.max+")");
				}
				if (PublicWay.tempSelectedItem.size() > 0) {
					finishBtn.setEnabled(true);
					finishBtn.setClickable(true);
				}
			}
		};
		gridPictureAdapter.setmItemClickListener(onItemClickListener);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
			}else {
				PublicWay.tempSelectedItem.clear();
				finish();
			}
		}
		return true;
	}
	
	private boolean removeOneData(ImageItem item){
		if (PublicWay.tempSelectedItem.contains(item)) {
			PublicWay.tempSelectedItem.remove(item);
			finishBtn.setText(Res.getString("finish")+"("
			+PublicWay.tempSelectedItem.size()+"/"+PublicWay.max+")");
			return true;
		}
		return false;
	}
}
