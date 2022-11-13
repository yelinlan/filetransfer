package com.yll.servlet.common;


import com.yll.servlet.constant.Constant;
import com.yll.servlet.util.PageSupport;
import com.yll.servlet.util.ResultData;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *@项目名称: smbms
 *@类名称: BaseServlet
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/10 22:36
 **/
public class BaseServlet extends HttpServlet {

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected ResultData resultData;
	protected PageSupport pageSupport;

	protected BaseServlet() {
		this.pageSupport = new PageSupport();
		this.resultData = new ResultData();
	}

	public String realPath(String path) {
		return request.getServletContext().getRealPath(path);
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public void setResultData(ResultData resultData) {
		this.resultData = resultData;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
	}

	protected void result(String json) {
		response.setContentType(Constant.APPLICATION_JSON);
		try (PrintWriter writer = response.getWriter();) {
			writer.write(json);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void dispatcher(String url) {
		try {
			request.getRequestDispatcher(url).forward(request, response);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	protected void redirect(String url) {
		try {
			response.sendRedirect(getContextPath() + url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}