package com.yll.servlet;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.yll.servlet.common.BaseServlet;
import com.yll.servlet.constant.Constant;
import com.yll.servlet.util.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *@项目名称: filetransfer
 *@类名称: FileServlet
 *@类描述:
 *@创建人: yll
 *@创建时间: 2022/11/13 8:11
 **/
public class FileServlet extends BaseServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		request = req;
		response = resp;
		String msg = "上传文件失败";
		if (!ServletFileUpload.isMultipartContent(req)) {
			return;
		}
		File upload = FileUtil.mkdir(realPath(Constant.UPLOAD));
		File tmp = FileUtil.mkdir(realPath(Constant.TMP));
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(tmp);
		factory.setSizeThreshold(Constant.MB);

		ServletFileUpload fileUpload = new ServletFileUpload(factory);
		fileUpload.setProgressListener((progress, size, items) -> System.out.printf("总大小：%s,已上传：%s\n", size, progress));
		fileUpload.setHeaderEncoding(Constant.UTF_8);
		fileUpload.setFileSizeMax(10 * Constant.MB);
		fileUpload.setSizeMax(20 * Constant.MB);


		List<FileItem> fileItems;
		try {
			fileItems = fileUpload.parseRequest(request);
		} catch (FileUploadException e) {
			e.printStackTrace();
			return;
		}
		for (FileItem item : fileItems) {
			if (item.isFormField()) {
				System.out.printf("%s:%s", item.getFieldName(), item.getString(Constant.UTF_8));
			} else {
				if (StringUtils.isNullOrEmpty(item.getFieldName())) {
					continue;
				}
				String name = FileUtil.getName(item.getName());
				String type = item.getName().substring(item.getName().indexOf(".")+1);
				String dest = realPath(Constant.UPLOAD + Constant.SPLASH + UUID.fastUUID() + Constant.SPLASH + name);
				System.out.printf("%s：%s", name, type);
				System.out.println();
				FileUtil.writeFromStream(item.getInputStream(), new File(dest));
				msg = "文件上传成功";
				item.delete();
			}
		}
		request.setAttribute("msg", msg);
		dispatcher("/index.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}