/*
 * 2019/2/24
 * ��ʦ��¼�ͻ�ȡ��Ϣ
 */
package project.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;

public class teacherInfo extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String course=null;
	// �������ݿ�
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

	public teacherInfo() {
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
		String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String type = request.getParameter("type");// ���ʷ�����ʱ�����ͣ�0��ʾ��¼��֤��1��ʾ��ȡ��ʦ��Ϣ,2��ʾ��ȡ��ʦ�Ŀγ�,3��ʾ�޸�����

		String T_Name = null;
		String password = null;
		int ct = 0;
		// �ڷ������˽��������������
		userName = new String(userName.getBytes("ISO8859-1"), "UTF-8");
		passWord = new String(passWord.getBytes("ISO8859-1"), "UTF-8");
		type = new String(type.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("�˺ţ�" + userName);
		System.out.println("���룺" + passWord);
		System.out.println("�������ͣ�" + type);

		try {
			con = connect.Connect();

			// ��ѯ��ʦ����Ϣ
			String sql = "select T_name from teacher where T_id=" + "'" + userName + "'";
			// ��ѯ�û��Ƿ����
			String sql1 = "select count(*) as ct from teacher where T_id=" + "'" + userName + "'";
			// ��ѯ�����Ƿ���ȷ
			String sql2 = "select T_password  from teacher where T_id=" + "'" + userName + "'";

			/*
			 * �ж�type����
			 */

			switch (type) {
			case "0":
				// �ж��û��Ƿ����
				PreparedStatement ps = con.prepareStatement(sql1);
				ResultSet rs1 = ps.executeQuery();
				rs1.next();
				ct = rs1.getInt("ct");
				break;
			case "1":
				// ��������Ϊ1ʱ��ֱ��ȷ���û�����
				ct = 1;
				break;
			case "2":
				// ������ʦ�̵Ŀγ�
				ct = 1;
				returnCourse(response, userName);
				break;
			case "3":
				channgepassword(userName,passWord);
				break;
			}
			/*
			 * �ȼ���û��Ƿ���ڣ����ж������Ƿ���ȷ
			 */
			switch (ct) {
			// ctΪ0ʱ�û�������
			case 0:
				// �������ڸ��˺�ʱ����0
				String str = "0";
				returntoClient(response, str);
				break;

			case 1:
				// ct=1ʱ���û����ڣ���������Ƿ���ȷ
				java.sql.Statement stm1 = con.createStatement();
				ResultSet rs2 = stm1.executeQuery(sql2);
				while (rs2.next()) {
					password = rs2.getString("T_password");
				}
				String pass = password + "";
				if (pass.equals(passWord)) {
					// ����Ϊ1ʱ����Ҫ������ʦ��Ϣ
					if (type.equals("1")) {
						// ��ѯ��ʦ����Ϣ������
						java.sql.Statement stm = con.createStatement();
						ResultSet rs = stm.executeQuery(sql);
						while (rs.next()) {
							T_Name = rs.getString("T_name");
							//System.out.println(rs.getString("T_name"));
						}
						course=getcourse(response,userName);
						//System.out.println(course);
						String info=course+","+T_Name;
						// ���ز�ѯ������ʦ����Ϣ�Ϳγ�
						returntoClient(response, info);
					}
				} else {
					// ���������ʱ����00
					String str1 = "00";
					returntoClient(response, str1);
				}
				break;
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void channgepassword(String name,String pass) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		new Insert().change(name,pass);
		
	}

	// ������ʦ�̵Ŀγ�
	private void returnCourse(HttpServletResponse response, String T_id)
			throws UnsupportedEncodingException, IOException, SQLException {
		
		course=getcourse(response,T_id);
		if(null==course) course="";
		returntoClient(response, course);
	}

	private String getcourse(HttpServletResponse response, String T_id) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select C_name from course where T_id=" + "'" + T_id + "'";
		java.sql.Statement stm = con.createStatement();
		ResultSet rs = stm.executeQuery(sql);
		course=null;
		while (rs.next()) {
			if(course==null){
				course=rs.getString("C_name");
			}
			else
			course=course+";"+rs.getString("C_name");
		}
		return course;
	}

	// ��ͻ��˴�����Ϣ
	private void returntoClient(HttpServletResponse response, String content)
			throws UnsupportedEncodingException, IOException {
		response.getOutputStream().write(content.getBytes("utf-8"));
	}

}
