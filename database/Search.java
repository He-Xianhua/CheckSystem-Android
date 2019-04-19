package database;

import java.io.IOException;
import java.sql.*;

public class Search {

	// 获取学该课程的学生总人数
	public ResultSet getTotalNum(String courseName) throws ClassNotFoundException {
		ResultSet rs = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		String sql = "select COUNT(*) as ct from learn where C_name='" + courseName + "'";
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet search(String Sid) throws ClassNotFoundException {
		ResultSet rs = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select * from student where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet getSid(String courseName) throws ClassNotFoundException {
		ResultSet rs = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select S_id from learn where C_name=" + "'" + courseName + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	// 获取所有学生的id
	public ResultSet searchSid() throws ClassNotFoundException {
		ResultSet rs = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm;
		try {
			stm = con.createStatement();
			String sql = "select S_id from student";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public String getrecord(String courseName) throws ClassNotFoundException {
		String info = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm=null;
		ResultSet rs =null;
		try {
			stm = con.createStatement();
			String sql = "select * from c_record where C_name=" + "'" + courseName + "'" + "order by Date";
			rs= stm.executeQuery(sql);
			while (rs.next()) {
				String date = rs.getString("Date");
				String totalnum = rs.getString("totalnum");
				String realnum = rs.getString("realnum");
				String nocome = rs.getString("notcome");
				if (info == null)
					info = date + ";" + totalnum + ";" + realnum + ";" + nocome;
				else
					info = info + "," + date + ";" + totalnum + ";" + realnum + ";" + nocome;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return info;
		// TODO Auto-generated method stub

	}
	
	public String searchrecord(String courseName) throws ClassNotFoundException{
		String record=null;
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			String sql = "select * from s_record where C_name=" + "'" + courseName + "'"+ "order by num desc ";
			 rs= stm.executeQuery(sql);
			while (rs.next()) {
				String S_id = rs.getString("S_id");
				int num=rs.getInt("num");
				if(num!=0){
					String name=searchname(S_id);
					if(record==null)
						record=S_id+"-"+name+";"+num+",";
					else
						record+=S_id+"-"+name+";"+num+",";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return record;
	}
	
	public String  searchname(String Sid) throws ClassNotFoundException {
		ResultSet rs = null;
		String name = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select S_name from student where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
			while(rs.next()){
				name=rs.getString("S_name");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				stm.close();
				con.close();
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return name;
	}
	
	public ResultSet getSrecord(String Sid) throws ClassNotFoundException{
		ResultSet rs = null;
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 数据查询
		java.sql.Statement stm = null;
		try {
			stm = con.createStatement();
			String sql = "select * from s_record where S_id=" + "'" + Sid + "'";
			rs = stm.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
		
	}
}
