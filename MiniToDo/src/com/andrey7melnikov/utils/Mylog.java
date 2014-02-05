package com.andrey7melnikov.utils;

import android.util.Log;
/**
 * Используется для логирования, измените настройки в классе
 * @author andrey7melnikov
 * 
 */

public class Mylog {
	
	static boolean enable_log = true;
	static String defaultTag = "mylog";
	
	
	static boolean enable_trace = false; // не реализовано
	
	/**
	 * a - пишем лог, в Log.d, tag = defaultTag
	 * @param log записываемый лог
	 */

	public static  void a(String log) {
		if (enable_log) Log.d(defaultTag, log);
	}
	
	/**
	 * e - пишем лог, в Log.e, tag = defaultTag
	 * @param log записываемый лог
	 */
	public static  void e(String log) {
		if (enable_log) Log.e(defaultTag, log);
	}
	
	
	/**
	 * e - пишем лог, в Log.e, tag = первый параметр
	 * @param log записываемый лог
	 *  @param tag таг
	 */
	public static  void a(String tag,String log) {
		if (enable_log) Log.d(tag, log);
	}

}
