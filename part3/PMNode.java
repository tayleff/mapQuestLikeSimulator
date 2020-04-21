package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.PriorityQueue;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;

public abstract class PMNode {
	public static final int WHITE = 0;
	public static final int BLACK = 1;
	public static final int GREY = 2;
	protected final int type;

	protected PMNode (final int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public abstract PMNode insert(MapStruct struct, Double origin, int width, int height, PMValidator validator) throws RoadViolatesPMThrowable; 
	public abstract PMNode delete(MapStruct struct, Double origin, int width, int height, PMValidator validator) ;
	public abstract Element printTree(Document results, Element ele);

	public abstract void drawIt(CanvasPlus canvas) ;
		
}
