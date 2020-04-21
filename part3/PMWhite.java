package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.PriorityQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;

public class PMWhite extends PMNode{
	//creates the one instance of the White node
	private static PMWhite instance = null;

	//constructor for the White node
	protected PMWhite(){
		super(WHITE);
	}
	
	//creates a new black node in place of itself
	@Override
	public PMNode insert(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator) throws RoadViolatesPMThrowable {
		PMNode blackNode = new PMBlack();

		return blackNode.insert(struct, origin, width, height, validator);
	}

	@Override
	public PMNode delete(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator) {
		return instance;
	}

	@Override
	public Element printTree(Document results, Element ele) {
		return results.createElement("white");
	}
	
	//creates the instance of itself if it has not already been created
	//returns the one instance of itself
	public static PMNode getInstance() {
		if (instance == null) {
			instance = new PMWhite();
		}
		
		return instance;
	}

	@Override
	public void drawIt(CanvasPlus canvas) {
		// TODO Auto-generated method stub
		
	}
}

