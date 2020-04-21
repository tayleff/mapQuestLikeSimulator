package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.PriorityQueue;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Shape2DDistanceCalculator;

public class PRBlack extends PRNode {

	//city object located in the black node
	protected Point2D metropole;
	protected Rectangle2D.Double region;
	
	//constructor for the black node
	protected PRBlack() {
		super(BLACK);
	}
	
	//inserts the city into a new black node or creates a grey node that puts the old city and new city within its partitions
	//returns either itself if it was empty or a grey node if the black node already contained a city
	@Override
	public PRNode insert(Point2D point, Point2D.Double origin, int width, int height) {
		if (metropole == null) {
			metropole = point;
			region = new Rectangle2D.Double(origin.x, origin.y, width, height);

			return this;
		} else {
			PRGrey greyNode = new PRGrey(origin, width, height);
			greyNode.insert(point, origin, width, height);
			greyNode.insert(point, origin, width, height);

			return greyNode;
		}
	}

	@Override
	public PRNode delete(Point2D point, Point2D.Double origin, int width, int height) {
		if (metropole == point) {
			this.metropole = null;
			return PRWhite.getInstance();
		}
		return this;
	}
	
	@Override
	public void queueAdd(PriorityQueue<PRBlack> pq, Point2D.Double point) {
		pq.add(this);
	}
	
	public double getDist(Point2D.Float point) {
		return Math.sqrt(Math.pow((point.getX() - metropole.getX()), 2) + Math.pow((point.getY() - metropole.getY()), 2));
	}
	
	//returns the city object in the black node
	public Point2D getPoint() {
		return metropole;
	}

}
