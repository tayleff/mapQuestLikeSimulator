package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.util.PriorityQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

public class PMGrey extends PMNode{

	//width of the region being partitioned
	protected int width;
	
	//height of the region being partitioned
	protected int height;
	
	//left most point of the region being partitioned
	protected Point2D.Double botLeft;
	
	//new left most points of the four child regions
	protected Point2D.Double[] leftBottom;
	
	//pointers to the children nodes
	protected PMNode[] children;
	
	//counter
	protected int i;
	
	//half the width of the region being partitioned 
	protected int halfWidth;
	
	//half the height of the region being partitioned 
	protected int halfHeight;
	
	//list of the regions within the original region
	protected Rectangle2D.Double[] regions;

	protected CanvasPlus canvas;
	
	//constructor for a Grey Node
	protected PMGrey(Point2D.Double origin, int width, int height) {
		super(GREY);
		
		this.botLeft = origin;
		this.width = width;
		this.height = height;
		
		children = new PMNode[4];
		for (i = 0; i < 4 ; i++) {
			children[i] = PMWhite.getInstance();
		}
		
		halfWidth = width /2;
		halfHeight = height/2;
		
		leftBottom = new Point2D.Double[4];
		leftBottom[0] = new Point2D.Double(origin.x, origin.y + halfHeight);
		leftBottom[1] = new Point2D.Double(origin.x + halfWidth, origin.y + halfHeight);
		leftBottom[2] = new Point2D.Double(origin.x, origin.y);
		leftBottom[3] = new Point2D.Double(origin.x + halfWidth, origin.y);
		
		regions = new Rectangle2D.Double[4];
		for (i = 0; i < 4; i++) {
			regions[i] = new Rectangle2D.Double(leftBottom[i].x, leftBottom[i].y, halfWidth, halfHeight);
		}


	}

	
	//inserts a Node into one of its partitioned regions
	@Override
	public PMNode insert(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator) throws RoadViolatesPMThrowable {

		if (struct.getType() == MapStruct.CITY) {
			City city = (City) struct;
			for(i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(city.toLocalPoint2D(), regions[i])) {
					children[i] = children[i].insert(city, leftBottom[i], halfWidth, halfHeight, validator);
				}
			}
		} else if (struct.getType() == MapStruct.ROAD) {
			Road road = (Road) struct;
			for(i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(road.getRoad(), regions[i])) {		
					children[i] = children[i].insert(road, leftBottom[i], halfWidth, halfHeight, validator);
				}
			}
		}
		
		return this;
	}

	@Override
	public PMNode delete(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator){

		// Removes mapstruct from children
		if (struct.getType() == MapStruct.CITY) {
			City city = (City) struct;
			for(i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(city.toLocalPoint2D(), regions[i])) {
					children[i] = children[i].delete(city, leftBottom[i], halfWidth, halfHeight, validator);
				}
			}
		} else if (struct.getType() == MapStruct.ROAD) {
			Road road = (Road) struct;
			for(i = 0; i < 4; i++) {
				if (Inclusive2DIntersectionVerifier.intersects(road.getRoad(), regions[i])) {		
					children[i] = children[i].delete(road, leftBottom[i], halfWidth, halfHeight, validator);
				}
			}
		}

		int numWhite = numWhite();
		int numBlack = numBlack();
		// Check all white
		if (numWhite == 4) {
			return PMWhite.getInstance();
		}

		// Check if one is black
		if (numWhite == 3 && numBlack == 1) {
			for (int i = 0; i < 4; i++) {
				if (children[i].type == PMNode.BLACK)
					return children[i];
			}
		}

		// if not all grey, put all structs in a single black node
		if (numGrey() != 4) {
			PMBlack b = new PMBlack();
			for (int i = 0; i < 4; i++) {
				if (children[i].type == PMNode.BLACK) {
					PMBlack b2 = (PMBlack) children[i];
					for (MapStruct s : b2.getStructs()) {
						try {
							b.insert(s, origin, width, height, validator);
						} catch (RoadViolatesPMThrowable e) {

						}
					}
				}
			}
			if (validator.valid(b)) {
				return b;
			}
		}
		return this;
	}
	
	@Override
	public Element printTree(Document results, Element ele) {
		Element greyEle = results.createElement("gray");
		greyEle.setAttribute("x", Integer.toString(getCenterX()));
		greyEle.setAttribute("y", Integer.toString(getCenterY()));
		
		Element childEle;
		for(i = 0; i < 4 ; i++) {
			childEle = children[i].printTree(results, greyEle);
			greyEle.appendChild(childEle);
		}

		return greyEle;
	}
	
	public int numWhite() {
		int count = 0;
		
		for (i = 0; i < 4; i++) {
			if (children[i] == PMWhite.getInstance()) {
				count++;
			}
		}
		return count;
	}

	public int numBlack() {
		int count = 0;
		
		for (i = 0; i < 4; i++) {
			if (children[i].getType() == PMNode.BLACK) {
				count++;
			}
		}
		return count;
	}
	
	public int numGrey() {
		int count = 0;
		
		for (i = 0; i < 4; i++) {
			if (children[i].getType() == PMNode.GREY) {
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
	
	public void drawIt(CanvasPlus canvas) {	
		if (canvas != null) {
            canvas.addCross(getCenterX(), getCenterY(), halfWidth, Color.GRAY);
		}
		for(i = 0; i < 4 ; i++) {
			children[i].drawIt(canvas);
		}
	}
}
