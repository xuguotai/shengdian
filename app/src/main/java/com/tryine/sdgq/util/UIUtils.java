package com.tryine.sdgq.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.tryine.sdgq.Application;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 */ /*
* 获取、设置控件信息 
*/ 
public class UIUtils { 
	
	/**
	 * 获取到字符数组 
	 * @param tabNames  字符数组的id
	 */
	public static String[] getStringArray(int tabNames) {
		return getResource().getStringArray(tabNames);
	}

	public static Resources getResource() {
		return Application.getApplication().getResources();
	}
	public static Context getContext(){
		return Application.getApplication();
	}
	/* 
	* 获取控件宽 
	*/ 
	public static int getWidth(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h); 
		return (view.getMeasuredWidth()); 
	} 
	/* 
	* 获取控件高 
	*/ 
	public static int getHeight(View view) {
		int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(w, h); 
		return (view.getMeasuredHeight()); 
	} 

	/* 
	* 设置控件所在的位置X，并且不改变宽高， 
	* X为绝对位置，此时Y可能归0 
	*/ 
	public static void setLayoutX(View view, int x) {
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(x,margin.topMargin, x+margin.width, margin.bottomMargin); 
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams); 
	} 
	/* 
	* 设置控件所在的位置Y，并且不改变宽高， 
	* Y为绝对位置，此时X可能归0 
	*/ 
	public static void setLayoutY(View view, int y){
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(margin.leftMargin,y, margin.rightMargin, y+margin.height); 
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams); 
	} 
	/* 
	* 设置控件所在的位置YY，并且不改变宽高， 
	* XY为绝对位置 
	*/ 
	public static void setLayout(View view, int x, int y) {
		MarginLayoutParams margin=new MarginLayoutParams(view.getLayoutParams());
		margin.setMargins(x,y, x+margin.width, y+margin.height); 
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
		view.setLayoutParams(layoutParams); 
	}

	public static void showToast(String str){
		try{
			Toast.makeText(getContext(),str, Toast.LENGTH_SHORT).show();
		}catch (Exception e){

		}
	}

	public static void showToast(String str, int length){
		try{
			Toast.makeText(getContext(),str,length).show();
		}catch (Exception e){

		}
	}

	/**
	 * 禁止EditText输入空格
	 *
	 * @param editText
	 */
	public static void setEditTextInhibitInputSpace(EditText editText) {
		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source.equals(" ")){
					return "";
				}else {
					return null;
				}
			}
		};
		editText.setFilters(new InputFilter[]{filter});
	}

	/**
	 * 禁止EditText输入特殊字符
	 *
	 * @param editText
	 */
	public static void setEditTextInhibitInputSpeChat(EditText editText) {
		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
				Pattern pattern = Pattern.compile(speChat);
				Matcher matcher = pattern.matcher(source.toString());
				if (matcher.find()){
					return "";
				}else {
					return null;
				}
			}
		};
		editText.setFilters(new InputFilter[]{filter});
	}
	/**
	 * 禁止EditText输入特殊字符和空格
	 *
	 * @param editText
	 */
	public static void setAllEditTextInhibitInputSpeChat(EditText editText) {
		InputFilter filter1 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source.equals(" ")){
					return "";
				}else {
					return null;
				}
			}
		};
		InputFilter filter2 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
				Pattern pattern = Pattern.compile(speChat);
				Matcher matcher = pattern.matcher(source.toString());
				if (matcher.find()){
					return "";
				}else {
					return null;
				}
			}
		};
		editText.setFilters(new InputFilter[]{filter1,filter2});
	}


	/**
	 * 过滤掉常见特殊字符,常见的表情
	 */
	public static void setEtFilter(EditText et) {
		if (et == null) {
			return;
		}
		//表情过滤器
		InputFilter emojiFilter = new InputFilter() {

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
				Pattern emoji = Pattern.compile(
						"[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
						Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
				Matcher emojiMatcher = emoji.matcher(source);
				if (emojiMatcher.find()) {
					return "";
				}
				return null;
			}
		};
		//特殊字符过滤器
		InputFilter specialCharFilter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				String regexStr = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
				Pattern pattern = Pattern.compile(regexStr);
				Matcher matcher = pattern.matcher(source.toString());
				if (matcher.matches()) {
					return "";
				} else {
					return null;
				}

			}
		};
		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source.equals(" ")){
					return "";
				}else {
					return null;
				}
			}
		};
		et.setFilters(new InputFilter[]{emojiFilter , specialCharFilter ,filter});
	}

	public static void showNetErorrToast(){
		try{
			Toast.makeText(getContext(),"网络异常", Toast.LENGTH_SHORT).show();
		}catch (Exception e){

		}
	}

	public static void showSystemErorrToast(){
		try{
			Toast.makeText(getContext(),"系统异常", Toast.LENGTH_SHORT).show();
		}catch (Exception e){

		}

	}

	/**
	 * 根据id获取字符串
	 */
	public static String getString(int id) {
		return getContext().getResources().getString(id);
	}

	/**
	 * 根据id获取图片
	 */
	public static Drawable getDrawable(int id) {
		return getContext().getResources().getDrawable(id);
	}

	/**
	 * 根据id获取颜色值
	 */
	public static int getColor(int id) {
		return getContext().getResources().getColor(id);
	}

	/**
	 * 获取颜色状态集合
	 */
	public static ColorStateList getColorStateList(int id) {
		return getContext().getResources().getColorStateList(id);
	}

	/**
	 * 获取String
	 */
	public static String getResourcesString(int id) {
		return getContext().getResources().getString(id);
	}

	/**
	 * 获取String
	 */
	public static String getAreaPhone(String code, String phone) {
		return code.replace("+","") + "-" + phone;
	}
	/**
	 * 根据id获取尺寸
	 */
	public static int getDimen(int id) {
		return getContext().getResources().getDimensionPixelSize(id);
	}

	/**
	 * dp转px
	 */
	public static int dip2px(float dp) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (density * dp + 0.5);
	}

	/**
	 * 解决simpleCourseChapterList返回数组类型为string的问题
	 * @param obj
	 * @return
	 */
	public static String getReplace(JSONObject obj) {
		return obj.toString().replace("simpleCourseChapterList\":\"\"","simpleCourseChapterList\":[]");
	}

	public static String getReplace(String str , JSONObject obj) {
		return obj.toString().replace(str + "\":\"\"",str+ "\":[]");
	}
	/**
	 * px转dp
	 */
	public static float px2dip(float px) {
		float density = getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}

	/**
	 * 加载布局文件
	 */
	public static View inflate(int layoutId) {
		return View.inflate(getContext(), layoutId, null);
	}
//	public static void showToastForHttpError(HttpException e){
//		Toast.makeText(UIUtils.getContext(),e.getExceptionCode() ==0? Constant.ERROR_NETWORK:Constant.ERROR_SYSTEM,Toast.LENGTH_SHORT).show();
//	}

	public static boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiNetworkInfo.isConnected()) {
			return true;
		}
		return false;
	}


}

