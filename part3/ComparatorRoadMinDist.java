package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class ComparatorRoadMinDist implements Comparator<Road>{
	protected Point2D.Float point;
	
	public ComparatorRoadMinDist(Point2D.Float point) {
		this.point = point;
	}
	
	@Override
	public int compare(Road o1, Road o2) {
		if (getDist(point, o1) > getDist(point , o2)) return 1;
		if (getDist(point, o1) < getDist(point, o2)) return -1;
		return 0;
	}
	
	public double getDist(Point2D.Float point, Road road) {
		return road.getRoad().ptSegDist(point);
	}
}
