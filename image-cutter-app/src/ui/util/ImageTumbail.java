package ui.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class ImageTumbail extends JPanel {

	private static final long serialVersionUID = 2241252370533252336L;
	private static ImageIcon BACKGROUND;
	private static final int WIDTH = 160;
	private static final int HEIGHT = 160;
	private static final int BORDER = 20;
	public static JPanel parent; 
	private JPopupMenu popup;
	
	/*private String lastNameExisted = new String("");
	private int count = 1;*/
	
	private BufferedImage image;
	
	public ImageTumbail(BufferedImage image){
		BACKGROUND = new ImageIcon(getClass().getResource("/images/frame.png"));
		this.image = image;
		
		setOpaque(false);		
		setMaximumSize(new Dimension(WIDTH, HEIGHT));
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		
		setLayout(new BorderLayout(5, 5));
		
		ImageIcon icon = new ImageIcon(image);
		
		int mayor = icon.getIconWidth() < icon.getIconHeight() ? icon.getIconHeight() : icon.getIconWidth();
		if(mayor > WIDTH - BORDER){
			int difference = mayor - (WIDTH - BORDER);		//WIDTH = HEIGHT
			int differencePercent = difference * 100 / mayor;
			int percent = 100 - differencePercent;
			int width = icon.getIconWidth() * percent / 100;
			int height = icon.getIconHeight() * percent / 100;
			icon = Utilities.scaleImageIcon(icon, width, height);
		}
		
		JLabel tag = new JLabel(icon);
		tag.setHorizontalAlignment(JLabel.CENTER);
		tag.setVerticalAlignment(JLabel.CENTER);
		
		add(tag, BorderLayout.CENTER);
		
		//PopupMenu
		popup = new JPopupMenu();
		JMenuItem item = new JMenuItem("Guardar");
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				savePicture();
			}
		});
		popup.add(item);
		popup.addSeparator();
		item = new JMenuItem("Eliminar");
		item.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.remove(ImageTumbail.this);
				parent.revalidate();
				parent.repaint();
			}
		});
		popup.add(item);
		setComponentPopupMenu(popup);
		
	}
	
	private void savePicture(){
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		ExtendedFileFilter filter = new ExtendedFileFilter("jpg", new String[]{"JPG"});
		chooser.addChoosableFileFilter(filter);
		filter = new ExtendedFileFilter("jpeg", new String[]{"JPEG"});
		chooser.addChoosableFileFilter(filter);
		filter = new ExtendedFileFilter("png", new String[]{"PNG"});
		chooser.addChoosableFileFilter(filter);
		int status = chooser.showSaveDialog(null);
		if(status == JFileChooser.APPROVE_OPTION){
			filter = (ExtendedFileFilter)chooser.getFileFilter();
			String extension = filter.getExtensions()[0];
			File file = chooser.getSelectedFile();
			
			/*StringBuffer file = new StringBuffer(chooser.getSelectedFile().getAbsolutePath());
			if(!Utilities.haveExtension(file.toString(), extension))
				file.append("."+extension);	
			
			StringBuffer definityFile = new StringBuffer(file.toString());
			if(new File(file.toString()).exists()){
				System.out.println("existe fichero");
				if(!lastNameExisted.equals(file.toString())){
					System.out.println("se tenia guardado");
					lastNameExisted = file.toString();
					count = 0;					
				}
				else{
					count++;
					definityFile.insert(definityFile.length() - 4, "_"+count);
				}
			}*/
			
			try {
				ImageIO.write(image, extension, new File(file.getAbsolutePath()+"."+extension));
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(getParent(), "No pudo ser salvada la imagen", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	
	public void paint(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(BACKGROUND.getImage(), 0, 0, WIDTH, HEIGHT, null);
		
		super.paintComponent(g2d);
		super.paintComponents(g2d);
	}

}
