package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;

public class City extends MapStruct implements Comparable<City>{
	public static final int CITY = 0;
	public static final int AIRPORT = 1;
	public static final int TERMINAL = 2;

	protected final int type;
	//city name
	protected String name;
	
	//city radius
	protected int radius;
	
	//city color
	protected String color;
	
	//city coordinates
	protected Point2D.Double localPoint;
	protected Point2D.Double remotePoint;
	
	protected boolean isolated; 
	
	protected City parent; 
	
	protected double minDist = Double.POSITIVE_INFINITY;
	
	//constructor for the City object
	public City(String name, int localX, int localY, int remoteX, int remoteY, int radius, String color, final int type){
		super(CITY);
		this.name = name;
		this.type = type;
		localPoint = new Point2D.Double(localX,localY);
		remotePoint = new Point2D.Double(remoteX,remoteY);
		this.radius = radius;
		this.color = color;
		isolated = true;
	}
	
	public void setNotIsolated() {
		isolated = false;
	}
	
	public boolean isIsolated() {
		return isolated;
	}
	
	//returns the name of the city
	public String getName() {
		return name;
	}
	
	public int getCityType() {
		return type;
	}
	
	public Point2D getLocal() {
		return localPoint;
	}
	
	public Point2D getRemote() {
		return remotePoint;
	}
	
	public Point2D getMetropole() {
		return remotePoint;
	}
	//returns the x coordinate of the city
	public int getLocalX() {
		return (int) localPoint.x;
	}
	
	//returns the y coordinate of the city
	public int getLocalY() {
		return (int) localPoint.y;
	}
	
	//returns the x coordinate of the city
	public int getRemoteX() {
		return (int) remotePoint.x;
	}
	
	//returns the y coordinate of the city
	public int getRemoteY() {
		return (int) remotePoint.y;
	}
	
	//returns the color of the city
	public String getColor() {
		return color;
	}
	
	public Location getLocation() {
		return new Location(remotePoint, localPoint);
	}
	
	//returns the radius of the city
	public int getRadius() {
		return radius;
	}
	
	public void setParent(City parent) {
		this.parent = parent;
	}
	
	public City getParent() {
		return parent;
	}
	
	public void setMinDist(double dist) {
		minDist = dist;
	}
	
	public double getMinDist() {
		return minDist;
	}
	
	/**
	 * Determines if this city is equal to another object. The result is true if
	 * and only if the object is not null and a City object that contains the
	 * same name, X and Y coordinates, radius, and color.
	 * 
	 * @param obj
	 *            the object to compare this city against
	 * @return <code>true</code> if cities are equal, <code>false</code>
	 *         otherwise
	 */
	public boolean equals(final Object obj) {
		if (obj == this)
			return true;
		if (obj != null && (obj.getClass().equals(this.getClass()))) {
			City c = (City) obj;
			return (localPoint.equals(c.localPoint) && remotePoint.equals(c.remotePoint) && (radius == c.radius) && color
					.equals(c.color));
		}
		return false;
	}
	

	/**
	 * Returns a hash code for this city.
	 * 
	 * @return hash code for this city
	 */
	public int hashCode() {
		int hash = 12;
		hash = 37 * hash + name.hashCode();
		hash = 37 * hash + localPoint.hashCode();
		hash = 37 * hash + remotePoint.hashCode();
		hash = 37 * hash + radius;
		hash = 37 * hash + color.hashCode();
		return hash;
	}

	/**
	 * Returns an (x,y) representation of the city. Important: casts the x and y
	 * coordinates to integers.
	 * 
	 * @return string representing the location of the city
	 */
	public String getLocalLocationString() {
		final StringBuilder location = new StringBuilder();
		location.append("(");
		location.append(getLocalX());
		location.append(",");
		location.append(getLocalY());
		location.append(")");
		return location.toString();
	}

	/**
	 * Returns a Point2D instance representing the City's location.
	 * 
	 * @return location of this city
	 */
	public Point2D.Double toLocalPoint2D() {
		return new Point2D.Double(localPoint.x, localPoint.y);
	}
	
	public Point2D.Double toRemotePoint2D() {
		return new Point2D.Double(remotePoint.x, remotePoint.y);
	}
	
	public String toString() {
		return getName();
	}

	@Override
	public int compareTo(City o) {
		return Double.compare(minDist, o.getMinDist());
	}
}
