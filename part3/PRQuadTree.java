package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;

public class PRQuadTree {

	//root of the QuadTree
	protected PRNode root;
	
	//origin of the spatial map (0,0)
	protected Point2D.Double origin;
	
	//set of all city names mapped 	
	protected TreeSet<Point2D> metropoles;
	
	//reference to the White empty node singleton
	protected PRNode emptyNode = PRWhite.getInstance();
	
	protected static int spatialWidth;
	protected static int spatialHeight;
		
	protected static Rectangle2D.Double region;
	
	//creates PRQuadTree object
	public PRQuadTree() {
		root = emptyNode;
		origin = new Point2D.Double(0,0);
		metropoles = new TreeSet<Point2D>(new ComparatorCoordinates());
		region = new Rectangle2D.Double(origin.x, origin.y, spatialWidth, spatialHeight);
	}
	
	//sets spatialWidth and spatialheight
	@SuppressWarnings("static-access")
	public void setBounds(int width, int height) {
		this.spatialHeight = height;
		this.spatialWidth = width;
	}
	
	//adds first node to the QuadTree
	public void insert(Point2D point) {
		metropoles.add(point);
		root = root.insert(point, origin, spatialWidth, spatialHeight);
	}
	
	//adds first node to the QuadTree
	public void delete(Point2D point) {
		root = root.delete(point, origin, spatialWidth, spatialHeight);
	}
	
	//returns true if QuadTree has the city mapped
	public boolean contains(Point2D point) {
		if (metropoles.contains(point)) {
			return true;
		}
		return false;
	}
	
	//returns true if QuadTree is empty
	public boolean isEmpty() {
		if (root == emptyNode) {
			return true;
		}
		return false;
	}
	
	//returns the x coordinate of the origin (should always be 0)
	public int getMinX() {
		return (int) origin.x;
	}
	
	//returns the y coordinate of the origin (should always be 0)
	public int getMinY() {
		return (int) origin.y;
	}
	
	public int getMaxX() {
		return spatialWidth;
	}
	
	//returns the y coordinate of the origin (should always be 0)
	public int getMaxY() {
		return spatialWidth;
	}
	
	public Rectangle2D.Double getRegion(){
		return region;
	}
	
	//returns the root of the quadTree
	public PRNode getRoot() {
		return root;
	}

	public void clear() {
		metropoles.clear();
		root = emptyNode;
	}

	public void queueAdd(PriorityQueue<PRBlack> pq,Point2D.Double point) {
		root.queueAdd(pq, point);
	}
	
	public static boolean inBounds(Point2D point) {
		return (point.getX() >= region.getMinX()
				&& point.getX() < spatialWidth
				&& point.getY() >= region.getMinY() && point.getY() < spatialHeight);
	}
}
