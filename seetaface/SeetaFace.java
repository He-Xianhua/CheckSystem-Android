package seetaface; 

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLException;

public class SeetaFace {  
    static{  
//    	System.setProperty( "java.library.path", "D:\\vc_projects\\NativeDemoSeeta1\\x64\\Debug" );
    	System.out.println("need for SeetaFaceJNIDLL.dll under java.library.path---"+ System.getProperty("java.library.path"));
        System.loadLibrary("SeetaFaceJNIDLL");   
    }
    
    
    public native float CalcSimilarity(float[] vFeat1, float[] vFeat2);
     
    
    
	public CMSeetaFace[] DetectFacesPath(String vPath){
		String tag = "DetectFacesPath";
		if(null == vPath || vPath.equals("")){
			return null; 
		}
		
		CMSeetaFace[] tFaces = null; // new CMSeetaFace[0];
		
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(vPath));
			System.out.println(tag + ", image size="+ image.getWidth()+", "+image.getHeight());

	        byte[] tBmpData = ((DataBufferByte) image.getData().getDataBuffer()).getData();
	        int tWidth = image.getWidth(); 
	        int tHeight = image.getHeight();
	        int tCh = tBmpData.length / (tWidth*tHeight); 
	        
	        tFaces = DetectFacesByte(tBmpData, tWidth, tHeight);
	        if(null != tFaces){
	        	System.out.println(tag + " facenum=" + tFaces.length); 
	        	
	        	//System.out.println(" tFaces[0].bottom"+   		tFaces[0].bottom);
	        	
	        }else{
	        	System.out.println(tag + " no face"); 
	        }
		} catch (FileNotFoundException e) {
			System.out.println("e="+e.toString()); 
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("e="+e.toString()); 
			e.printStackTrace();
		}  
		
		return tFaces;
	}

    
	public CMSeetaFace[] DetectFacesImage(BufferedImage vImage){
		String tag = "DetectFacesPath";
		if(null == vImage){
			return null; 
		}
		
		CMSeetaFace[] tFaces = null; // new CMSeetaFace[0];
		 
		System.out.println(tag + ", image size="+ vImage.getWidth()+", "+vImage.getHeight());

        byte[] tBmpData = ((DataBufferByte) vImage.getData().getDataBuffer()).getData();
        int tWidth = vImage.getWidth(); 
        int tHeight = vImage.getHeight();
        int tCh = tBmpData.length / (tWidth*tHeight); 
        
        tFaces = DetectFacesByte(tBmpData, tWidth, tHeight);
        if(null != tFaces){
        	System.out.println(tag + " facenum=" + tFaces.length); 
        	
        	//System.out.println(" tFaces[0].bottom"+   		tFaces[0].bottom);
        	
        }else{
        	System.out.println(tag + " no face"); 
        }
	 
		
		return tFaces;
	}
    
    
    public native CMSeetaFace[] DetectFacesByte(byte[] vBmpByte, int vWidth, int vHeight); 
    
    
	
	public native boolean initModelPath(String vModelDir);
	 

}  