package com.yll.servlet.util;

/**
 *@项目名称: filetransfer
 *@类名称: StringUtils
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/13 8:15
 **/
public class StringUtils {
	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().equals("") ;
	}
}