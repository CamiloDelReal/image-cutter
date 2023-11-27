package ui.util;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class ExtendedFileFilter extends FileFilter{
	private String description;
	private String []extensions;
	
	public ExtendedFileFilter(String description, String extensions[]){
		this.description = description;
		this.extensions = (String[])extensions.clone();
		Utilities.toLower(this.extensions);
	}
	
	public String[] getExtensions(){
		return extensions;
	}
	
	@Override
	public boolean accept(File file) {
		if (file.isDirectory()){
			return true;
		}
		else{
			String path = file.getAbsolutePath().toLowerCase();
			for (int i=0, n=extensions.length; i<n; i++) {
				String extension = extensions[i];
				if((path.endsWith(extension) &&(path.charAt(path.length()-extension.length()-1)) == '.')){
					return true;
				}
			}
		}
			
		return false;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
}