/*
 * 2019/2/24
 * ��ʦ��¼�ͻ�ȡ��Ϣ
 */
package project.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;

public class Submit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String course=null;
	// �������ݿ�
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

	public Submit() {
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
		String courseName = request.getParameter("coursename");
		String totalnum = request.getParameter("totalnum");
		String realnum = request.getParameter("realnum");
		String nocome=request.getParameter("notcome");

		// �ڷ������˽��������������
		courseName = new String(courseName.getBytes("ISO8859-1"), "UTF-8");
		totalnum = new String(totalnum.getBytes("ISO8859-1"), "UTF-8");
		realnum = new String(realnum.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("�γ�����" + courseName);
		System.out.println("Ӧ��������" + totalnum);
		System.out.println("ʵ��������" + realnum);
		System.out.println("δ��������" + nocome);
		
		String regEx="[^0-9]";  
		Pattern p = Pattern.compile(regEx);  
		Matcher a = p.matcher(totalnum); 
		Matcher b = p.matcher(realnum); 
		int tnum=Integer.parseInt(a.replaceAll("").trim());
		int rnum=Integer.parseInt(b.replaceAll("").trim());
		
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");//�������ڸ�ʽ
        String date=df.format(new Date()).toString();// new Date()Ϊ��ȡ��ǰϵͳʱ��
		//�����д�����ݿ�
        try {
			new Insert().insertresult(courseName,nocome,date,tnum,rnum);
			new Insert().upSrecord(nocome,courseName);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
