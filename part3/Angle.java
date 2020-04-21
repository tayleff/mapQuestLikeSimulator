package cmsc420.meeshquest.part3;

import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;

public class Angle {

	Point2D.Double p1, p2, p3;
	
	public Angle(Point2D.Float p1, Point2D.Float p2, Point2D.Float p3){
		this.p1 = new Point2D.Double(p1.getX(), p1.getY());
		this.p2 = new Point2D.Double(p2.getX(), p2.getY());
		this.p3 = new Point2D.Double(p3.getX(), p3.getY());
	}
	
	public String getDir() {
		Arc2D.Double arc = new Arc2D.Double();
		arc.setArcByTangent(p1,p2,p3,1);
		
		Double angle = arc.getAngleExtent();


		while(angle < 0) {
			angle += 360;
		}

		while(angle > 360) {
			angle -= 360;
		}
		
		if (angle >= 45 && angle <= 180) {

			return "right";
		} else if (angle > 180 && angle <= 315) {

			return "left";
		} else {

			return "straight";
		}
	}
}
