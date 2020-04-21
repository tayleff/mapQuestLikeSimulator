/**
 * 
 */
package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.PriorityQueue;
import java.awt.geom.Rectangle2D;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author taylorleffler
 *	https://www.javaworld.com/article/2073352/core-java/core-java-simply-singleton.html
 */
public class PRWhite extends PRNode{
	//creates the one instance of the White node
	private static PRWhite instance = null;

	//constructor for the White node
	protected PRWhite(){
		super(WHITE);
	}
	
	//creates a new black node in place of itself
	@Override
	public PRNode insert(Point2D point, Point2D.Double origin, int width, int height) {
		PRNode blackNode = new PRBlack();

		return blackNode.insert(point, origin, width, height);
	}

	@Override
	public PRNode delete(Point2D point, Point2D.Double origin, int width, int height) {
		// TODO Auto-generated method stub
		return instance;
	}

	@Override
	public void queueAdd(PriorityQueue<PRBlack> pq, Point2D.Double point) {
		
	}
	
	//creates the instance of itself if it has not already been created
	//returns the one instance of itself
	public static PRNode getInstance() {
		if (instance == null) {
			instance = new PRWhite();
		}
		
		return instance;
	}
}
