
package seetaface;

import java.awt.image.BufferedImage;
import java.io.File;

public class XUtils {

 
  public static Boolean fileExists(String vPath) {
    if (null == vPath || "".equals(vPath)) {
      return false;
    }

    try {
      File file = new File(vPath);
      if (!file.exists()) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }

    return true;
  }
 

  public static void LogOut(String vTag, String vStr){
	  System.out.println(vTag+", "+vStr);
  }

 
 
  public static String intArrayToString(int[] vArr) {
    String tRetString = "";
    if (null == vArr || vArr.length < 1) {
      return tRetString;
    }
    int num = vArr.length;
    for (int i = 0; i < num; i++) {
      tRetString += vArr[i] + ",";
    }

    if (!isEmptyStr(tRetString)) {
      tRetString = tRetString.substring(0, tRetString.length() - 1);
    }
    return tRetString;
  }

  
  public static boolean isImageFile(String vPath) {
    if (null == vPath || vPath.trim().length() < 1) {
      return false;
    }

    String tPath = vPath.trim();

 
    String end = tPath.substring(tPath.lastIndexOf(".") + 1, tPath.length()).toLowerCase();

    if ("".equals(end.trim())) {
      return false;
    }

   
    if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")
        || end.equals("mov")) {
    } else {
      return false;
    }

   
    File file = new File(tPath);
    if (!file.exists()) {
      return false;
    }

    return true;
  }

  public static Boolean isEmptyStr(String vStr){
	  return (vStr == null || vStr.length() <= 0);  
  }
  
  

 
  public static String[] stringStringValueSplit(String vStr) {
    //LogOut("stringStringValueSplit", "1");
    if (isEmptyStr(vStr)) {
      return null;
    }

    LogOut("stringStringValueSplit", vStr);

    String oldString = vStr;
    vStr = vStr.replaceAll("\\r", "");
    vStr = vStr.replaceAll("\\n", "");
    vStr = vStr.replaceAll("\\[", "");
    vStr = vStr.replaceAll("\\]", "");
    vStr = vStr.replaceAll(";", ",");
    vStr = vStr.replaceAll(" ", "");

    LogOut("stringStringValueSplit", "str after replace =" + vStr + ", old=" + oldString);

    return vStr.split(",|;|\\[|\\]");
  }

 
  public static int[] stringIntValueSplit(String vStr) {
    LogOut("stringIntValueSplit", "1");
    if (isEmptyStr(vStr)) {
      return null;
    }

    LogOut("stringIntValueSplit", vStr);

    String oldString = vStr;
    vStr = vStr.replaceAll("\\r", "");
    vStr = vStr.replaceAll("\\n", "");
    vStr = vStr.replaceAll("\\[", "");
    vStr = vStr.replaceAll("\\]", "");
    vStr = vStr.replaceAll(";", ",");
    vStr = vStr.replaceAll(" ", "");

    LogOut("stringIntValueSplit", "str after replace =" + vStr + ", old=" + oldString);

    String[] pStr = vStr.split(",|;|\\[|\\]");

    if (null == pStr || 0 == pStr.length) {
      LogOut("stringIntValueSplit", "pStr.length=0");
      return null;
    }
    int[] dRet = new int[pStr.length];

    LogOut("stringIntValueSplit", "pStr.length=" + pStr.length);

    for (int i = 0; i < pStr.length; i++) {
      String tString = pStr[i];
      if (null == tString || tString.trim().equals("")) {
        LogOut("stringIntValueSplit", "i=" + i + ", null, or '' ");
        continue;
      }

      try {
        dRet[i] = Integer.parseInt(tString);
      } catch (NumberFormatException ex) {
        // System.out.println("The String does not contain a parsable integer");
        LogOut("stringIntValueSplit", "NumberFormatException," + ex.toString());
      }
    }

    return dRet;

  }
 

  
  
  
	 
	
	public static BufferedImage getScaledBitmap(String vPath, int vMinWidth) {	
		String tag = "getScaledBitmap";
		return null;
	}

}




