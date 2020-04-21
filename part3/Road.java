package cmsc420.meeshquest.part3;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends MapStruct{

	City end;
	City start;
	String cityA;
	String cityB;
	Line2D.Double road;
	
	public Road(City cityA, City cityB) {
		super(ROAD);
		this.cityA = cityA.getName();
		this.cityB = cityB.getName();
		this.end = getEndCityName(cityA, cityB);
		this.start = getStartCityName(cityA, cityB);
		road = new Line2D.Double(cityA.getLocalX(), cityA.getLocalY(), cityB.getLocalX(), cityB.getLocalY());
	}
	
	public String getCityA() {
		return cityA;
	}
	
	public String getCityB() {
		return cityB;
	}
	
	public City getStartCity() {
		return start;
	}
	
	public City getEndCity() {
		return end;
	}
	
	public City getStartCityName(City cityA, City cityB) {
		if (cityA.getName().compareTo(cityB.getName()) >= 1) {
			return cityB;
		}
		
		return cityA;
	}
	
	public City getEndCityName(City cityA, City cityB) {
		if (cityA.getName().compareTo(cityB.getName()) >= 1) {
			return cityA;
		}
		
		return cityB;
	}
	
	public String getStartName() {
		return start.getName();
	}
	
	public String getEndName() {
		return end.getName();
	}
	
	public Point2D.Double getStart() {
		return start.toLocalPoint2D();
	}
	
	public float getStartX() {
		return (float) start.toLocalPoint2D().getX();
	}
	
	public float getStartY() {
		return (float) start.toLocalPoint2D().getY();
	}
	
	public float getEndX() {
		return (float) end.toLocalPoint2D().getX();
	}
	
	public float getEndY() {
		return (float) end.toLocalPoint2D().getY();
	}
	
	public Point2D.Double getEnd() {
		return end.toLocalPoint2D();
	}
	
	public Line2D.Double getRoad() {
		return road;
	}
	
	public double getDist() {
		return Math.sqrt(Math.pow((getStartX() - getEndX()), 2) + Math.pow((getStartY() - getEndY()), 2));
	}
	
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj != null && (obj.getClass().equals(this.getClass()))) {
			Road r = (Road) obj;
			return (start.equals(r.start) && (end.equals(r.end))  || start.equals(r.end) && (end.equals(r.start)));
		}
		return false;
	}
}
