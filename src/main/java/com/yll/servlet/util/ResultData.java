package com.yll.servlet.util;

import lombok.Data;

import java.io.Serializable;

/**
 *@项目名称: smbms
 *@类名称: ResultData
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/11 22:46
 **/
@Data
public class ResultData implements Serializable {

	private static final long serialVersionUID = 1L;

	private String result;
	private String userCode;
	private String delResult;
}