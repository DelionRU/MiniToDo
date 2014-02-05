package com.andrey7melnikov.utils;

import android.util.Log;
/**
 * ������������ ��� �����������, �������� ��������� � ������
 * @author andrey7melnikov
 * 
 */

public class Mylog {
	
	static boolean enable_log = true;
	static String defaultTag = "mylog";
	
	
	static boolean enable_trace = false; // �� �����������
	
	/**
	 * a - ����� ���, � Log.d, tag = defaultTag
	 * @param log ������������ ���
	 */

	public static  void a(String log) {
		if (enable_log) Log.d(defaultTag, log);
	}
	
	/**
	 * e - ����� ���, � Log.e, tag = defaultTag
	 * @param log ������������ ���
	 */
	public static  void e(String log) {
		if (enable_log) Log.e(defaultTag, log);
	}
	
	
	/**
	 * e - ����� ���, � Log.e, tag = ������ ��������
	 * @param log ������������ ���
	 *  @param tag ���
	 */
	public static  void a(String tag,String log) {
		if (enable_log) Log.d(tag, log);
	}

}
