package com.yll.servlet.util;


import com.yll.servlet.constant.Constant;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 *@项目名称: smbms
 *@类名称: ConvertUtil
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/10 20:35
 **/
public class ConvertUtil {

	public static Map<String, Object> bean2Map(Object o) {
		Map<String, Object> beanMap = new HashMap<>();
		Class<?> clazz = o.getClass();
		if (clazz != null) {
			Arrays.stream(clazz.getDeclaredFields()).forEach(p -> {
				p.setAccessible(true);
				try {
					if (p.get(o) != null && !StringUtils.isNullOrEmpty(p.get(o).toString()) && !"serialVersionUID"
							.equals(p.getName()) && !"id".equals(p.getName())) {
						beanMap.put(p.getName(), p.get(o));
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				p.setAccessible(false);
			});
		}
		return beanMap;
	}

	public static <T> T convertBean(Class<T> clazz, HttpServletRequest req) {
		T bean = null;
		if (clazz != null) {
			try {
				bean = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			Object finalBean = bean;
			Arrays.stream(clazz.getDeclaredFields()).forEach(p -> {
				p.setAccessible(true);
				try {
					Object rsObject = null;
					rsObject = req.getParameter(p.getName());
					if (rsObject != null) {
						setBeanFieldByType(finalBean, p, rsObject);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				p.setAccessible(false);
			});
		}
		return bean;
	}

	private static <T> T convertOneBean(Class<T> clazz, ResultSet rs) {
		T bean = null;
		if (clazz != null) {
			try {
				bean = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			Object finalBean = bean;
			Arrays.stream(clazz.getDeclaredFields()).forEach(p -> {
				p.setAccessible(true);
				try {
					Object rsObject = null;
					try {
						rsObject = rs.getObject(p.getName());
					} catch (SQLException ignored) {

					}
					if (rsObject != null && !StringUtils.isNullOrEmpty(rsObject.toString())) {
						setBeanFieldByType(finalBean, p, rsObject);
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				p.setAccessible(false);
			});
		}
		return bean;
	}

	private static void setBeanFieldByType(Object finalBean, Field p, Object rsObject) throws IllegalAccessException {
		String dest = p.getType().getName();
		String source = rsObject.getClass().getName();
		if (!dest.equals(source)) {
			rsObject = castForce(rsObject, dest, source);
			System.out.println(p.getName() + "--->" + dest + ":" + source);
		}
		p.set(finalBean, rsObject);
	}

	private static Object castForce(Object rsObject, String dest, String source) {
		if (source.equals("java.lang.String")) {
			String s = String.valueOf(rsObject);
			if (dest.equals("java.math.BigDecimal")) {
				rsObject = new BigDecimal(s).setScale(2, BigDecimal.ROUND_DOWN);
			}else if (dest.equals("java.lang.Long")) {
				rsObject = Long.parseLong(s);
			} else if (dest.equals("java.lang.Integer")) {
				rsObject = Integer.parseInt(s);
			} else if (dest.equals("java.time.LocalDateTime")) {
				try {
					rsObject = LocalDate.parse(s, DateTimeFormatter.ofPattern(Constant.DATE_FORMAT)).atStartOfDay();
				} catch (Exception e) {
					try {
						rsObject = LocalDateTime.parse(s, DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT));
					} catch (Exception ignored) {
						rsObject = LocalDateTime.parse(s);
					}
				}
			}
		}
		return rsObject;
	}

	public static <T> List<T> convertBeans(Class<T> clazz, ResultSet rs) throws SQLException {
		List<T> list = new ArrayList<>();
		if (rs != null) {
			while (rs.next()) {
				list.add(convertOneBean(clazz, rs));
			}
		}
		return list;
	}

	public static <T> T convertBean(Class<T> clazz, ResultSet rs) throws SQLException {
		if (rs != null) {
			List<T> list = convertBeans(clazz, rs);
			if (list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	public static int convertCount(ResultSet rs, String name) throws SQLException {
		if (rs.next()) {
			return rs.getInt("billCount");
		}
		return 0;
	}
}