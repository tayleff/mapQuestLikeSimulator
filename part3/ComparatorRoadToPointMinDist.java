package cmsc420.meeshquest.part3;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Comparator;

public class ComparatorRoadToPointMinDist implements Comparator<City> {

	Line2D.Double line;
	public ComparatorRoadToPointMinDist(Road road) {
		this.line = road.getRoad();
	}

	@Override
	public int compare(City o1, City o2) {
		if (getDist(o1.toLocalPoint2D(), line) > getDist(o2.toLocalPoint2D(), line)) return 1;
		if (getDist(o1.toLocalPoint2D(), line) < getDist(o2.toLocalPoint2D(), line)) return -1;
		return 0;
	}
	
	public double getDist(Point2D.Double point, Line2D.Double road) {
		return road.ptSegDist(point);
	}
}
