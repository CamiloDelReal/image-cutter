package ui.util;

import java.util.LinkedList;
import java.util.List;

public class ImageList {

	private List<ImageTumbail> list;
	
	public ImageList(){
		list = new LinkedList<ImageTumbail>();
	}
	
	public List<ImageTumbail> getImageTumbails(){
		return list;
	}
}
