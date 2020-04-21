package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;

public class Location {

	Point2D remote;
	Point2D local;
	
	public Location(Point2D remote, Point2D local) {
		this.remote = remote;
		this.local = local;
	}
	
	public Point2D getRemote() {
		return remote;
	}
	
	public Point2D getLocal() {
		return local;
	}
	

}
