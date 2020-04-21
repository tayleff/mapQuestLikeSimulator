package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.geom.Rectangle2D;
import java.util.PriorityQueue;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Shape2DDistanceCalculator;


public class PRGrey extends PRNode{

	//width of the region being partitioned
	protected int width;
	
	//height of the region being partitioned
	protected int height;
	
	//left most point of the region being partitioned
	protected Point2D.Double botLeft;
	
	//new left most points of the four child regions
	protected Point2D.Double[] leftBottom;
	
	//pointers to the children nodes
	protected PRNode[] children;
	
	//counter
	protected int i;
	
	//half the width of the region being partitioned 
	protected int halfWidth;
	
	//half the height of the region being partitioned 
	protected int halfHeight;
	
	//list of the regions within the original region
	protected Rectangle2D.Double[] regions;
	
	//constructor for a Grey Node
	protected PRGrey(Point2D.Double botLeft, int width, int height) {
		super(GREY);
		
		this.botLeft = botLeft;
		this.width = width;
		this.height = height;
		
		children = new PRNode[4];
		for (i = 0; i < 4 ; i++) {
			children[i] = PRWhite.getInstance();
		}
		
		halfWidth = width /2;
		halfHeight = height/2;
		
		leftBottom = new Point2D.Double[4];
		leftBottom[0] = new Point2D.Double(botLeft.x, botLeft.y + halfHeight);
		leftBottom[1] = new Point2D.Double(botLeft.x + halfWidth, botLeft.y + halfHeight);
		leftBottom[2] = new Point2D.Double(botLeft.x, botLeft.y);
		leftBottom[3] = new Point2D.Double(botLeft.x + halfWidth, botLeft.y);
		
		regions = new Rectangle2D.Double[4];
		for (i = 0; i < 4; i++) {
			regions[i] = new Rectangle2D.Double(leftBottom[i].x, leftBottom[i].y, halfWidth, halfHeight);
		}
//
//		if (canvas != null) {
//            canvas.addCross(getCenterX(), getCenterY(), halfWidth, Color.BLACK);
//		}
	}

	
	//inserts a Node into one of its partitioned regions
	@Override
	public PRNode insert(Point2D point, Point2D.Double origin, int width, int height) {
		for(i = 0; i < 4; i++) {
			if (intersects(point, regions[i])) {
				children[i] = children[i].insert(point, leftBottom[i], halfWidth, halfHeight);
			}
		}
		
		return this;
	}

	@Override
	public PRNode delete(Point2D point, Point2D.Double origin, int width, int height) {

		// removes city from grey node
		for(i = 0; i < 4; i++) {
			if (intersects(point, regions[i])) {
				children[i] = children[i].delete(point, leftBottom[i], halfWidth, halfHeight);
			}
		}

		//checks if node needs to be condensed
		if (numWhite() == 4) {
//			if (canvas != null) {
//				canvas.removeCross(getCenterX(), getCenterY(), halfWidth, Color.BLACK);
//			}
			return PRWhite.getInstance();
		} else if (numWhite() == 3 && numBlack() == 1) {
//			if (canvas != null) {
//				canvas.removeCross(getCenterX(), getCenterY(), halfWidth, Color.BLACK);
//			}
			
			for (i = 0; i < 4; i++) {
				if (children[i].getType() == PRNode.BLACK) {
					return children[i];
				}
			}	
		} 
		
		return this;
	}
	
	@Override
	public void queueAdd(PriorityQueue<PRBlack> pq,Point2D.Double point) {
		for(i = 0; i < 4 ; i++) {
			children[i].queueAdd(pq, point);
		}
	}
	
	public int numWhite() {
		int count = 0;
		
		for (i = 0; i < 4; i++) {
			if (children[i] == PRWhite.getInstance()) {
				count++;
			}
		}
		return count;
	}

	public int numBlack() {
		int count = 0;
		
		for (i = 0; i < 4; i++) {
			if (children[i].getType() == PRNode.BLACK) {
				count++;
			}
		}
		return count;
	}

	public int getCenterX() {
		return (int) botLeft.x + halfWidth;
	}
	
	public int getCenterY() {
		return (int) botLeft.y + halfHeight;
	}
	
	public static boolean intersects(Point2D point, Rectangle2D rect) {
		return (point.getX() >= rect.getMinX()
				&& point.getX() < rect.getMaxX()
				&& point.getY() >= rect.getMinY() && point.getY() < rect
				.getMaxY());
	}

}
