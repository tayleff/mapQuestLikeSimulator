package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Inclusive2DIntersectionVerifier;

public class PMQuadtree {

	// root of the PMQuadTree
	protected PMNode root;

	// origin of the spatial map (0,0)
	protected Point2D.Double origin = new Point2D.Double(0, 0);

	// set of all isolated city names mapped
	protected TreeSet<Road> roads = new TreeSet<Road>(new ComparatorRoadName());
	protected TreeSet<String> airports = new TreeSet<String>(new ComparatorName());
	
	// set of all mapped cities connected by a road
	protected AdjacencyList mapped = new AdjacencyList();
	
	// reference to the White empty node singleton
	protected PMNode emptyNode = PMWhite.getInstance();
	


	protected PMValidator validator;
	protected int spatialWidth;
	protected int spatialHeight;
	protected Rectangle2D.Double region;
	protected Point2D location;

	// creates PRQuadTree object
	public PMQuadtree(PMValidator validator, int width, int height, Point2D location) {
		root = emptyNode;
		this.location = location;  
		this.validator = validator;
		this.spatialHeight = height;
		this.spatialWidth = width;
		region = new Rectangle2D.Double(origin.x, origin.y, spatialWidth, spatialHeight);
	}

	// adds first node to the QuadTree
	public void insert(MapStruct struct) throws RoadViolatesPMThrowable {
		if (struct.getType() == MapStruct.CITY) {
			City city = (City) struct;
			airports.add(city.getName());
			
			root = root.insert(city, origin, spatialWidth, spatialHeight, validator);
		} else {
			Road road = (Road) struct;
			roads.add(road);
			mapped.addMappedRoad(road);
			
			root = root.insert(road, origin, spatialWidth, spatialHeight, validator);
		}
	}

	// adds first node to the QuadTree
	public void delete(MapStruct struct) {
		root = root.delete(struct, origin, spatialWidth, spatialHeight, validator);
	}

	// returns true if QuadTree has the city mapped
	public boolean containsCity(City city) {
		if(mapped.containsCity(city)) {
			return true;
		}
		return false;
	}
	
	public boolean containsRoad(Road road) {
		if(mapped.containsRoad(road)) {
			return true;
		}
		return false;
	}
	
	public boolean containsAirport(Airport a) {
		if (airports.contains(a.getName())) {
			return true;
		}
		return false;
	}

	// returns true if QuadTree is empty
	public boolean isEmpty() {
		if (root == emptyNode) {
			return true;
		}
		return false;
	}
	
	public Point2D getLocation() {
		return location;
	}
	
	public TreeSet<Road> getRoads() {
		return roads;
	}

	public PMValidator getValidator() {
		return validator;
	}

	public Rectangle2D.Double getRegion() {
		return region;
	}
	
	public AdjacencyList getMapped() {
		return mapped;
	}

	public void printTree(Document results, Element quadTreeEle) {
		Element rootEle = root.printTree(results, quadTreeEle);
		quadTreeEle.appendChild(rootEle);
	}
	
	public void drawIt(CanvasPlus canvas) {
		root.drawIt(canvas);
	}
	
	public String getLocationString() {
		final StringBuilder location = new StringBuilder();
		location.append("(");
		location.append(getLocation().getX());
		location.append(",");
		location.append(getLocation().getY());
		location.append(")");
		return location.toString();
	}
	
	
	public boolean airportValid(Airport a) {
		for (Road r : roads) {
			if (Inclusive2DIntersectionVerifier.intersects(a.getLocal(), r.getRoad())) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean terminalValid(Terminal t) {
		for (Road r : roads) {
			if (Inclusive2DIntersectionVerifier.intersects(t.getLocal(), r.getRoad())) {
				return false;
			}
		}
		
		return true;
	}
	
	
	public boolean roadIntersects(Road road) {
		for (Road r : roads) {
			if (Inclusive2DIntersectionVerifier.intersects(road.getRoad(), r.getRoad())) {
				if (!road.getEndCity().equals(r.getEndCity()) && !road.getStartCity().equals(r.getEndCity())
						&& !road.getEndCity().equals(r.getStartCity()) && !road.getStartCity().equals(r.getStartCity())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public TreeMap<City, ArrayList<City>> getCityRoads() {
		return mapped.getMappedRoads();
	}
}
