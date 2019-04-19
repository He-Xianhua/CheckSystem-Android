package database;
import javax.swing.*;

public class Image {
	public static void main(String args[]){
		JFrame frame=new JFrame();
		frame.setSize(200, 200);
		JLabel lable=new JLabel(new ImageIcon("F:/2.jpg"));
		frame.add(lable);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

}
