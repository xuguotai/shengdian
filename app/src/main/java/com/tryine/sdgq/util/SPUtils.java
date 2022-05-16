package com.tryine.sdgq.util;

import android.content.Context;
import android.content.SharedPreferences;


import com.tryine.sdgq.config.Parameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;


public class SPUtils {
	/**
	 * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
	 * 
	 * @param context
	 * @param key
	 * @param object
	 */
	public static void put(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	public static void putConfig(Context context, String key, Object object) {

		SharedPreferences sp = context.getSharedPreferences(Parameter.cache_config,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		if (object instanceof String) {
			editor.putString(key, (String) object);
		} else if (object instanceof Integer) {
			editor.putInt(key, (Integer) object);
		} else if (object instanceof Boolean) {
			editor.putBoolean(key, (Boolean) object);
		} else if (object instanceof Float) {
			editor.putFloat(key, (Float) object);
		} else if (object instanceof Long) {
			editor.putLong(key, (Long) object);
		} else {
			editor.putString(key, object.toString());
		}

		SharedPreferencesCompat.apply(editor);
	}

	public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultValue);
	}
	
	public static int getInt(Context ctx, String key,
                             int defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static int getIntConfig(Context ctx, String key,
                                   int defaultValue, String type) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache_config, Context.MODE_PRIVATE);
		return sp.getInt(key, defaultValue);
	}

	public static void setBoolean(Context ctx, String key, boolean value) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static void putLong(Context ctx, String key, long value) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		sp.edit().putLong(key, value).commit();
	}
	
	public static long getLong(Context ctx, String key,
                               long defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.getLong(key, defaultValue);
	}

	/**
	 * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
	 * 
	 * @param context
	 * @param key
	 * @param defaultObject
	 * @return
	 */
	public static Object get(Context context, String key, Object defaultObject) {
		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		if (defaultObject instanceof String) {
			return sp.getString(key, (String) defaultObject);
		} else if (defaultObject instanceof Integer) {
			return sp.getInt(key, (Integer) defaultObject);
		} else if (defaultObject instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultObject);
		} else if (defaultObject instanceof Float) {
			return sp.getFloat(key, (Float) defaultObject);
		} else if (defaultObject instanceof Long) {
			return sp.getLong(key, (Long) defaultObject);
		}

		return null;
	}

	/**
	 * 移除某个key值已经对应的值
	 * 
	 * @param context
	 * @param key
	 */
	public static void remove(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 清除所有数据
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear();
		SharedPreferencesCompat.apply(editor);
	}

	/**
	 * 查询某个key是否已经存在
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean contains(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.contains(key);
	}

	/**
	 * 返回所有的键值对
	 * 
	 * @param context
	 * @return
	 */
	public static Map<String, ?> getAll(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.getAll();
	}

	/**
	 * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
	 * 
	 * @author zhy
	 * 
	 */
	private static class SharedPreferencesCompat {
		private static final Method sApplyMethod = findApplyMethod();

		/**
		 * 反射查找apply的方法
		 * 
		 * @return
		 */
		@SuppressWarnings({ "unchecked", "rawtypes" })
		private static Method findApplyMethod() {
			try {
				Class clz = SharedPreferences.Editor.class;
				return clz.getMethod("apply");
			} catch (NoSuchMethodException e) {
			}

			return null;
		}

		/**
		 * 如果找到则使用apply执行，否则使用commit
		 * 
		 * @param editor
		 */
		public static void apply(SharedPreferences.Editor editor) {
			try {
				if (sApplyMethod != null) {
					sApplyMethod.invoke(editor);
					return;
				}
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
			editor.commit();
		}
	}

	/**
	 * 是否登录
	 * @return
     */
	public static boolean getIsLogin(){
		return getBoolean(UIUtils.getContext(), Parameter.IS_LOGIN,false);
	}

	/**
	 * 是否登录
	 * @return
	 */
	public static boolean getIsLogin(Context mContext){
		boolean isLogin = getBoolean(UIUtils.getContext(), Parameter.IS_LOGIN,false);
		if(!isLogin){
			//PhoneLoginActivity.start(mContext);
		}
		return isLogin;
	}

	public static void saveString(String key, String value){
		SharedPreferences sp = UIUtils.getContext().getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}


	public static String getString(String key, String defValue){
		SharedPreferences sp = UIUtils.getContext().getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static String getString(String key){
		SharedPreferences sp = UIUtils.getContext().getSharedPreferences(Parameter.cache,
				Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

	public static void saveConfigString(String key, String value){
		SharedPreferences sp = UIUtils.getContext().getSharedPreferences(Parameter.cache_config,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getConfigString(String key, String defValue){
		SharedPreferences sp = UIUtils.getContext().getSharedPreferences(Parameter.cache_config,
				Context.MODE_PRIVATE);
		return sp.getString(key, defValue);
	}

	public static String getToken(){
		return getString(Parameter.TOKEN);
	}

	public static String getUserId(){
		return getString(Parameter.USER_ID);
	}
}