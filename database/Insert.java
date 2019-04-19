package database;

/*
 * 向数据库中插入数据
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class Insert {
	public void insert() throws ClassNotFoundException, FileNotFoundException {
		// 插入指定图片到数据库
		String path = "E:\\seetafaceJava\\SeetaFaceJavaDemo\\Picture\\james.jpg";
		FileInputStream in = new FileInputStream(new File(path));
		// 数据库连接
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 插入数据
		PreparedStatement stm;
		try {
			stm = con.prepareStatement("update student set S_image=? where S_name=?");
			stm.setBinaryStream(1, in);
			stm.setString(2, "James");
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setState(int state, String Sid) throws ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 插入数据
		String sql = "update student set S_state=" + state + " where S_id=" + "'" + Sid + "'";
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void main(String args[]) throws ClassNotFoundException, SQLException, IOException {
		Insert insert = new Insert();
		insert.insert();
		/*
		 * Search search=new Search(); search.search();
		 */
	}

	public void change(String name, String pass) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 插入数据
		String sql = "update teacher set T_password=" + "'" + pass + "'" + " where T_id=" + "'" + name + "'";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public void changeStupass(String name, String pass) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		// 插入数据
		String sql = "update student set S_password=" + "'" + pass + "'" + " where S_id=" + "'" + name + "'";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void insertresult(String coursename, String notcome, String date, int totalnum, int realnum)
			throws ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		if("null".equals(notcome)) notcome="";
		// 插入数据
		String sql = "INSERT INTO c_record VALUES (?,?,?,?,?)";
		System.out.println(sql);
		PreparedStatement stm;
		try {
			stm = con.prepareStatement(sql);
			stm.setString(1, coursename);
			stm.setString(2, notcome);
			stm.setString(3, date);
			stm.setInt(4, totalnum);
			stm.setInt(5, realnum);
			stm.executeLargeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@SuppressWarnings("null")
	public void upSrecord(String nameId, String coursename) throws SQLException, ClassNotFoundException {
		DBConnecting connect = new DBConnecting("test");
		java.sql.Connection con = connect.Connect();
		java.sql.Statement stm = con.createStatement();
		if (null!=nameId&&nameId.length()!=0) {
			System.out.println(nameId.length());
			String str[] = nameId.split("/");
			for (int i = 0; i < str.length; i++) {
				String str1[] = str[i].split("-");
				String sql = "select num from s_record where S_id =" + "'" + str1[1] + "'" + "and C_name =" + "'"
						+ coursename + "'";
				System.out.println(sql);
				ResultSet rs = stm.executeQuery(sql);
				while (rs.next()) {
					int num = rs.getInt("num");
					num++;
					String sql1 = "update s_record set num=" + num +" where S_id =" + "'" + str1[1] + "'"+ "and C_name =" + "'"
							+ coursename + "'";;
					System.out.println(sql1);
					DBConnecting connect1 = new DBConnecting("test");
					java.sql.Connection con1 = connect1.Connect();
					PreparedStatement stm1;
					stm1 = con1.prepareStatement(sql1);
					stm1.executeLargeUpdate();
				}

			}
		}

	}

}
