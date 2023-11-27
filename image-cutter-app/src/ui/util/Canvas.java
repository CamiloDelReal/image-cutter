package ui.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Canvas extends JPanel implements MouseMotionListener, MouseListener{
	private static final long serialVersionUID = 54878456146541L;

	private BufferedImage originalPicture; 		// Imagen original selecionada por el usuario
	private BufferedImage paintedPicture; 	// Buffer donde se dibujara la imagen original para presentarla en pantalla y hacerle los recortes
											//Tecnica del buffer doble
	private int percentToDraw;

	private boolean drag = false;			//controla si se tiene una seleccion en pantalla

	private static final float stroke = 1f;	//Ancho del marco de seleccion
	
	private Selection selection;			//Cuadro de seleccion
	private Position position;				//Controla la ubicacion donde se encuentra el cursor al posicionarse sobre el area seleccionada, 
											//se emplea en las ampliaciones del area seleccionada y al mover esta

	private Point init = null;				//Punto donde se comienza a arrartrar el mouse, se emplea para mover y crear nuevas selecciones
	private Point end = null;				//Punto donde se libera el mouse de un arrartre, se emplea para mover y crear nuevas selecciones
	
	private JLabel coordenateDisplay;
	private JLabel selectionDisplay;
	
	public Canvas(BufferedImage image, int percent, JLabel coordenateDisplay, JLabel selectionDisplay) {
		super();
		this.originalPicture = image;
		this.percentToDraw = percent;
		this.coordenateDisplay = coordenateDisplay;
		this.selectionDisplay = selectionDisplay;
		if(originalPicture != null){
			setPreferredSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));		//El area de dibujo tendra por defecto el tamaño de la imagen
			setMinimumSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));
			setMaximumSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));
		}
		addMouseMotionListener(this);
		addMouseListener(this);
		selection = new Selection(new Rectangle(0, 0, 0, 0));
	}
	
	public void setPercentToDraw(int percent){
		percentToDraw = percent;
		if(originalPicture != null){
			setPreferredSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));		//El area de dibujo tendra por defecto el tamaño de la imagen
			setMinimumSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));
			setMaximumSize(new Dimension(originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100));
		}
		
		Rectangle rect = selection.getSelectedArea();
		rect.x = 0;
		rect.y = 0;
		rect.width = 0;
		rect.height = 0;
		selection.changeFrame();
	}
	
	public void setPicture(BufferedImage picture){
		this.originalPicture = picture;
		this.percentToDraw = 100;
		if(originalPicture != null){
			setPreferredSize(new Dimension(originalPicture.getWidth(), originalPicture.getHeight()));
			setMinimumSize(new Dimension(originalPicture.getWidth(), originalPicture.getHeight()));
			setMaximumSize(new Dimension(originalPicture.getWidth(), originalPicture.getHeight()));
		}
	}
	
	public BufferedImage getPicture(){
		return originalPicture;
	}
	
	public void clearSelections(){
		selection.makeEmpty();
	}
	
	public BufferedImage trimPicture(){
		BufferedImage picture = null;
		
		if(!selection.isEmpty()){
			Rectangle rect = selection.getSelectedArea();
			
			if(percentToDraw < 100){
				Image image = Utilities.scaleImageIcon(originalPicture, originalPicture.getWidth() * percentToDraw / 100, originalPicture.getHeight() * percentToDraw / 100);
				picture = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
				picture.getGraphics().drawImage(image, 0, 0, null);
				if(rect.width > image.getWidth(null))
					rect.width = image.getWidth(null);
				if(rect.height > image.getHeight(null))
					rect.height = image.getHeight(null);
				picture = picture.getSubimage(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
			}
			else{
				picture = originalPicture.getSubimage(rect.x + 1, rect.y + 1, rect.width - 2, rect.height - 2);
			}
		}
		
		return picture;
	}
	
	
	public void paint(Graphics g) {
		paintedPicture = new BufferedImage(getWidth(), getHeight(),	BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = (Graphics2D) paintedPicture.getGraphics();
		g2d.setColor(Color.WHITE);

		if(originalPicture != null){
			g2d.drawImage(originalPicture, 0, 0, getWidth(), getHeight(), this);
			
			g2d.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 2f));
			
			Rectangle rect = selection.getSelectedArea();
			
			g2d.setColor(new Color(200, 0, 0, 100));
			g2d.fillRect(rect.x, rect.y, rect.width, rect.height);
			
			g2d.setColor(new Color(200, 0, 0));		
			g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
			
			if(selectionDisplay != null)
				selectionDisplay.setText("(" + rect.x + "," + rect.y + "," + rect.width + "," +rect.height +")");
		}
		
		g.drawImage(paintedPicture, 0, 0, this);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(drag){
			if(position.equals(Position.CENTER)){	//Moving
				end = e.getPoint();
				
				int distanceX = end.x - init.x;
				int distanceY = end.y - init.y;
				
				int positionX = selection.getSelectedArea().x + distanceX;
				int positionY = selection.getSelectedArea().y + distanceY;
				
				if(positionX < 0)
					positionX = 0;
				if(positionY < 0)
					positionY = 0;
				if( (positionX + selection.getSelectedArea().width) > getWidth())
					positionX = getWidth() - selection.getSelectedArea().width;
				if( (positionY + selection.getSelectedArea().height) > getHeight())
					positionY = getHeight() - selection.getSelectedArea().height;
				
				init = end;
				
				selection.moving(positionX, positionY);
			}
			else{									//Resizing
				selection.change(e.getPoint(), position);
			}
			
			repaint();
		}
		else{		
			end = e.getPoint();
			Rectangle rect = selection.getSelectedArea();
			rect.x = init.x < end.x ? init.x : end.x;
			rect.y = init.y < end.y ? init.y : end.y;
			
			int width = end.x - init.x;
			int height = end.y - init.y;
			
			if(width == 0)
				width = 1;
			else if(width < 0)
				width *= -1;
			
			if(height == 0)
				height = 1;
			else if(height < 0)
				height *= -1;
			
			rect.width = width;
			rect.height = height;
			
			selection.changeFrame();

			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		if(selection.getSelectedArea().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			drag = true;
			position = Position.CENTER;
		}
		else if(selection.getTopFrame().contains(e.getPoint()) && selection.getLeftFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
			drag = true;
			position = Position.LEFT_TOP;
		}
		else if(selection.getTopFrame().contains(e.getPoint()) && selection.getRightFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
			drag = true;
			position = Position.TOP_RIGHT;
		}
		else if(selection.getBottomFrame().contains(e.getPoint()) && selection.getLeftFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
			drag = true;
			position = Position.BOTTOM_LEFT;
		}
		else if(selection.getBottomFrame().contains(e.getPoint()) && selection.getRightFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
			drag = true;
			position = Position.RIGHT_BOTTOM;
		}
		else if(selection.getTopFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
			drag = true;
			position = Position.TOP;			
		}
		else if(selection.getLeftFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
			drag = true;
			position = Position.LEFT;
		}
		else if(selection.getBottomFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
			drag = true;
			position = Position.BOTTOM;
		}
		else if(selection.getRightFrame().contains(e.getPoint())){
			Canvas.this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
			drag = true;
			position = Position.RIGHT;
		}
		else{
			Canvas.this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			drag = false;
		}
		
		if(coordenateDisplay != null)
			coordenateDisplay.setText("(" + e.getPoint().x +";"+ e.getPoint().y + ")");
	}

	@Override
	public void mouseClicked(MouseEvent e) {;}

	@Override
	public void mouseEntered(MouseEvent e) {;}

	@Override
	public void mouseExited(MouseEvent e) {;}

	@Override
	public void mousePressed(MouseEvent e) {
		if(!drag && SwingUtilities.isLeftMouseButton(e)){
			Rectangle rect = selection.getSelectedArea();
			rect.x = 0;
			rect.y = 0;
			rect.width = 0;
			rect.height = 0;
			repaint();
		}
		init = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		init = null;
		end = null;
	}
}
