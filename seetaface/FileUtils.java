/**
 * 采用中科院山世光开源的SeetaFaceEngine实现android上的人脸检测与对齐、识别
 * BSD license
 * 广州炒米信息科技有限公司
 * www.cume.cc
 * 吴祖玉
 * wuzuyu365@163.com
 * 2016.11.9
 *
 */

package seetaface;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.omg.CORBA.Environment;

public class FileUtils {

	private String TAG = FileUtils.class.getSimpleName();

	/**
	 * 操作成功返回值
	 */
	public static final int SUCCESS = 0;

	/**
	 * 操作失败返回值
	 */
	public static final int FAILED = -1;


	public static byte[] Bitmap2Bytes(BufferedImage bm) {
		byte[] data = ((DataBufferByte) bm.getData().getDataBuffer()).getData();
		return data; 
	}


	/**
	 * bytes转换成十六进制字符串
	 * 
	 * @param byte[]
	 *          b byte数组
	 * @return String 每个Byte值之间空格分隔
	 */
	public static String byte2HexStr(byte[] b) {
		String stmp = "";
		StringBuilder sb = new StringBuilder("");
		for (int n = 0; n < b.length; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
			sb.append(" ");
		}
		return sb.toString().toUpperCase().trim();
	}

	/**
	 * bytes字符串转换为Byte值
	 * 
	 * @param String
	 *          src Byte字符串，每个Byte之间没有分隔符
	 * @return byte[]
	 */
	public static byte[] hexStr2Bytes(String src) {
		int m = 0, n = 0;
		int l = src.length() / 2;
		System.out.println(l);
		byte[] ret = new byte[l];
		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = Byte.decode("0x" + src.substring(i * 2, m) + src.substring(m, n));
		}
		return ret;
	}

	public static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String hex = Integer.toHexString(0xFF & bytes[i]);
			if (hex.length() == 1) {
				sb.append('0');
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
	'F' };

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}


	/**
	 * 把浮点数组保存到文件
	 * @param vArr
	 * @param vPath
	 */
	public static Boolean saveFloatArray(float[] vArr, String vPath){
//		String tag = "saveFloatArray"; 
		if(null == vArr){
//			System.out.println(tag+ ", null == vArr");
			return false; 
		}

		if(null == vPath || vPath.equals("")){
//			System.out.println(tag+ ", null == vPath");
			return false; 
		}

		FileOutputStream fos = null;
		DataOutputStream dos = null;

		try{
			// create file output stream
			fos = new FileOutputStream(vPath);

			// create data output stream
			dos = new DataOutputStream(fos);

			// for each byte in the buffer
			for (float f:vArr)
			{
				// write float to the data output stream
//				System.out.println(tag+ ", f="+f);
				dos.writeFloat(f);                     
			}

			// force bytes to the underlying stream
			dos.flush();

		}catch(Exception e){
			// if any I/O error occurs
			e.printStackTrace();
		}finally{

			if(dos!=null)
				try
			{
					dos.close();
			}
			catch (IOException e)
			{
//				System.out.println(tag+ ", e="+e.toString());
				e.printStackTrace();
			}

			if(fos!=null)
				try
			{
					fos.close();
			}
			catch (IOException e)
			{
//				System.out.println(tag+ ", e="+e.toString());
				e.printStackTrace();
			}
		}

		return true; 
	}

	/**
	 * 从文件加载浮点数组
	 * @param vPath
	 * @param vCount：数组长度
	 * @return
	 */
	public static float[] loadFloatArray(String vPath, int vCount){
//		String tag = "loadFloatArray"; 
		if(!fileIsExists(vPath)){
			return null; 
		}

		// LogUtils.i(tag, "1,path="+vPath+", count="+vCount);
		InputStream is = null;
		DataInputStream dis = null;

		float[] tArr = new float[vCount]; 
		try{
			// create file input stream
			is = new FileInputStream(vPath);
			// LogUtils.i(tag, "2, FileInputStream ok"); 
			// create new data input stream
			dis = new DataInputStream(is);

			// read till end of the stream
			int i=0; 
			while(dis.available()>0)
			{
				// read character
				float c = dis.readFloat();
				tArr[i++] = c;
				//LogUtils.i(tag, "i="+i+",c="+c); 
				// print
				//System.out.print(c + " ");
			}
		}catch(Exception e){
			// if any I/O error occurs
			e.printStackTrace();
		}finally{

			// releases all system resources from the streams
			if(dis!=null)
				try
			{
					dis.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return tArr;
	}



	/**
	 * 
	 * 方法名: </br>
	 * 详述: 判断文件是否存在</br>
	 * 开发人员：王太顺</br>
	 * 创建时间：2015年11月13日</br>
	 * 
	 * @param path
	 * @return
	 */
	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *          被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String sPath) {
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *          被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public static boolean deleteDirectory(String sPath) {
		if (XUtils.isEmptyStr(sPath)) {
			return false;
		}
		boolean flag;
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 文件转化为字节数组
	 * 
	 * @EditTime 2007-8-13 上午11:45:28
	 */
	public static byte[] getBytesFromFile(File f) {
		if (f == null) {
			return null;
		}
		try {
			FileInputStream stream = new FileInputStream(f);
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			stream.close();
			out.close();
			return out.toByteArray();
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * 把字节数组保存为一个文件
	 * 
	 * @EditTime 2007-8-13 上午11:45:56
	 */
	public static File getFileFromBytes(byte[] b, String outputFile) {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = new File(outputFile);
			FileOutputStream fstream = new FileOutputStream(file);
			stream = new BufferedOutputStream(fstream);
			stream.write(b);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 
	 * 方法名: </br>
	 * 详述: </br>
	 * 开发人员：王太顺</br>
	 * 创建时间：2015-10-26</br>
	 * 
	 * @param dbFile
	 * @param backup
	 * @throws IOException
	 */
	public static void fileCopy(File dbFile, File backup) throws IOException {
		FileChannel inChannel = new FileInputStream(dbFile).getChannel();
		FileChannel outChannel = new FileOutputStream(backup).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inChannel != null) {
				inChannel.close();
			}
			if (outChannel != null) {
				outChannel.close();
			}
		}
	}


	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
	public static int createDir(String path) {
		return createDir(new File(path));
	}

	public static int createDir(File file) {
		if (file.exists()) {
			if (file.isDirectory())
				return SUCCESS;
			file.delete(); // 避免他是一个文件存在
		}

		if (file.mkdirs())
			return SUCCESS;

		return FAILED;
	}

}
