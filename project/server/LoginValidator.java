/*
 * 2019/2/24
 * 启动人脸识别程序
 */
package project.server;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ietf.jgss.GSSName;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import database.Insert;
import database.Search;
import seetaface.Test;

public class LoginValidator extends HttpServlet {
	private String path;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginValidator() {
		super();
	}

	public void destroy() {
		super.destroy();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*// 获取传来的图片
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		// PrintWriter out = response.getWriter();
		// 创建文件项目工厂对象
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		// 设置文件上传路径
		// $$$$$$$$$$$$$$$$$$$$$$
		//String upload ="/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac";
		String upload = "E:\\seetafaceJava\\SeetaFaceJavaDemo";

		// 获取系统默认的临时文件保存路径，该路径为Tomcat根目录下的temp文件夹
		// $$$$$$$$$$$$$$$$$$$$$$
		//String temp="/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac/temp";
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
						// System.out.println("aa");
						path = upload + "/" + item.getName();
						inputStream2File(is, path);
						break;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			System.out.println(upload);
			// out.write(path); //这里我把服务端成功后，返回给客户端的是上传成功后路径
		} catch (org.apache.commons.fileupload.FileUploadException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/

		// out.flush();
		// out.close();

		String course = request.getParameter("userName");
		// 在服务器端解决中文乱码问题
		course = new String(course.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("课程名：" + course);
		System.out.println("-----------------------------------");
		// 返回学习该课程的总人数
		try {
			ResultSet rs = new Search().getTotalNum(course);
			rs.next();
			int totalNum = rs.getInt("ct");
			String num = totalNum + "";
			//returntoClient(response, num + ",");
			// System.out.println(totalNum);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			 InputStream is = getimage(response,course);
		        if(null!=is){  
		            /*OutputStream os = response.getOutputStream();    
		            int len;
		            byte buf[] = new byte[1024];
		          
		            	while((len=is.read(buf))!=-1){
			                os.write(buf, 0, len); 
			            }
		            	is.close();
		           
		            os.close();*/
		        }
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 调用人脸识别的类
		/*try {
			System.out.println(upload);
			String a = Test.test(course, path, upload,response);
			returntoClient(response, a + "");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	private InputStream getimage(HttpServletResponse response,String course) throws SQLException, ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		// 根据课程名称获取学生的学号
		InputStream is = null;
		ResultSet rs1 = new Search().getSid(course);
		// 获取学号
		while (rs1.next()) {
			String Sid = rs1.getString("S_id");
			ResultSet rs = new Search().search(Sid);
			// 调取数据库中的图片并进行比对
			while (rs.next()) {
				if (rs.getInt("S_state") != 1) {
				    Blob blob = (Blob) rs.getBlob("S_image");
					String name = rs.getString("S_name");
					is = blob.getBinaryStream();
					
					OutputStream os = response.getOutputStream();    
		            int len;
		            byte buf[] = new byte[1024];
		          
		            	while((len=is.read(buf))!=-1){
			                os.write(buf, 0, len); 
			            }
		            	is.close();
		           
		            os.close();
				}
			}
		}
		return is;
	}

	// 向客户端传送信息
	private void returntoClient(HttpServletResponse response, String content)
			throws UnsupportedEncodingException, IOException {
		response.getOutputStream().write(content.getBytes("utf-8"));
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
