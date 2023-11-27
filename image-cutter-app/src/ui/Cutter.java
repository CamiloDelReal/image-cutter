/*
 * Created by JFormDesigner on Mon Jan 16 00:23:53 CST 2012
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ui.util.Canvas;
import ui.util.ExtendedFileFilter;
import ui.util.ImageList;
import ui.util.ImageTumbail;
import ui.util.LabelToChooser;

/**
 * @author Camilo José del Real Martell
 */
public class Cutter extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3652820250434795985L;
	private Canvas canvas;
	private JFileChooser chooser;
	private ImageList imageList;

	public Cutter() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		imageList = new ImageList();
		
		initComponents();
		
		ImageTumbail.parent = trimsPanel;
		
		chooser = new JFileChooser();
		chooser.setDialogTitle("Seleccione una imagen");
		chooser.setAcceptAllFileFilterUsed(false);
		ExtendedFileFilter filter = new ExtendedFileFilter("jpg", new String[]{"JPG"});
		chooser.addChoosableFileFilter(filter);
		filter = new ExtendedFileFilter("jpeg", new String[]{"JPEG"});
		chooser.addChoosableFileFilter(filter);
		filter = new ExtendedFileFilter("png", new String[]{"PNG"});
		chooser.addChoosableFileFilter(filter);
		filter = new ExtendedFileFilter("jpg, jpeg, png", new String[]{"JPG", "JPEG", "PNG"});
		chooser.addChoosableFileFilter(filter);
		LabelToChooser lab = new LabelToChooser(chooser);
		chooser.setAccessory(lab);
	}
	
	private void actionExit(ActionEvent e) {
		System.exit(0);
	}

	private void openToFindPicture(ActionEvent e) {
		int status = chooser.showOpenDialog(null);
		if(status == JFileChooser.APPROVE_OPTION){			
			File file = chooser.getSelectedFile();
			BufferedImage imageDisplayed = null;
			try {
				imageDisplayed = ImageIO.read(file);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int percent = slider1.getValue();
			if(canvas == null){
				canvas = new Canvas(imageDisplayed, percent, coordenate, selection);
				canvas.setComponentPopupMenu(popupMenu1);
				
				imagePanel.add(canvas);
			}
			else{
				canvas.setPicture(imageDisplayed);
				canvas.setPercentToDraw(percent);
			}
			imagePanel.revalidate();
			imagePanel.repaint();
		}
	}

	private void clearCanvas(ActionEvent e) {
		if(canvas != null){
			canvas.clearSelections();
			imagePanel.repaint();
		}
	}

	private void percentChanged(ChangeEvent e) {
		int percent = slider1.getValue();
		if(canvas != null){
			canvas.setPercentToDraw(percent);
			imagePanel.revalidate();
		}
	}

	private void frameResized(ComponentEvent e) {
		int width = getWidth();
		int height = getHeight();
		
		if(width < 550)
			width = 550;
		if(height < 470)
			height = 470;
		setSize(width, height);
		
		Insets insets = getInsets();
		
		int widthAux = width - insets.left - insets.right;
		int columns = (widthAux - 10) / (160 + 10);
		if(columns <= 0)
			columns = 1;
		int count = imageList.getImageTumbails().size();
		int rows = count / columns;		
		if(count % columns != 0)
			rows++;		
		int heightAux = rows * (160 + 10) + 10;
		
		trimsPanel.setMaximumSize(new Dimension(widthAux, heightAux));
		trimsPanel.setMinimumSize(new Dimension(widthAux, heightAux));
		trimsPanel.setPreferredSize(new Dimension(widthAux, heightAux));
		trimsPanel.revalidate();
		
	}

	private void trimPicture(ActionEvent e) {
		BufferedImage picture = canvas.trimPicture();
		if(picture != null){
			ImageTumbail tumb = new ImageTumbail(picture);
			imageList.getImageTumbails().add(tumb);
			trimsPanel.add(tumb);
		}
		else
			JOptionPane.showMessageDialog(this, "Debe realizar una seleccion", "Alerta", JOptionPane.INFORMATION_MESSAGE);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		panel1 = new JPanel();
		button3 = new JButton();
		button2 = new JButton();
		button4 = new JButton();
		button1 = new JButton();
		tabbedPane1 = new JTabbedPane();
		panel5 = new JPanel();
		imagePanelActions = new JPanel();
		slider1 = new JSlider();
		coordenate = new JLabel();
		selection = new JLabel();
		separator2 = new JSeparator();
		separator3 = new JSeparator();
		scrollPane1 = new JScrollPane();
		imagePanel = new JPanel();
		panel2 = new JPanel();
		trimsPanelActions = new JPanel();
		button5 = new JButton();
		scrollPane2 = new JScrollPane();
		trimsPanel = new JPanel();
		popupMenu1 = new JPopupMenu();
		menuItem1 = new JMenuItem();
		menuItem2 = new JMenuItem();

		//======== this ========
		setTitle("Cortadora de im\u00e1genes");
		setIconImage(new ImageIcon(getClass().getResource("/images/picture_32x32_img.png")).getImage());
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				frameResized(e);
			}
		});
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== panel1 ========
		{
			panel1.setLayout(new FlowLayout(FlowLayout.RIGHT));

			//---- button3 ----
			button3.setText("Abrir");
			button3.setIcon(new ImageIcon(getClass().getResource("/images/open_img.png")));
			button3.setPreferredSize(new Dimension(115, 30));
			button3.setMinimumSize(new Dimension(115, 30));
			button3.setMaximumSize(new Dimension(115, 30));
			button3.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					openToFindPicture(e);
				}
			});
			panel1.add(button3);

			//---- button2 ----
			button2.setText("Limpiar");
			button2.setIcon(new ImageIcon(getClass().getResource("/images/trash_img.png")));
			button2.setPreferredSize(new Dimension(115, 30));
			button2.setMinimumSize(new Dimension(115, 30));
			button2.setMaximumSize(new Dimension(115, 30));
			button2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clearCanvas(e);
				}
			});
			panel1.add(button2);

			//---- button4 ----
			button4.setText("Acerca de ...");
			button4.setIcon(new ImageIcon(getClass().getResource("/images/about_img.png")));
			button4.setMaximumSize(new Dimension(115, 30));
			button4.setMinimumSize(new Dimension(115, 30));
			button4.setPreferredSize(new Dimension(115, 30));
			panel1.add(button4);

			//---- button1 ----
			button1.setText("Salir");
			button1.setIcon(new ImageIcon(getClass().getResource("/images/exit_img.png")));
			button1.setPreferredSize(new Dimension(115, 30));
			button1.setMinimumSize(new Dimension(115, 30));
			button1.setMaximumSize(new Dimension(115, 30));
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					actionExit(e);
				}
			});
			panel1.add(button1);
		}
		contentPane.add(panel1, BorderLayout.SOUTH);

		//======== tabbedPane1 ========
		{

			//======== panel5 ========
			{
				panel5.setLayout(new BorderLayout(5, 5));

				//======== imagePanelActions ========
				{
					imagePanelActions.setPreferredSize(new Dimension(560, 55));

					//---- slider1 ----
					slider1.setValue(100);
					slider1.setMajorTickSpacing(10);
					slider1.setMinorTickSpacing(10);
					slider1.setMinimum(10);
					slider1.setPreferredSize(new Dimension(200, 37));
					slider1.setPaintLabels(true);
					slider1.addChangeListener(new ChangeListener() {
						@Override
						public void stateChanged(ChangeEvent e) {
							percentChanged(e);
						}
					});

					//---- coordenate ----
					coordenate.setText("(000;000)");
					coordenate.setToolTipText("Coordenadas (x,y)");

					//---- selection ----
					selection.setText("(000,000,000,000)");
					selection.setToolTipText("Selecci\u00f3n (x,y,ancho,alto)");

					GroupLayout imagePanelActionsLayout = new GroupLayout(imagePanelActions);
					imagePanelActions.setLayout(imagePanelActionsLayout);
					imagePanelActionsLayout.setHorizontalGroup(
						imagePanelActionsLayout.createParallelGroup()
							.addComponent(separator3, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
							.addGroup(imagePanelActionsLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(coordenate, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
								.addGap(18, 18, 18)
								.addComponent(selection, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
								.addComponent(slider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap())
							.addComponent(separator2, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
					);
					imagePanelActionsLayout.setVerticalGroup(
						imagePanelActionsLayout.createParallelGroup()
							.addGroup(GroupLayout.Alignment.TRAILING, imagePanelActionsLayout.createSequentialGroup()
								.addComponent(separator2, GroupLayout.DEFAULT_SIZE, 2, Short.MAX_VALUE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(imagePanelActionsLayout.createParallelGroup()
									.addComponent(slider1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGroup(imagePanelActionsLayout.createSequentialGroup()
										.addGap(14, 14, 14)
										.addGroup(imagePanelActionsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
											.addComponent(selection)
											.addComponent(coordenate))))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(separator3, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE))
					);
				}
				panel5.add(imagePanelActions, BorderLayout.SOUTH);

				//======== scrollPane1 ========
				{
					scrollPane1.setBorder(null);

					//======== imagePanel ========
					{
						imagePanel.setBorder(null);
						imagePanel.setLayout(new FlowLayout());
					}
					scrollPane1.setViewportView(imagePanel);
				}
				panel5.add(scrollPane1, BorderLayout.CENTER);
			}
			tabbedPane1.addTab("Imagen", panel5);


			//======== panel2 ========
			{
				panel2.setLayout(new BorderLayout(5, 5));

				//======== trimsPanelActions ========
				{
					trimsPanelActions.setLayout(new FlowLayout(FlowLayout.RIGHT));

					//---- button5 ----
					button5.setText("Hay que ver que ponemos");
					trimsPanelActions.add(button5);
				}
				panel2.add(trimsPanelActions, BorderLayout.SOUTH);

				//======== scrollPane2 ========
				{
					scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					scrollPane2.setBorder(null);

					//======== trimsPanel ========
					{
						trimsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
					}
					scrollPane2.setViewportView(trimsPanel);
				}
				panel2.add(scrollPane2, BorderLayout.CENTER);
			}
			tabbedPane1.addTab("Recortes", panel2);

		}
		contentPane.add(tabbedPane1, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());

		//======== popupMenu1 ========
		{

			//---- menuItem1 ----
			menuItem1.setText("Recortar");
			menuItem1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					trimPicture(e);
				}
			});
			popupMenu1.add(menuItem1);
			popupMenu1.addSeparator();

			//---- menuItem2 ----
			menuItem2.setText("Limpiar");
			menuItem2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					clearCanvas(e);
				}
			});
			popupMenu1.add(menuItem2);
		}
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
		
		setIconToTabs();

		button4.setVisible(false);
		button5.setVisible(false);
	}
	
	private void setIconToTabs(){
		//Icon to tab "Imagen"
		JLabel tag = new JLabel("Imagen", new ImageIcon(getClass().getResource("/images/picture_32x32_img.png")), JLabel.LEFT);
		tabbedPane1.setTabComponentAt(0, tag);
		
		//Icon to tab "Recortes"
		tag = new JLabel("Recortes", new ImageIcon(getClass().getResource("/images/picture_list_32x32_img.png")), JLabel.LEFT);
		tabbedPane1.setTabComponentAt(1, tag);
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel panel1;
	private JButton button3;
	private JButton button2;
	private JButton button4;
	private JButton button1;
	private JTabbedPane tabbedPane1;
	private JPanel panel5;
	private JPanel imagePanelActions;
	private JSlider slider1;
	private JLabel coordenate;
	private JLabel selection;
	private JSeparator separator2;
	private JSeparator separator3;
	private JScrollPane scrollPane1;
	private JPanel imagePanel;
	private JPanel panel2;
	private JPanel trimsPanelActions;
	private JButton button5;
	private JScrollPane scrollPane2;
	private JPanel trimsPanel;
	private JPopupMenu popupMenu1;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
