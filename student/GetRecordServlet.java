
package student;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.Search;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetRecordServlet extends HttpServlet {
	String userName;
	String password;
	List<String> list = new ArrayList<String>();

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String Sid = request.getParameter("Sid");
		String Cname;
		Sid = new String(Sid.getBytes("ISO8859-1"), "UTF-8");
		
		System.out.println("-----------------------------------");
		System.out.println("学号：" + Sid);
		
		try {
			ResultSet rs=new Search().getSrecord(Sid);
			String record = null;
			while(rs.next()){
				Cname=rs.getString("C_name");
				int num=rs.getInt("num");
				String s=Cname+";"+num;
				if(record==null)
					record=s+",";
				else
				record+=s+",";
				
			}
			System.out.println(record);
			returntoClient(response,record);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 向客户端传送信息
		private void returntoClient(HttpServletResponse response, String content)
				throws UnsupportedEncodingException, IOException {
			response.getOutputStream().write(content.getBytes("utf-8"));
		}
}
