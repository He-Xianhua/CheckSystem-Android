package seetaface;

public class CMSeetaFace {
 
	//人脸范围
    public int left, right, top, bottom;
    
    public float roll_angle;  
    public float pitch_angle;	
    public float yaw_angle;	
        
   
    public int landmarks[] = new int[10];
    
    
	public float features[] = new float[2048];
}
