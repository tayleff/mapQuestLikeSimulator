package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class ComparatorCoordinates implements Comparator<Point2D>{

	@Override
	public int compare(Point2D o1, Point2D o2) {
		int result = 0;
		
		if (o1.getY() > o2.getY()) {
			return 1;
		}
		
		if (o1.getY() < o2.getY()) {
			return -1;
		}
		
		if (o1.getX() > o2.getX()) {
			return 1;
		}
		
		if (o1.getX() < o2.getX()) {
			return -1;
		}
		
		return result;
	}

}
