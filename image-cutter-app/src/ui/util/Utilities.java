package ui.util;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class Utilities {
	public static void centerWindow(Window wnd){
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		wnd.setLocation((size.width - wnd.getWidth()) / 2, (size.height - wnd.getHeight()) / 2);
	}
	
	public static void toLower(String array[]) {
		for (int i=0, n=array.length; i<n; i++) {
			array[i] = array[i].toLowerCase();
		}
	}
	
	public static Image scaleImageIcon(BufferedImage icon, int width, int height){
		Image image = null;
		if(icon.getWidth() != width || icon.getHeight() != height)
			image = icon.getScaledInstance(width, height, Image.SCALE_DEFAULT);
		
		return image;
	}
	
	public static ImageIcon scaleImageIcon(ImageIcon icon, int width, int height){
		if(icon.getIconWidth() != width || icon.getIconHeight() != height)
			icon = new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
		
		return icon;
	}
	
	public static boolean haveExtension(String path, String extension){
		boolean have = false;
		if((path.endsWith(extension) &&(path.charAt(path.length()-extension.length()-1)) == '.')){
			have = true;
		}
		return have;
	}
}
