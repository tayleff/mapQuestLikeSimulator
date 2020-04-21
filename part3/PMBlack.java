package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
import java.awt.geom.Point2D.Float;
import java.util.PriorityQueue;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

public class PMBlack extends PMNode {

	//city object located in the black node
	protected City city = null;
	protected int numCities;
	protected CanvasPlus canvas;
	protected Rectangle2D.Double region;
	protected TreeSet<MapStruct> structs = new TreeSet<MapStruct>(new ComparatorMapStruct());
	
	//constructor for the black node
	protected PMBlack() {
		super(BLACK);
		numCities = 0;
	}
	
	//inserts the city into a new black node or creates a grey node that puts the old city and new city within its partitions
	//returns either itself if it was empty or a grey node if the black node already contained a city
	@Override
	public PMNode insert(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator) throws RoadViolatesPMThrowable{
			region = new Rectangle2D.Double(origin.x, origin.y, width, height);
			structs.add(struct);

			if (struct.getType() == MapStruct.CITY) {
				City newCity = (City) struct;
				
				setCity(newCity);
			} else if (struct.getType() == MapStruct.ROAD) {
				Road newRoad = (Road) struct;

				setCity(newRoad.getStartCity());
				setCity(newRoad.getEndCity());
				newRoad.getStartCity().setNotIsolated();
				newRoad.getEndCity().setNotIsolated();
			} 

		if (validator.valid(this)) {
			return this;
		} else {	
			return partition(origin, width, height, validator);
		}
	}
	
	public PMNode partition(Point2D.Double origin, int width, int height, PMValidator validator) throws RoadViolatesPMThrowable {
		PMGrey greyNode = new PMGrey(origin, width, height);

		for (MapStruct s : structs) {
			greyNode.insert(s, origin, width, height, validator);
		}

		return greyNode;
	}
	
	@Override
	public PMNode delete(MapStruct struct, Point2D.Double origin, int width, int height, PMValidator validator) {
		structs.remove(struct);

		if (struct.getType() == MapStruct.CITY) {
			City c = (City) struct;
			if (city.equals(c) ) {
				city = null;
				numCities--;
			}
		} else {
			Road r = (Road) struct;
			if (r.getEndCity().equals(city)) {
				city = null;
				numCities--;
			}
			
			if (r.getStartCity().equals(city)) {
				city = null;
				numCities--;
			}
		}

		if (structs.size() == 0) {
			return PMWhite.getInstance();
		}

		return this;
	}

	@Override
	public Element printTree(Document results, Element ele) {
		Element blackEle = results.createElement("black");
		blackEle.setAttribute("cardinality", Integer.toString(structs.size()));

		City c = hasCity();
		if (c != null) {
			Element cityEle;
			if (c.isIsolated()) {
				cityEle = results.createElement("airport");	
				cityEle.setAttribute("localX", Integer.toString(c.getLocalX()));
				cityEle.setAttribute("localY", Integer.toString(c.getLocalY()));
				cityEle.setAttribute("name", c.getName());
				cityEle.setAttribute("remoteX", Integer.toString(c.getRemoteX()));
				cityEle.setAttribute("remoteY", Integer.toString(c.getRemoteY()));
			} else if (c.getCityType() == City.TERMINAL) {
				cityEle = results.createElement("terminal");
				Terminal t = (Terminal) c;
				cityEle.setAttribute("airportName", t.getAirport());
				cityEle.setAttribute("cityName", t.getCity());
				cityEle.setAttribute("localX", Integer.toString(c.getLocalX()));
				cityEle.setAttribute("localY", Integer.toString(c.getLocalY()));
				cityEle.setAttribute("name", c.getName());
				cityEle.setAttribute("remoteX", Integer.toString(c.getRemoteX()));
				cityEle.setAttribute("remoteY", Integer.toString(c.getRemoteY()));

			} else {
				cityEle = results.createElement("city");
				cityEle.setAttribute("color", c.getColor());
				cityEle.setAttribute("localX", Integer.toString(c.getLocalX()));
				cityEle.setAttribute("localY", Integer.toString(c.getLocalY()));
				cityEle.setAttribute("name", c.getName());
				cityEle.setAttribute("radius", Integer.toString(c.getRadius()));
				cityEle.setAttribute("remoteX", Integer.toString(c.getRemoteX()));
				cityEle.setAttribute("remoteY", Integer.toString(c.getRemoteY()));
			}

			
			blackEle.appendChild(cityEle);
		}
		
		Element roadEle;
		for (MapStruct s : structs) {
			if (s.getType() == MapStruct.ROAD) {
				Road r = (Road) s;
				roadEle = results.createElement("road");
				roadEle.setAttribute("end", r.getEndName());
				roadEle.setAttribute("start", r.getStartName());
				blackEle.appendChild(roadEle);
			}
		}
		
		return blackEle;
	}
	
	//returns the city object in the black node
	public City getCity() {
		return city;
	}
	
	public Rectangle2D.Double getRegion() {
		return region;
	}
	
	public City hasCity() {
		for (MapStruct s : structs) {
			if (s.getType() == MapStruct.CITY) {
				return (City) s;
			}
		}
		return null;
	}
	
	public void setCity(City city) {
		if (Inclusive2DIntersectionVerifier.intersects(city.toLocalPoint2D(), getRegion())) {
			if (this.city == null) {
				this.city = city;
				numCities++;
				structs.add(city);

			} else if(!this.city.equals(city)) {
				numCities++;
			}
		}
	}
	
	public int numCities() {
		return numCities;
	}
	
	public int numStructs() {
		return structs.size();
	}
	
	public TreeSet<MapStruct> getStructs() {
		return structs;
	}
	
	public void drawIt(CanvasPlus canvas) {
		
		for (MapStruct struct : structs) {
			if (struct.getType() == MapStruct.CITY) {
				City newCity = (City) struct;
				
				if (newCity.getCityType() == City.CITY) {
					setCity(newCity);
		
					canvas.addPoint(city.getName(), city.getLocalX(), city.getLocalY(), Color.BLACK);
				} else if (newCity.getCityType() == City.TERMINAL) {
					setCity(newCity);
		
					canvas.addPoint(city.getName(), city.getLocalX(), city.getLocalY(), Color.MAGENTA);
				} else {
					setCity(newCity);
					
					canvas.addPoint(city.getName(), city.getLocalX(), city.getLocalY(), Color.GREEN);
				}
	
			} else if (struct.getType() == MapStruct.ROAD) {
				Road newRoad = (Road) struct;
			
				if (newRoad.getEndCity().getCityType()==City.TERMINAL) {
					canvas.addPoint(newRoad.getStartName(), newRoad.getStart().getX(), newRoad.getStart().getY(), Color.BLACK);
					canvas.addPoint(newRoad.getEndName(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.MAGENTA);
					canvas.addLine(newRoad.getStart().getX(), newRoad.getStart().getY(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.BLACK);
				} else if (newRoad.getStartCity().getCityType()==City.TERMINAL){
					canvas.addPoint(newRoad.getStartName(), newRoad.getStart().getX(), newRoad.getStart().getY(), Color.MAGENTA);
					canvas.addPoint(newRoad.getEndName(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.BLACK);
					canvas.addLine(newRoad.getStart().getX(), newRoad.getStart().getY(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.BLACK);
				} else {
					canvas.addPoint(newRoad.getStartName(), newRoad.getStart().getX(), newRoad.getStart().getY(), Color.BLACK);
					canvas.addPoint(newRoad.getEndName(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.BLACK);
					canvas.addLine(newRoad.getStart().getX(), newRoad.getStart().getY(), newRoad.getEnd().getX(), newRoad.getEnd().getY(), Color.BLACK);
				}
			} 
		}
	}
}
