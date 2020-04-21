package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;

public class Terminal extends City {
	
	protected String city;
	protected String airport;
	
	public Terminal(String name, int localX, int localY, int remoteX, int remoteY, String cityName, String airportName) {
		super(name, localX, localY, remoteX, remoteY, 0, "black", 2);
		city = cityName;
		airport = airportName;
	}
	
	public String getCity() {
		return city;
	}
	
	public String getAirport() {
		return airport;
	}
}
