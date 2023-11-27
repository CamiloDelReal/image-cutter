package ui.util;


import java.awt.Point;
import java.awt.Rectangle;

public class Selection {
	private Rectangle selectedArea;

	private Rectangle topFrame;
	private Rectangle leftFrame;
	private Rectangle bottomFrame;
	private Rectangle rightFrame;

	private static final int EXTERNAL_BORDER = 2;
	private static final int INTERNAL_BORDER = 1;

	private static final int WIDTH = 4;
	private static final int HEIGHT = 4;

	public Selection(Rectangle selectedArea) {
		this.selectedArea = selectedArea;

		topFrame = new Rectangle(selectedArea.x - EXTERNAL_BORDER,
								 selectedArea.y - EXTERNAL_BORDER, 
								 selectedArea.width + EXTERNAL_BORDER * 2, // Se multiplica por 2 porque se pierden 2px al correr el valor de x
								 HEIGHT); 

		leftFrame = new Rectangle(selectedArea.x - EXTERNAL_BORDER,
								  selectedArea.y - EXTERNAL_BORDER,
								  WIDTH,
								  selectedArea.height + EXTERNAL_BORDER * 2);

		bottomFrame = new Rectangle(selectedArea.x - EXTERNAL_BORDER,
									(selectedArea.y + selectedArea.height) - INTERNAL_BORDER,
									selectedArea.width + EXTERNAL_BORDER * 2,
									HEIGHT);

		rightFrame = new Rectangle(	(selectedArea.x + selectedArea.width) - INTERNAL_BORDER,
									selectedArea.y - EXTERNAL_BORDER,
									WIDTH,
									selectedArea.height + EXTERNAL_BORDER * 2);
	}

	public Selection(int x, int y, int width, int height) {
		this(new Rectangle(x, y, width, height));
	}
	
	public boolean isEmpty(){
		return (selectedArea.x == 0 && selectedArea.y == 0 && selectedArea.width == 0 && selectedArea.height == 0);
	}
	
	public void makeEmpty(){
		selectedArea.x = 0;
		selectedArea.y = 0;
		selectedArea.width = 0;
		selectedArea.height = 0;
	}

	public Rectangle getSelectedArea() {
		return selectedArea;
	}
	public Rectangle getTopFrame() {
		return topFrame;
	}
	public Rectangle getLeftFrame() {
		return leftFrame;
	}
	public Rectangle getBottomFrame() {
		return bottomFrame;
	}
	public Rectangle getRightFrame() {
		return rightFrame;
	}
	
	
	public void change(Point point, Position position){		
		if( position.equals(Position.TOP_RIGHT) && (point.y + INTERNAL_BORDER < bottomFrame.y) && (point.x - INTERNAL_BORDER > leftFrame.x + leftFrame.width) ){
			adjustTop(point);
			adjustRight(point);
		}
		else if( position.equals(Position.LEFT_TOP) && (point.x + INTERNAL_BORDER < rightFrame.x) && (point.y + INTERNAL_BORDER < bottomFrame.y) ){
			adjustLeft(point);
			adjustTop(point);
		}
		else if( position.equals(Position.RIGHT_BOTTOM) && (point.x - INTERNAL_BORDER > leftFrame.x + leftFrame.width) && (point.y - INTERNAL_BORDER > topFrame.y + topFrame.height) ){
			adjustRight(point);
			adjustBottom(point);
		}
		else if( position.equals(Position.BOTTOM_LEFT) && (point.y - INTERNAL_BORDER > topFrame.y + topFrame.height) && (point.x + INTERNAL_BORDER < rightFrame.x) ){
			adjustBottom(point);
			adjustLeft(point);
		}
		else if( position.equals(Position.TOP) && (point.y + INTERNAL_BORDER < bottomFrame.y) ){
			adjustTop(point);			
		}
		else if( position.equals(Position.LEFT) && (point.x + INTERNAL_BORDER < rightFrame.x) ){
			adjustLeft(point);			
		}
		else if( position.equals(Position.BOTTOM) && (point.y - INTERNAL_BORDER > topFrame.y + topFrame.height) ){
			adjustBottom(point);			
		}
		else if( position.equals(Position.RIGHT) && (point.x - INTERNAL_BORDER > leftFrame.x + leftFrame.width) ){
			adjustRight(point);			
		}
		
		changeFrame();
	}
	
	private void adjustTop(Point point){
		int difference = point.y - selectedArea.y;
		
		selectedArea.y = point.y;
		selectedArea.height -= difference;
	}
	private void adjustLeft(Point point){
		int difference = point.x - selectedArea.x;
		
		selectedArea.x = point.x;
		selectedArea.width -= difference;
	}
	private void adjustBottom(Point point){
		int difference = point.y - (selectedArea.y + selectedArea.height);
		
		selectedArea.height += difference;
	}
	private void adjustRight(Point point){
		int difference = point.x - (selectedArea.x + selectedArea.width);
		
		selectedArea.width += difference;
	}
	
	public void changeFrame(){
		topFrame.x 			= selectedArea.x - EXTERNAL_BORDER;
		topFrame.y 			= selectedArea.y - EXTERNAL_BORDER;
		topFrame.width 		= selectedArea.width + EXTERNAL_BORDER * 2;
		topFrame.height		= HEIGHT;
		
		leftFrame.x 		= selectedArea.x - EXTERNAL_BORDER;
		leftFrame.y 		= selectedArea.y - EXTERNAL_BORDER;
		leftFrame.width 	= WIDTH;
		leftFrame.height 	= selectedArea.height + EXTERNAL_BORDER * 2;
		
		bottomFrame.x 		= selectedArea.x - EXTERNAL_BORDER;
		bottomFrame.y 		= (selectedArea.y + selectedArea.height) - INTERNAL_BORDER;
		bottomFrame.width 	= selectedArea.width + EXTERNAL_BORDER * 2;
		bottomFrame.height 	= HEIGHT;
		
		rightFrame.x 		= (selectedArea.x + selectedArea.width) - INTERNAL_BORDER;
		rightFrame.y 		= selectedArea.y - EXTERNAL_BORDER;
		rightFrame.width 	= WIDTH;
		rightFrame.height 	= selectedArea.height + EXTERNAL_BORDER * 2;
	}

	public void moving(int x, int y){
		selectedArea.x = x;
		selectedArea.y = y;
		
		changeFrame();
	}
	
}
