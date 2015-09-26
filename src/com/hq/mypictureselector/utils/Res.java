package com.hq.mypictureselector.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;


/**
 * 加载R文件里的内容
 * @author heqing
 * @date 2015年9月12日上午10:12:40
 */
public class Res {

	private static String pkgName;//文件路径名
	private static Resources resources;//R文件的对象
	
	/*
	 * 初始化文件夹路径和选项
	 */
	public static void init(Context context){
		pkgName = context.getPackageName();
		resources = context.getResources();
	}
	/**
	 * layout文件夹下xml文件的id的获取
	 * @param layoutName
	 * @return
	 */
	public static int getLayoutID(String layoutName){
		return resources.getIdentifier(layoutName, "layout", pkgName);
	}
	/**
	 * 得到控件的id
	 * @param widgetName
	 * @return
	 */
	public static int getWidgetID(String widgetName){
		return resources.getIdentifier(widgetName, "id", pkgName);
	}
	/**
	 * anim文件夹下xml文件的id的获取
	 * @param animName
	 * @return
	 */
	public static int getAnimID(String animName){
		return resources.getIdentifier(animName, "anim", pkgName);
	}
	/**
	 * 获取xml文件的id
	 * @param xmlName
	 * @return
	 */
	public static int getXmlID(String xmlName){
		return resources.getIdentifier(xmlName, "xml", pkgName);
	}
	/**
	 * 获取xml文件
	 * @param xmlName
	 * @return
	 */
	public static XmlResourceParser getXml(String xmlName){
		int xmlId = getXmlID(xmlName);
		return resources.getXml(xmlId);
	}
	
	/**
	 * 获取raw文件夹下资源的 id
	 */
	public static int getRawID(String rawName){
		return resources.getIdentifier(rawName, "raw", pkgName);
	}
	
	/**
	 * drawable文件夹下文件的id
	 * @param drawName
	 * @return
	 */
	public static int getDrawableID(String drawName){
		return resources.getIdentifier(drawName, "drawable", pkgName);
	}
	/**
	 * 获取到drawable文件
	 * @param drawName
	 * @return
	 */
	public static Drawable getDrawable(String drawName){
		int drawId = getDrawableID(drawName);
		return resources.getDrawable(drawId);
	}
	/**
	 * 获取values文件夹下的attrs.xml里的元素 的id
	 * @param attrName
	 * @return
	 */
	public static int getAttrID(String attrName){
		return resources.getIdentifier(attrName, "attr", pkgName);
	}
	/**
	 * 获取values文件夹下的dimens.xml里的元素 的id
	 * @param dimenName
	 * @return
	 */
	public static int getDimenID(String dimenName){
		return resources.getIdentifier(dimenName, "dimen", pkgName);
	}
	/**
	 * 获取values文件夹下的colors.xml里的元素 的id
	 * @param colorName
	 * @return
	 */
	public static int getColorID(String colorName){
		return resources.getIdentifier(colorName, "color", pkgName);
	}
	/**
	 * 获取values文件夹下的colors.xml里的元素
	 * @param colorName
	 * @return
	 */
	public static int getColor(String colorName){
		return resources.getColor(getColorID(colorName));
	}
	/**
	 * 获取values文件夹下的styles.xml里的元素的id
	 * @param styleName
	 * @return
	 */
	public static int getStyleID(String styleName){
		return resources.getIdentifier(styleName, "style", pkgName);
	}
	/**
	 * 获取values文件夹下的string.xml里的元素的id
	 * @param strName
	 * @return
	 */
	public static int getStringID(String strName){
		return resources.getIdentifier(strName, "string", pkgName);
	}
	/**
	 * 获取values文件夹下的string.xml里的元素
	 * @param strName
	 * @return
	 */
	public static String getString(String strName){
		return resources.getString(getStringID(strName));
	}
	/**
	 * 获取values文件夹下的integer-array元素
	 * @param arrName
	 * @return
	 */
	public static int[] getIntArray(String arrName){
		return resources.getIntArray(resources.getIdentifier(arrName, "array", pkgName));
	}
	
}
