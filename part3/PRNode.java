package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.PriorityQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public abstract class PRNode {
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int GREY = 2;
	protected final int type;

	protected PRNode (final int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public abstract PRNode insert(Point2D point, Point2D.Double origin, int width, int height); 
	public abstract PRNode delete(Point2D point, Point2D.Double origin, int width, int height);
	public abstract void queueAdd(PriorityQueue<PRBlack> pq,Point2D.Double point);
}
