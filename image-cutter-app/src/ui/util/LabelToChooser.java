package ui.util;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class LabelToChooser extends JLabel implements PropertyChangeListener {
	private static final long serialVersionUID = 4185562899513223451L;
	private static final int PREFERRED_WIDTH = 80;
	private static final int PREFERRED_HEIGHT = 100;
	
	public LabelToChooser(JFileChooser chooser){
		setVerticalAlignment(JLabel.CENTER);
		setHorizontalAlignment(JLabel.CENTER);
		chooser.addPropertyChangeListener(this);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
		setMaximumSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		String changeName = event.getPropertyName();
		
		if (changeName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			File file = (File)event.getNewValue();
			if (file != null) {
				ImageIcon icon = new ImageIcon(file.getPath());
				icon = Utilities.scaleImageIcon(icon, PREFERRED_WIDTH, PREFERRED_HEIGHT);
				setIcon(icon);
			}
		}
	}

}
