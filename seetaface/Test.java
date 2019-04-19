package seetaface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import database.DBConnecting;
import database.Insert;
import database.Search;

@SuppressWarnings("restriction")
public class Test {
	static JFrame J;
	static JFrame J1;
	static String imgPath1;
	static String imgPath2;
	static CMSeetaFace[] tFaces1;
	static CMSeetaFace[] tFaces2;
	static float tSim;// 相似度
	static String srcImgPath; // 源图片地址
	static String source;
	static HttpServletResponse re;
	//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	//static String tarImgPath = "/usr/java/tomcat/apache-tomcat-7.0.92/webapps/result.jpg";
	static String tarImgPath = "E:\\seetafaceJava\\SeetaFaceJavaDemo\\Picture\\result\\result.jpg"; // 待存储的地址
	
	static String name;

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		imgPath1 = "Picture\\GDP.jpg";// 多个人脸的图片
		source="E:\\seetafaceJava\\SeetaFaceJavaDemo";
		tarImgPath=source+"\\result.jpg";
		String course = "DataBase";
		seetaTest1(imgPath1, course);
	}

	public static String test(String course,String path,String upload,HttpServletResponse response) throws ClassNotFoundException, SQLException, IOException {
		re=response;
		source=upload;//每一次遍历数据库图片存放的地方
		tarImgPath=source+"\\result.jpg";//结果存放的地方
		imgPath1 = path;// 多个人脸的图片
		String m = seetaTest1(imgPath1, course);
		return m;
	}

	public static String seetaTest1(String imgPath1, String course)
			throws ClassNotFoundException, SQLException, IOException {
		SeetaFace tSeetaFace = new SeetaFace();
		int m = 0;
		String Name = null;
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		
		//tSeetaFace.initModelPath("/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac");
		tSeetaFace.initModelPath("E:\\SeetaFaceEngine-windows\\x64\\Release");
	
		tFaces1 = tSeetaFace.DetectFacesPath(imgPath1);
		//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
		//String tFeatPath="/usr/java/tomcat/apache-tomcat-7.0.92/webapps/seetafac/seetafeat.txt";
		String tFeatPath = "E:\\seetafeat.txt";
		
		// 当输入的图片没有人脸时,直接返回
		if (null == tFaces1)
			return "0" + "noface";

		for (int i = 0; i < tFaces1.length; i++) {
			FileUtils.saveFloatArray(tFaces1[i].features, tFeatPath);
			float[] tFeat = FileUtils.loadFloatArray(tFeatPath, 2048);
			Color markContentColor = null;// 颜色

			// 根据课程名称获取学生的学号
			ResultSet rs1 = new Search().getSid(course);
			// 获取学号
			while (rs1.next()) {
				String Sid = rs1.getString("S_id");
				ResultSet rs = new Search().search(Sid);
				// 调取数据库中的图片并进行比对
				while (rs.next()) {
					if (rs.getInt("S_state") != 1) {
					    Blob blob = (Blob) rs.getBlob("S_image");
						name = rs.getString("S_name");
						File file = new File(source+"\\test.jpg");// 指定文件的输出路径和文件名
						if (!file.exists())// 判断，如果文件不存在，则创建文件
							file.createNewFile();
						FileOutputStream fos = new FileOutputStream(file);// 根据指定的路径，创建文件输出流
						BufferedInputStream in = new BufferedInputStream(blob.getBinaryStream());// 将得到的文件写入流中
						BufferedImage image = ImageIO.read(in);// 将流转换为Image对象
						JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
						encoder.encode(image);
						tFaces2 = tSeetaFace.DetectFacesPath(source+"\\test.jpg");
						// 计算相似值
						tSim = tSeetaFace.CalcSimilarity(tFaces2[0].features, tFeat);
						if (tSim >= 0.6) {// 相似度大于0.6时将数据库中对应的名字标记在图片上
							//将state设置为1，表示已到
							new Insert().setState(1, Sid);
							m++;

							markContentColor = new Color(0, 255, 0, 128); // 颜色和透明度
							srcImgPath = imgPath1; // 源图片地址
							// tarImgPath = "F:\\Picture\\result\\result.jpg";
							// // 待存储的地址
							// 将名字标记在图片上
							if (m == 1) {
								//System.out.println("i=" + m);
								addWaterMark(srcImgPath, tarImgPath, tFaces1[i], name, markContentColor);
							} else {
								//System.out.println("i=" + m);
								addWaterMark(tarImgPath, tarImgPath, tFaces1[i], name, markContentColor);
							}
						}
					}

				}
			}
		}

		// 获取选该门课程学生id
		ResultSet rs = new Search().getSid(course);
		while (rs.next()) {
			String Sid = rs.getString("S_id");
			// 根据学生id获取state
			ResultSet rs1 = new Search().search(Sid);
			while (rs1.next()) {
				int state = rs1.getInt("S_state");
				if (state == 0) {
					String str0 = rs1.getString("S_name");
					String str1=rs1.getString("S_id");
					String str=str0+"-"+str1;
					if (Name == null) {
						Name = str;
					} else {
						Name = Name + ";" + str;
					}
				}
			}
		}
		System.out.println("添加标记完成");

		// 结束时
		ResultSet rs1 = new Search().searchSid();
		while (rs1.next()) {
			String sid = rs1.getString("S_id");
			new Insert().setState(0, sid);// 把学生的state设置为0，表示未到
		}

		return m + "," + Name;
	}

	private static void showimg() {
		J = new JFrame();
		J.setTitle("Img1");
		J.setBounds(230, 130, 800, 800);
		J.add(new JLabel(new ImageIcon(tarImgPath)));
		J.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		J.setVisible(true);
	}

	// 给图片标记序号
	public static void addWaterMark(String srcImgPath, String tarImgPath, CMSeetaFace tFaces, String num,
			Color markContentColor) {
		try {
			// 读取原图片信息
			File srcImgFile = new File(srcImgPath);// 得到文件
			Image srcImg = ImageIO.read(srcImgFile);// 文件转化为图片
			int srcImgWidth = srcImg.getWidth(null);// 获取图片的宽
			int srcImgHeight = srcImg.getHeight(null);// 获取图片的高

			Font font = new Font("微软雅黑", Font.PLAIN, 25); // 字体
			// 加标号
			BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = bufImg.createGraphics();
			g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
			g.setColor(markContentColor); // 设置颜色
			g.setFont(font); // 设置字体

			int x = (tFaces.right + tFaces.left) / 2;
			int y = tFaces.top;
			g.drawString(num, x, y); // 标记出序号
			g.dispose();
			// 输出图片
			FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
			ImageIO.write(bufImg, "jpg", outImgStream);

			outImgStream.flush();
			outImgStream.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
		return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
	}
	
	// 向客户端传送信息
		private static void returntoClient(HttpServletResponse response, String content)
				throws UnsupportedEncodingException, IOException {
			response.getOutputStream().write(content.getBytes("utf-8"));
		}
}
