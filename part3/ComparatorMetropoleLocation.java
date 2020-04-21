package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.Comparator;

public class ComparatorMetropoleLocation implements Comparator<PMQuadtree>{

		@Override
		public int compare(PMQuadtree quad1, PMQuadtree quad2) {
			Point2D o1 = quad1.getLocation();
			Point2D o2 = quad2.getLocation();
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

