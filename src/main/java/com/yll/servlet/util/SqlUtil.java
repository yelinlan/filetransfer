package com.yll.servlet.util;


import java.util.ArrayList;
import java.util.List;

/**
 *@项目名称: smbms
 *@类名称: SqlUtils
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/12 0:03
 **/
public class SqlUtil {
	public static final String WHERE = " where ";
	public static final String AND = " and ";
	public static final String OR = " or ";
	public static final String SET = " set ";
	public static final String COMMA = " , ";
	public static final String LIKE = " like ";
	private static SqlUtil sqlUtil = new SqlUtil();

	private static StringBuffer sql;
	private static StringBuffer sqlVal;
	private static List<Object> params = new ArrayList();

	public Object[] getParams() {
		return params.toArray();
	}

	public SqlUtil and(String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString())) {
			if (sql.length() == 0 || !sql.toString().contains(WHERE)) {
				sql.append(WHERE);
			} else {
				sql.append(AND);
			}
			sql.append(column).append("=? ");
			params.add(val);
		}
		return this;
	}

	public SqlUtil set(String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString())) {
			if (sql.length() == 0 || !sql.toString().contains(SET)) {
				sql.append(SET);
				sql.append(column).append(" =? ");
			} else {
				sql.append(COMMA);
				sql.append(column).append(" =? ");
			}
			params.add(val);
		}
		return this;
	}

	public SqlUtil setBeans(Object o) {
		ConvertUtil.bean2Map(o).forEach(this::set);
		return this;
	}

	public SqlUtil insertBeans(Object o) {
		ConvertUtil.bean2Map(o).forEach(this::add);
		return this;
	}

	private SqlUtil add(String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString())) {
			if (sql.length() == 0) {
				sql.append(column);
				sqlVal.append(" ? ");
			} else {
				sql.append(COMMA);
				sql.append(column);

				sqlVal.append(COMMA);
				sqlVal.append(" ? ");
			}
			params.add(val);
		}
		return this;
	}

	public SqlUtil and(boolean condition, String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString()) && condition) {
			if (sql.length() == 0) {
				sql.append(WHERE);
			} else {
				sql.append(AND);
			}
			sql.append(column).append("=? ");
			params.add(val);
		}
		return this;
	}

	public SqlUtil like(boolean condition, String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString()) && condition) {
			if (sql.length() == 0) {
				sql.append(WHERE);
			} else {
				sql.append(AND);
			}
			sql.append(column).append(LIKE + " '%?% ' ");
			params.add(val);
		}
		return this;
	}

	public SqlUtil like(String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString())) {
			if (sql.length() == 0) {
				sql.append(WHERE);
			} else {
				sql.append(AND);
			}
			sql.append(column).append(LIKE + " ? ");
			params.add("%"+val+"%");
		}
		return this;
	}

	public SqlUtil limit(PageSupport pageSupport) {
		sql.append(" limit ").append(" ? , ? ");
		params.add((pageSupport.getCurrentPageNo() - 1) * pageSupport.getPageSize());
		params.add(pageSupport.getPageSize());
		return this;
	}

	public SqlUtil or(boolean condition, String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString()) && condition) {
			if (sql.length() == 0) {
				sql.append(WHERE).append(column).append("=? ");
			} else {
				sql.append(OR).append(column).append("=? ");
			}
		}
		return this;
	}

	public SqlUtil or(String column, Object val) {
		if (val != null && !StringUtils.isNullOrEmpty(val.toString())) {
			if (sql.length() == 0) {
				sql.append(WHERE).append(column).append("=? ");
			} else {
				sql.append(OR).append(column).append("=? ");
			}
		}
		return this;
	}

	public SqlUtil head(StringBuffer column) {
		column.append(sql);
		return this;
	}

	public static SqlUtil instance() {
		sql = new StringBuffer();
		sqlVal = new StringBuffer();
		params.clear();
		return sqlUtil;
	}

	public SqlUtil merge(StringBuffer insert) {
		String format = String.format(insert.toString(), sql, sqlVal);
		insert.delete(0, insert.length()).append(format);
		return this;
	}

}