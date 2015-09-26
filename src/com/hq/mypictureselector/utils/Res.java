package com.hq.mypictureselector.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;


/**
 * ����R�ļ��������
 * @author heqing
 * @date 2015��9��12������10:12:40
 */
public class Res {

	private static String pkgName;//�ļ�·����
	private static Resources resources;//R�ļ��Ķ���
	
	/*
	 * ��ʼ���ļ���·����ѡ��
	 */
	public static void init(Context context){
		pkgName = context.getPackageName();
		resources = context.getResources();
	}
	/**
	 * layout�ļ�����xml�ļ���id�Ļ�ȡ
	 * @param layoutName
	 * @return
	 */
	public static int getLayoutID(String layoutName){
		return resources.getIdentifier(layoutName, "layout", pkgName);
	}
	/**
	 * �õ��ؼ���id
	 * @param widgetName
	 * @return
	 */
	public static int getWidgetID(String widgetName){
		return resources.getIdentifier(widgetName, "id", pkgName);
	}
	/**
	 * anim�ļ�����xml�ļ���id�Ļ�ȡ
	 * @param animName
	 * @return
	 */
	public static int getAnimID(String animName){
		return resources.getIdentifier(animName, "anim", pkgName);
	}
	/**
	 * ��ȡxml�ļ���id
	 * @param xmlName
	 * @return
	 */
	public static int getXmlID(String xmlName){
		return resources.getIdentifier(xmlName, "xml", pkgName);
	}
	/**
	 * ��ȡxml�ļ�
	 * @param xmlName
	 * @return
	 */
	public static XmlResourceParser getXml(String xmlName){
		int xmlId = getXmlID(xmlName);
		return resources.getXml(xmlId);
	}
	
	/**
	 * ��ȡraw�ļ�������Դ�� id
	 */
	public static int getRawID(String rawName){
		return resources.getIdentifier(rawName, "raw", pkgName);
	}
	
	/**
	 * drawable�ļ������ļ���id
	 * @param drawName
	 * @return
	 */
	public static int getDrawableID(String drawName){
		return resources.getIdentifier(drawName, "drawable", pkgName);
	}
	/**
	 * ��ȡ��drawable�ļ�
	 * @param drawName
	 * @return
	 */
	public static Drawable getDrawable(String drawName){
		int drawId = getDrawableID(drawName);
		return resources.getDrawable(drawId);
	}
	/**
	 * ��ȡvalues�ļ����µ�attrs.xml���Ԫ�� ��id
	 * @param attrName
	 * @return
	 */
	public static int getAttrID(String attrName){
		return resources.getIdentifier(attrName, "attr", pkgName);
	}
	/**
	 * ��ȡvalues�ļ����µ�dimens.xml���Ԫ�� ��id
	 * @param dimenName
	 * @return
	 */
	public static int getDimenID(String dimenName){
		return resources.getIdentifier(dimenName, "dimen", pkgName);
	}
	/**
	 * ��ȡvalues�ļ����µ�colors.xml���Ԫ�� ��id
	 * @param colorName
	 * @return
	 */
	public static int getColorID(String colorName){
		return resources.getIdentifier(colorName, "color", pkgName);
	}
	/**
	 * ��ȡvalues�ļ����µ�colors.xml���Ԫ��
	 * @param colorName
	 * @return
	 */
	public static int getColor(String colorName){
		return resources.getColor(getColorID(colorName));
	}
	/**
	 * ��ȡvalues�ļ����µ�styles.xml���Ԫ�ص�id
	 * @param styleName
	 * @return
	 */
	public static int getStyleID(String styleName){
		return resources.getIdentifier(styleName, "style", pkgName);
	}
	/**
	 * ��ȡvalues�ļ����µ�string.xml���Ԫ�ص�id
	 * @param strName
	 * @return
	 */
	public static int getStringID(String strName){
		return resources.getIdentifier(strName, "string", pkgName);
	}
	/**
	 * ��ȡvalues�ļ����µ�string.xml���Ԫ��
	 * @param strName
	 * @return
	 */
	public static String getString(String strName){
		return resources.getString(getStringID(strName));
	}
	/**
	 * ��ȡvalues�ļ����µ�integer-arrayԪ��
	 * @param arrName
	 * @return
	 */
	public static int[] getIntArray(String arrName){
		return resources.getIntArray(resources.getIdentifier(arrName, "array", pkgName));
	}
	
}
