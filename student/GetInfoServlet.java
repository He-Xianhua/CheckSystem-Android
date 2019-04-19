package student;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.DBConnecting;
import database.Insert;
import database.Search;

import java.io.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetInfoServlet extends HttpServlet {
    String userName;
    String course=null;
	// 连接数据库
	DBConnecting connect = new DBConnecting("test");
	java.sql.Connection con;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse responce) throws IOException {
        this.doPost(request,responce);
        
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse responce) throws IOException {
    	String userName = request.getParameter("userName");
		String passWord = request.getParameter("passWord");
		String type = request.getParameter("type");// 访问服务器时的类型，0表示登录验证，1表示获取学生信息,2表示获取学生的课程,3表示修改密码

		String S_Name = null;
		String password = null;
		int ct = 0;
		// 在服务器端解决中文乱码问题
		userName = new String(userName.getBytes("ISO8859-1"), "UTF-8");
		passWord = new String(passWord.getBytes("ISO8859-1"), "UTF-8");
		type = new String(type.getBytes("ISO8859-1"), "UTF-8");
		System.out.println("-----------------------------------");
		System.out.println("账号：" + userName);
		System.out.println("密码：" + passWord);
		System.out.println("访问类型：" + type);

		try {
			con = connect.Connect();

			// 查询学生的信息
			String sql = "select S_name from student where S_id=" + "'" + userName + "'";
			// 查询用户是否存在
			String sql1 = "select count(*) as ct from student where S_id=" + "'" + userName + "'";
			// 查询密码是否正确
			String sql2 = "select S_password  from student where S_id=" + "'" + userName + "'";

			/*
			 * 判断type类型
			 */

			switch (type) {
			case "0":
				// 判断用户是否存在
				PreparedStatement ps = con.prepareStatement(sql1);
				ResultSet rs1 = ps.executeQuery();
				rs1.next();
				ct = rs1.getInt("ct");
				break;
			case "1":
				// 访问类型为1时，直接确认用户存在
				ct = 1;
				break;
			case "2":
				// 返回学生教的课程
				ct = 1;
				returnCourse(responce, userName);
				break;
				//修改密码
			case "3":
				channgepassword(userName,passWord);
				break;
			}
			/*
			 * 先检查用户是否存在，再判断密码是否正确
			 */
			switch (ct) {
			// ct为0时用户不存在
			case 0:
				// 当不存在该账号时返回0
				String str = "0";
				returntoClient(responce, str);
				break;

			case 1:
				// ct=1时，用户存在，检查密码是否正确
				java.sql.Statement stm1 = con.createStatement();
				ResultSet rs2 = stm1.executeQuery(sql2);
				while (rs2.next()) {
					password = rs2.getString("S_password");
				}
				String pass = password + "";
				if (pass.equals(passWord)) {
					// 类型为1时，需要返回学生信息
					if (type.equals("1")) {
						// 查询学生的信息并返回
						java.sql.Statement stm = con.createStatement();
						ResultSet rs = stm.executeQuery(sql);
						while (rs.next()) {
							S_Name = rs.getString("S_name");
							//System.out.println(rs.getString("T_name"));
						}
						course=getcourse(responce,userName);
						//System.out.println(course);
						String info=course+","+S_Name;
						// 返回查询到的学生的信息和课程
						returntoClient(responce, info);
					}
				} else {
					// 当密码错误时返回00
					String str1 = "00";
					returntoClient(responce, str1);
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
		new Insert().changeStupass(name,pass);
		
	}

	// 返回学生教的课程
	private void returnCourse(HttpServletResponse response, String S_id)
			throws UnsupportedEncodingException, IOException, SQLException {
		
		course=getcourse(response,S_id);
		if(null==course) course="";
		returntoClient(response, course);
	}

	private String getcourse(HttpServletResponse response, String S_id) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "select C_name from learn where S_id=" + "'" + S_id + "'";
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
	
	// 向客户端传送信息
	private void returntoClient(HttpServletResponse response, String content)
			throws UnsupportedEncodingException, IOException {
		response.getOutputStream().write(content.getBytes("utf-8"));
	}
}
