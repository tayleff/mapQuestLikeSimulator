package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class ComparatorCityMinDist implements Comparator<City>{
	protected Point2D.Float point;
	
	public ComparatorCityMinDist(Point2D.Float point) {
		this.point = point;
	}
	
	@Override
	public int compare(City o1, City o2) {
		if (getDist(point, o1) > getDist(point , o2)) return 1;
		if (getDist(point, o1) < getDist(point, o2)) return -1;
		return 0;
	}
	
	public double getDist(Point2D.Float point, City city) {
		return Math.sqrt(Math.pow((point.getX() - city.getLocalX()), 2) + Math.pow((point.getY() - city.getLocalY()), 2));
	}
}
