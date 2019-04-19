package database;

/*
 * 数据库的连接，从外部传入数据库名给构造函数，由连接方法Connect返回Connection
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
 
public class DBConnecting {
	String DataBase="test";
	public DBConnecting(String DataBase){
		this.DataBase=DataBase;
	}
   @SuppressWarnings("finally")
public  Connection Connect() throws ClassNotFoundException{
	   //声明Connection对象
	   Connection con = null;
   		//驱动程序名
	   String driver = "com.mysql.cj.jdbc.Driver";
   		//URL指向要访问的数据库名mydata
	   String url = "jdbc:mysql://47.102.205.118:3306/myproject?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false";
	   //MySQL配置时的用户名
	   String user = "root";
	   //MySQL配置时的密码
	   String password = "root";
       //加载驱动程序
       Class.forName(driver);
       //连接MySQL数据库！！
       try{
    	   con = DriverManager.getConnection(url,user,password);
    	   System.out.println("获取数据库连接成功！");
       }catch (SQLException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
           System.out.println("获取数据库连接失败！");
      }
       return con;
      
     }
   public static void main(String[] args){
	   DBConnecting db=new DBConnecting("test");
	   try {
		db.Connect();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
 
 } 