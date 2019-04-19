package project.server;
 
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
 
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.mysql.cj.jdbc.Blob;
 
public class ImageSever extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String path;
 
	public ImageSever() {
		super();
	}
 
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*String course = request.getParameter("name");

		// 在服务器端解决中文乱码问题
		course = new String(course.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("课程名：" + course);
		System.out.println("-----------------------------------");*/
		
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		// 创建文件项目工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
 
		// 设置文件上传路径
		String upload = this.getServletContext().getRealPath("/");
		
		// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
		String temp = System.getProperty("java.io.tmpdir");
		// 设置缓冲区大小为 5M
		factory.setSizeThreshold(1024 * 1024 * 5);
		// 设置临时文件夹为temp
		factory.setRepository(new File(temp));
		// 用工厂实例化上传组件,ServletFileUpload 用来解析文件上传请求
		ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
 
		// 解析结果放在List中
		try {
			List<FileItem> list = servletFileUpload.parseRequest(request);
 
			for (FileItem item : list) {
				String name = item.getFieldName();
				System.out.println(name);
				InputStream is = item.getInputStream();
				if (name.contains("content")) {
					System.out.println(inputStream2String(is));
				} else if (name.contains("img")) {
					try {
						//System.out.println("aa");
						path = upload+"\\"+item.getName();
						inputStream2File(is, path);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(upload);
			out.write(path);  //这里我把服务端成功后，返回给客户端的是上传成功后路径
		} catch (org.apache.commons.fileupload.FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
 
		out.flush();
		out.close();
	}
 
	// 流转化成字符串
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		return baos.toString();
	}
 
	// 流转化成文件
	public static void inputStream2File(InputStream is, String savePath) throws Exception {
		//Blob blob=Hibernate.createBlob(is);
		System.out.println("文件保存路径为:" + savePath);
		File file = new File(savePath);
		InputStream inputSteam = is;
		BufferedInputStream fis = new BufferedInputStream(inputSteam);
		FileOutputStream fos = new FileOutputStream(file);
		int f;
		while ((f = fis.read()) != -1) {
			fos.write(f);
		}
		fos.flush();
		fos.close();
		fis.close();
		inputSteam.close();
 
	}
}