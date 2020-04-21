package cmsc420.meeshquest.part3;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cmsc420.drawing.CanvasPlus;
import cmsc420.geom.Circle2D;
import cmsc420.geom.Inclusive2DIntersectionVerifier;
import cmsc420.sortedmap.Treap;

public class Command {
	//always need
	protected Document results;
	protected Element resultElement;
	protected int localSpatialWidth;
	protected int localSpatialHeight;
	protected int remoteSpatialWidth;
	protected int remoteSpatialHeight;
	protected CanvasPlus canvas;
	protected Printing print;
	
	//City dictionaries
	protected TreeMap<String, City> nameMap = new TreeMap<String,City>(new ComparatorName());
	protected TreeSet<City> coordinateMap = new TreeSet<City>(new ComparatorCityCoordinate());
	protected Treap<String, Point2D> treap = new Treap<String, Point2D>(new ComparatorName());

	//metropole dictionary
	protected TreeMap<Point2D, PMQuadtree> metropoles = new TreeMap<Point2D, PMQuadtree>(new ComparatorCoordinates());
	protected PRQuadTree prQuad;

	//All name and coordinate dictionaries
	protected TreeSet<String> names = new TreeSet<String>(new ComparatorName());
	protected TreeSet<Location> coordinates = new TreeSet<Location>(new ComparatorLocation());

	//part 2
	protected String pmOrder;
	protected Rectangle2D.Double pmRegion;
		
	//sets the spatialWidth, spatialHeight, canvas, results Document, and resultsElement
	//sets the spatialWidth and Height in the PRQuadtree as well
 	public void commandNode(Element node, CanvasPlus canvas, Document results, Element resultElement, Printing print) {
		this.localSpatialWidth = Integer.parseInt(node.getAttribute("localSpatialWidth"));
		this.localSpatialHeight = Integer.parseInt(node.getAttribute("localSpatialHeight"));
		this.remoteSpatialWidth = Integer.parseInt(node.getAttribute("remoteSpatialWidth"));
		this.remoteSpatialHeight = Integer.parseInt(node.getAttribute("remoteSpatialHeight"));
		this.canvas = canvas;
		this.results = results;
		this.resultElement = resultElement;
		this.print = print;
		this.pmOrder = node.getAttribute("pmOrder");
		
		this.prQuad = new PRQuadTree();
		prQuad.setBounds(remoteSpatialWidth, remoteSpatialHeight);
		
		pmRegion = new Rectangle2D.Double(0, 0, localSpatialWidth, localSpatialHeight);
 	}
 	
 	public boolean inBounds(Location location) {
 		Point2D localPoint = location.getLocal();
 		Point2D remotePoint = location.getRemote();

 		if (Inclusive2DIntersectionVerifier.intersects(localPoint, pmRegion)  && PRQuadTree.inBounds(remotePoint)) {
 			return true;
 		}
 		return false;
 	}
 	
 	public boolean inBounds(Location l1, Location l2, Road road) {		
 		Point2D rp1 = l1.getRemote();
 		Point2D rp2 = l2.getRemote();
 		if (PRQuadTree.inBounds(rp1) && PRQuadTree.inBounds(rp2) && Inclusive2DIntersectionVerifier.intersects(road.getRoad(), pmRegion) ){
 			return true;
 		}
 		return false;
 	}
 	
 	public boolean sameMetropole(Point2D point1, Point2D point2) {
 		if (Inclusive2DIntersectionVerifier.intersects(point1, point2)) {
 			return true;
 		}
 		return false;
 	}
	
 	//sets up remote spacial map
 	public void setCanvas() {
		canvas.setFrameSize(localSpatialWidth, localSpatialHeight);
		canvas.addRectangle(0, 0, localSpatialWidth, localSpatialHeight, Color.WHITE, true);
		canvas.addRectangle(0, 0, localSpatialWidth, localSpatialHeight, Color.BLACK, false);
	}
	
    //processes the command createCity
    public void createCity(City city, String id) {
    		if (coordinates.contains(city.getLocation())) {
    			//handles the error for trying to add a duplicate city coordinate to the dictionary
	    		
	    		Element paramEle = print.errorProcess("duplicateCityCoordinates", "createCity", id);
	    		print.attributesCreateCity(paramEle, city);
	    		
    		} else if (names.contains(city.name)){
    			//handles the error for trying to add a duplicate city name to the dictionary 
	    		
	    		Element paramEle = print.errorProcess("duplicateCityName", "createCity", id);
	    		print.attributesCreateCity(paramEle, city);
	    		
    		}   else {
    			//processes a successful attempt to add a city to the dictionary 
    			
    			nameMap.put(city.name, city);
    			coordinateMap.add(city);
    			//treap.put(city.name, city.localPoint);
    			coordinates.add(city.getLocation());
    			names.add(city.name);

	    		Element successEle = results.createElement("success");
	    		resultElement.appendChild(successEle);
	    		
    			Element paramEle = print.successProcess(successEle, "createCity", id);
    			
	    		print.attributesCreateCity(paramEle, city);

	    		Element outputEle = results.createElement("output");
	    		successEle.appendChild(outputEle);
    		}  		
    }
    
    //processes the command listCities
	public void listCities(String sortBy, String id) {
	    	if (nameMap.isEmpty() && coordinateMap.isEmpty()) {
	    		//handles the error where no cities have been created yet

	    		Element parametersEle = print.errorProcess("noCitiesToList", "listCities", id);
	    		print.stringParam(parametersEle, "sortBy", sortBy);	    	
	    	} else {
	    		//processes a successful listCities command
			Element successEle = results.createElement("success");
			resultElement.appendChild(successEle);

			Element parametersEle = print.successProcess(successEle, "listCities", id);
    			print.stringParam(parametersEle, "sortBy", sortBy);	    	
			
			Element outputEle = results.createElement("output");
			successEle.appendChild(outputEle);
			
			Element cityListEle = results.createElement("cityList");
			outputEle.appendChild(cityListEle);
			
			Collection<City> cities = null;
			
			if(sortBy.equals("name")) {
				cities = nameMap.values();
			} else {
				cities = coordinateMap;
			}
			
			for (City city : cities) {
				Element cityEle = results.createElement("city");
				print.cityProcess(cityEle, city);
				cityListEle.appendChild(cityEle);
			}
	    }
	}

	//processes the command clearAll
	public void clearAll(String id) {
		nameMap.clear();
		coordinateMap.clear();
		prQuad.clear();
		treap.clear();
		coordinates.clear();
		names.clear();
		metropoles.clear();
		
		Element successEle = results.createElement("success");
		resultElement.appendChild(successEle);
		
		print.successProcess(successEle, "clearAll", id);
		
		Element outputEle = results.createElement("output");
		successEle.appendChild(outputEle);
	}
	
	//processes the command mapCity
	public void mapAirport(Airport airport, Terminal terminal, String id){		
		if (names.contains(airport.getName())) {

			Element parametersEle = print.errorProcess("duplicateAirportName", "mapAirport", id);
    			print.attributesMapAirport(parametersEle, airport, terminal);
    			return;
    			
		} else if (coordinates.contains(airport.getLocation())) {
			//gives error if city is already mapped
			Element parametersEle = print.errorProcess("duplicateAirportCoordinates", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);	
			return;

			
		} else if (!inBounds(airport.getLocation())) {
			//gives error if city if out of the spatial map bounds
			Element parametersEle = print.errorProcess("airportOutOfBounds", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;


		} else if(names.contains(terminal.getName())) {
			
			Element parametersEle = print.errorProcess("duplicateTerminalName", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;

			
		} else if (coordinates.contains(terminal.getLocation())) {
			//gives error if city is already mapped
			Element parametersEle = print.errorProcess("duplicateTerminalCoordinates", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);	
			return;
			
		} else if (!inBounds(terminal.getLocation())) {
			//gives error if city if out of the spatial map bounds
			Element parametersEle = print.errorProcess("terminalOutOfBounds", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;
			
		} else if (!nameMap.containsKey(terminal.getCity())) {
			
			Element parametersEle = print.errorProcess("connectingCityDoesNotExist", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;		
		} 
		
		City city = nameMap.get(terminal.getCity());
		
		if (!sameMetropole(airport.getMetropole(), city.getRemote())) {

			Element parametersEle = print.errorProcess("connectingCityNotInSameMetropole", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;	
			
		} 	
		
		PMQuadtree pmQuad = null;
		
		if (metropoles.containsKey(airport.getMetropole())) {
			pmQuad = metropoles.get(airport.getMetropole());
		} else {
			Element parametersEle = print.errorProcess("connectingCityNotMapped", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;	
		}
		
		if (!pmQuad.airportValid(airport)) {
			
			Element parametersEle = print.errorProcess("airportViolatesPMRules", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;	
			
		} else if (!pmQuad.containsCity(city)) {
			
			Element parametersEle = print.errorProcess("connectingCityNotMapped", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;	
			
		} 
		Road road = new Road(terminal, city);
		if(pmQuad.roadIntersects(road)) {
			
			Element parametersEle = print.errorProcess("roadIntersectsAnotherRoad", "mapAirport", id);
			print.attributesMapAirport(parametersEle, airport, terminal);
			return;	
			
		} else {

			
			try {
				pmQuad.insert(airport);
				
				try {
					pmQuad.insert(road);
					
					Element successEle = results.createElement("success");
					resultElement.appendChild(successEle);

					Element parametersEle = print.successProcess(successEle, "mapAirport", id);
					print.attributesMapAirport(parametersEle, airport, terminal);
					
					Element outputEle = results.createElement("output");
					successEle.appendChild(outputEle);
					
					names.add(airport.getName());
					names.add(terminal.getName());
					coordinates.add(airport.getLocation());
					coordinates.add(terminal.getLocation());
				} catch (RoadViolatesPMThrowable e) {
					//pmQuad.delete(terminal);
					//pmQuad.delete(airport);
					Element parametersEle = print.errorProcess("terminalViolatesPMRules", "mapAirport", id);
					print.attributesMapAirport(parametersEle, airport, terminal);
				}
				
			} catch (RoadViolatesPMThrowable e) {
				//pmQuad.delete(airport);
				Element parametersEle = print.errorProcess("airportViolatesPMRules", "mapAirport", id);
				print.attributesMapAirport(parametersEle, airport, terminal);
			}
		}
	}
	
	//process the command printPMQuadtree
	public void printQuadtree(String remoteX, String remoteY, String id) {
		Point2D point = new Point2D.Double(Integer.parseInt(remoteX), Integer.parseInt(remoteY));
		if (!metropoles.containsKey(point)) {
			//gives error because tree is empty
			Element parametersEle = print.errorProcess("mapIsEmpty", "printPMQuadtree", id);
			print.stringParam(parametersEle, "remoteX", remoteX);
			print.stringParam(parametersEle, "remoteY", remoteY);

		} else {
			PMQuadtree pmQuad = metropoles.get(point);
			Element successEle = results.createElement("success");
			resultElement.appendChild(successEle);

			Element parametersEle = print.successProcess(successEle, "printPMQuadtree", id);;
			print.stringParam(parametersEle, "remoteX", remoteX);
			print.stringParam(parametersEle, "remoteY", remoteY);


			Element outputEle = results.createElement("output");
			successEle.appendChild(outputEle);
			
			Element quadTreeEle = results.createElement("quadtree");
			quadTreeEle.setAttribute("order", pmOrder);
			outputEle.appendChild(quadTreeEle);
			
			pmQuad.printTree(results, quadTreeEle);		
			
			pmQuad.drawIt(canvas);
			canvas.draw();
		}
	}
	
	public void mapRoad(String start, String end, String id){

		if (!nameMap.containsKey(start)) {
	    		Element parametersEle = print.errorProcess("startPointDoesNotExist", "mapRoad", id);
	    		print.stringParam(parametersEle, "start", start);
	    		print.stringParam(parametersEle, "end", end);
	    		return;
		} else if(!nameMap.containsKey(end)) {
	    		Element parametersEle = print.errorProcess("endPointDoesNotExist", "mapRoad", id);
	    		print.stringParam(parametersEle, "start", start);
	    		print.stringParam(parametersEle, "end", end);
	    		return;
		} else if(start.equals(end)) {
	    		Element parametersEle = print.errorProcess("startEqualsEnd", "mapRoad", id);
	    		print.stringParam(parametersEle, "start", start);
	    		print.stringParam(parametersEle, "end", end);
	    		return;
		}
		City c1 = nameMap.get(start);
		City c2 = nameMap.get(end);
		Road road = new Road(c1, c2);

		if (!sameMetropole(c1.getRemote(), c2.getRemote() )) {
	    		Element parametersEle = print.errorProcess("roadNotInOneMetropole", "mapRoad", id);
	    		print.stringParam(parametersEle, "start", start);
	    		print.stringParam(parametersEle, "end", end);
	    		return;
		} else if (!inBounds(c1.getLocation(), c2.getLocation(), road)) {
			//gives error if road is out of the spatial map bounds
			Element parametersEle = print.errorProcess("roadOutOfBounds", "mapRoad", id);
			print.stringParam(parametersEle, "start", start);
			print.stringParam(parametersEle, "end", end);
			return;
		}
		
		
		PMQuadtree pmQuad;
		
		if (metropoles.containsKey(c1.getRemote())) {
			pmQuad = metropoles.get(c1.getRemote());
			
			if (pmQuad.containsRoad(road)) {
				//gives error if road is already mapped
				Element parametersEle = print.errorProcess("roadAlreadyMapped", "mapRoad", id);
				print.stringParam(parametersEle, "start", start);
				print.stringParam(parametersEle, "end", end);
				return;
				
			} else if ( pmQuad.roadIntersects(road)){
				//gives error if road intersects another road
				Element parametersEle = print.errorProcess("roadIntersectsAnotherRoad", "mapRoad", id);
				print.stringParam(parametersEle, "start", start);
				print.stringParam(parametersEle, "end", end);
				return;
			} 
			
			
		} else {
			if(pmOrder.equals("1")){
				pmQuad = new PMQuadtree(new PM1Validator(), localSpatialWidth, localSpatialHeight, c1.getRemote());
			} else {
				pmQuad = new PMQuadtree(new PM3Validator(), localSpatialWidth, localSpatialHeight, c1.getRemote());
			}
		}
		
		
//			
//			if (!pmQuad.inBounds(nameMap.get(start))) {
//				pmQuad.endNotOnSpatialInsert(nameMap.get(start));
//			}
//		
//			if (!pmQuad.inBounds(nameMap.get(end))) {
//				pmQuad.endNotOnSpatialInsert(road.getEndCity());
//			}
			try {
				pmQuad.insert(road);
				
				Element successEle = results.createElement("success");
				resultElement.appendChild(successEle);
	
				Element parametersEle = print.successProcess(successEle, "mapRoad", id);
				print.stringParam(parametersEle, "start", start);
				print.stringParam(parametersEle, "end", end);   	
				
				Element outputEle = results.createElement("output");
				successEle.appendChild(outputEle);
				
				Element roadEle = results.createElement("roadCreated");
				roadEle.setAttribute("start", start);
				roadEle.setAttribute("end", end);
				outputEle.appendChild(roadEle);
				metropoles.put(c1.getRemote(), pmQuad);
			
			} catch (RoadViolatesPMThrowable e1){
				Element parametersEle = print.errorProcess("roadViolatesPMRules", "mapRoad", id);
				print.stringParam(parametersEle, "start", start);
				print.stringParam(parametersEle, "end", end);
				return;
			}
		
	}
	

	public void saveMap(String name, String id) {
		try {
			canvas.save(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Element successEle = results.createElement("success");
		resultElement.appendChild(successEle);

		Element parametersEle = print.successProcess(successEle, "saveMap", id);
		print.stringParam(parametersEle, "name",name);	    	

		Element outputEle = results.createElement("output");
		successEle.appendChild(outputEle);	
	}

	public void rangeCities(int x, int y, int radius, String id) {
		Collection<City> cities;
		Collection<Point2D> mets = metropoles.keySet();
		Element ele = null;
		Point2D.Float origin = new Point2D.Float(x, y);
		Circle2D.Float circle = new Circle2D.Float(origin, radius);
		Element cityListEle = null;
		PMQuadtree pmQuad;
		for (Point2D m : mets) {
			if (Inclusive2DIntersectionVerifier.intersects(m, circle)) {	
				pmQuad = metropoles.get(m);
				cities = pmQuad.getCityRoads().keySet();
				for (City city : cities) {
						

					if (ele == null) {
						ele = results.createElement("success");
						resultElement.appendChild(ele);
		
						Element parametersEle = print.successProcess(ele, "rangeCities", id);				
						print.rangeCityProcess(x, y, radius, parametersEle);
								
						Element outputEle = results.createElement("output");
						ele.appendChild(outputEle);
								
						cityListEle = results.createElement("cityList");
						outputEle.appendChild(cityListEle);	
							
					}
							
					Element cityEle = results.createElement("city");
					print.cityProcess2(cityEle, city);
					cityListEle.appendChild(cityEle);
						
				}
			}
		}
		
		
		if (ele == null) {
    			Element parametersEle = print.errorProcess("noCitiesExistInRange", "rangeCities", id);
			print.rangeCityProcess(x, y, radius, parametersEle);
		}
	}

	public void nearestCity(int localX, int localY, int remoteX, int remoteY, String id) {
		Point2D.Float point = new Point2D.Float(localX, localY);
		Point2D.Float point2 = new Point2D.Float(remoteX, remoteY);
		PMQuadtree pmQuad = null;
		if (metropoles.containsKey(point2)) {
			pmQuad = metropoles.get(point2);
		} else {
			Element ele = print.errorProcess("cityNotFound", "nearestCity", id);
			Element xEle = results.createElement("localX");
			xEle.setAttribute("value", Integer.toString(localX));
			ele.appendChild(xEle);

			Element yEle = results.createElement("localY");
			yEle.setAttribute("value", Integer.toString(localY));
			ele.appendChild(yEle);
			
			Element xRemoteEle = results.createElement("remoteX");
			xRemoteEle.setAttribute("value", Integer.toString(remoteX));
			ele.appendChild(xRemoteEle);

			Element yRemoteEle = results.createElement("remoteY");
			yRemoteEle.setAttribute("value", Integer.toString(remoteY));
			ele.appendChild(yRemoteEle);
			return;
		}
		TreeMap<City, ArrayList<City>> notIsolatedCities = pmQuad.getCityRoads();
		PriorityQueue<City> pq = new PriorityQueue<City>(new ComparatorCityMinDist(point));
	
		Collection<City> cities = notIsolatedCities.keySet();
		for (City city : cities) {
			pq.add(city);
		}
			
			
		if (!pq.isEmpty()) {
			Element successEle = results.createElement("success");
			resultElement.appendChild(successEle);

			Element ele = print.successProcess(successEle, "nearestCity", id);
			Element xEle = results.createElement("localX");
			xEle.setAttribute("value", Integer.toString(localX));
			ele.appendChild(xEle);

			Element yEle = results.createElement("localY");
			yEle.setAttribute("value", Integer.toString(localY));
			ele.appendChild(yEle);
			
			Element xRemoteEle = results.createElement("remoteX");
			xRemoteEle.setAttribute("value", Integer.toString(remoteX));
			ele.appendChild(xRemoteEle);

			Element yRemoteEle = results.createElement("remoteY");
			yRemoteEle.setAttribute("value", Integer.toString(remoteY));
			ele.appendChild(yRemoteEle);

			Element outputEle = results.createElement("output");
			successEle.appendChild(outputEle);
				
			Element cityEle = results.createElement("city");
			print.cityProcess2(cityEle, pq.peek());
			outputEle.appendChild(cityEle);
		} else {
			Element ele = print.errorProcess("cityNotFound", "nearestCity", id);
			Element xEle = results.createElement("localX");
			xEle.setAttribute("value", Integer.toString(localX));
			ele.appendChild(xEle);

			Element yEle = results.createElement("localY");
			yEle.setAttribute("value", Integer.toString(localY));
			ele.appendChild(yEle);
			
			Element xRemoteEle = results.createElement("remoteX");
			xRemoteEle.setAttribute("value", Integer.toString(remoteX));
			ele.appendChild(xRemoteEle);

			Element yRemoteEle = results.createElement("remoteY");
			yRemoteEle.setAttribute("value", Integer.toString(remoteY));
			ele.appendChild(yRemoteEle);
		}
	}
	

//	public void rangeRoads(int x, int y, int radius, String name, String id) {
//		Collection<Road> roads = pmQuad.getRoads();
//		Element ele = null;
//		Point2D.Float origin = new Point2D.Float(x, y);
//		Line2D.Float line;
//		Element roadListEle = null;
//
//		for (Road road : roads) {
//			line = road.getRoad();
//
//				if (line.ptSegDist(origin) <= radius) {	
//					if (ele == null) {
//						ele = results.createElement("success");
//						resultElement.appendChild(ele);
//
//						Element parametersEle = print.successProcess(ele, "rangeRoads", id);				
//						print.rangeCityProcess(x, y, radius, name, parametersEle);
//						
//						Element outputEle = results.createElement("output");
//						ele.appendChild(outputEle);
//						
//						roadListEle = results.createElement("roadList");
//						outputEle.appendChild(roadListEle);	
//					
//						if (name != null) {
//							try {
//								canvas.save(name);
//							} catch (IOException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//					
//					Element roadEle = results.createElement("road");
//					print.roadProcess(roadEle, road);
//					roadListEle.appendChild(roadEle);
//				}
//			}
//		
//		
//		if (ele == null) {
//    			Element parametersEle = print.errorProcess("noRoadsExistInRange", "rangeRoads", id);
//			print.rangeCityProcess(x, y, radius, name, parametersEle);
//		}
//	}
//	
//	public void nearestIsolatedCity(int x, int y, String id) {
//		Point2D.Float point = new Point2D.Float(x, y);
//		TreeSet<String> isolatedCities = pmQuad.getIsolatedCities();
//		PriorityQueue<City> pq = new PriorityQueue<City>(new ComparatorCityMinDist(point));
//			
//		for (String city : isolatedCities) {
//			pq.add(nameMap.get(city));
//		}
//			
//			
//		if (!pq.isEmpty()) {
//			Element successEle = results.createElement("success");
//			resultElement.appendChild(successEle);
//
//			Element parametersEle = print.successProcess(successEle, "nearestIsolatedCity", id);
//			print.stringParam(parametersEle, "x", Integer.toString(x));
//			print.stringParam(parametersEle, "y", Integer.toString(y));
//
//			Element outputEle = results.createElement("output");
//			successEle.appendChild(outputEle);
//				
//			Element cityEle = results.createElement("isolatedCity");
//			print.cityProcess2(cityEle, pq.peek());
//			outputEle.appendChild(cityEle);
//		} else {
//			Element parametersEle = print.errorProcess("cityNotFound", "nearestIsolatedCity", id);
//    			print.stringParam(parametersEle, "x", Integer.toString(x));
//    			print.stringParam(parametersEle, "y", Integer.toString(y));
//		}
//	}
//
//	public void nearestRoad(int x, int y, String id) {
//		Point2D.Float point = new Point2D.Float(x, y);
//		TreeSet<Road> roads = pmQuad.getRoads();
//		PriorityQueue<Road> pq = new PriorityQueue<Road>(new ComparatorRoadMinDist(point));
//			
//		for (Road road : roads) {
//			pq.add(road);
//		}
//			
//			
//		if (!pq.isEmpty()) {
//			Element successEle = results.createElement("success");
//			resultElement.appendChild(successEle);
//
//			Element parametersEle = print.successProcess(successEle, "nearestRoad", id);
//			print.stringParam(parametersEle, "x", Integer.toString(x));
//			print.stringParam(parametersEle, "y", Integer.toString(y));
//
//			Element outputEle = results.createElement("output");
//			successEle.appendChild(outputEle);
//				
//			Element roadEle = results.createElement("road");
//			print.roadProcess(roadEle, pq.peek());
//			outputEle.appendChild(roadEle);
//		} else {
//			Element parametersEle = print.errorProcess("roadNotFound", "nearestRoad", id);
//    			print.stringParam(parametersEle, "x", Integer.toString(x));
//    			print.stringParam(parametersEle, "y", Integer.toString(y));
//		}
//	}
//
//	public void nearestCityToRoad(String start, String end, String id) {
//		City startCity = nameMap.get(start);
//		City endCity = nameMap.get(end);
//		Road line = new Road(startCity, endCity);
//
//		if (!pmQuad.contains(line)) {
//			Element parametersEle = print.errorProcess("roadIsNotMapped", "nearestCityToRoad", id);
//			print.stringParam(parametersEle, "start", start);
//			print.stringParam(parametersEle, "end", end);
//
//		} else {	
//			TreeSet<String> isolatedCities = pmQuad.getIsolatedCities();
//			TreeMap<City, ArrayList<City>> notIsolatedCities = pmQuad.getNotIsolatedCities();
//			PriorityQueue<City> pq = new PriorityQueue<City>(new ComparatorRoadToPointMinDist(line));
//			
//			for (String name : isolatedCities) {
//				pq.add(nameMap.get(name));
//			}
//			
//			Collection<City> cities = notIsolatedCities.keySet();
//			for (City city : cities) {
//				if (!city.equals(startCity) && !city.equals(endCity)) {
//					pq.add(city);
//				}
//			}
//			
//			if (!pq.isEmpty()) {
//				Element successEle = results.createElement("success");
//				resultElement.appendChild(successEle);
//	
//				Element parametersEle = print.successProcess(successEle, "nearestCityToRoad", id);
//				print.stringParam(parametersEle, "start", start);
//				print.stringParam(parametersEle, "end", end);
//	
//				Element outputEle = results.createElement("output");
//				successEle.appendChild(outputEle);
//					
//				Element cityEle = results.createElement("city");
//				print.cityProcess2(cityEle, pq.peek());
//				outputEle.appendChild(cityEle);
//			} else {
//				Element parametersEle = print.errorProcess("noOtherCitiesMapped", "nearestCityToRoad", id);
//				print.stringParam(parametersEle, "start", start);
//				print.stringParam(parametersEle, "end", end);
//			}
//		} 
//	}
//
//	public void shortestPath(String start, String end, String id, String nameMap2, String nameHTML) {
//		City startCity;
//		City endCity;
//
//		if (nameMap.containsKey(start)) {
//			startCity = nameMap.get(start);
//			if (!pmQuad.mappedRoad(startCity) ) {
//				print.shortestPathProcess("nonExistentStart", id, start, end, nameMap2, nameHTML);
//				return;
//			}
//			
//			if (pmQuad.endNotOnSpatial(startCity)) {
//				print.shortestPathProcess("nonExistentStart", id, start, end, nameMap2, nameHTML);
//				return;
//			}
//			
//		} else if (!nameMap.containsKey(start)){
//			print.shortestPathProcess("nonExistentStart", id, start, end, nameMap2, nameHTML);
//			return;
//		} 
//		
//		if (nameMap.containsKey(end)) {
//			endCity = nameMap.get(end);
//			if (!pmQuad.mappedRoad(endCity)) {
//				print.shortestPathProcess("nonExistentEnd", id, start, end, nameMap2, nameHTML);
//				return;
//			}
//			
//			if (pmQuad.endNotOnSpatial(endCity)) {
//				print.shortestPathProcess("nonExistentEnd", id, start, end, nameMap2, nameHTML);
//				return;
//			}
//		} else if (!nameMap.containsKey(end)) {
//
//			print.shortestPathProcess("nonExistentEnd", id, start, end, nameMap2, nameHTML);
//			return;
//		} 
//			
//		DecimalFormat format = new DecimalFormat("#.000");
//		startCity = nameMap.get(start);
//		endCity = nameMap.get(end);
//			
//		if (startCity.equals(endCity)) {
//			Element successEle = results.createElement("success");
//			resultElement.appendChild(successEle);
//
//			Element parametersEle = print.successProcess(successEle, "shortestPath", id);
//			print.stringParam(parametersEle, "start", start);
//			print.stringParam(parametersEle, "end", end);
//				
//			if (nameMap2 != null) {
//				print.stringParam(parametersEle, "saveMap", nameMap2);
//			}
//				
//			if (nameHTML != null) {
//				print.stringParam(parametersEle, "saveHTML", nameHTML);
//			}
//				
//			Element outputEle = results.createElement("output");
//			successEle.appendChild(outputEle);
//
//			Element pathEle = results.createElement("path");
//			outputEle.appendChild(pathEle);
//				
//			pathEle.setAttribute("length", "0.000");
//			pathEle.setAttribute("hops", "0");
//				
//			return;
//		}
//			
//		pmQuad.computePaths(nameMap.get(start));
//		List<City> path = PMQuadtree.getShortestPathTo(nameMap.get(end));
//		Double dist = 0.0;
//		int hops = 0;
//		Angle angle;
//		ListIterator<City> iterator = path.listIterator();
//			
//		for (City c : path) {
//			if (pmQuad.endNotOnSpatial(c)) {
//				pmQuad.cleanPaths();
//				print.shortestPathProcess("noPathExists", id, start, end, nameMap2, nameHTML);
//				return;
//			}
//		}
//			
//		if (path.contains(nameMap.get(start)) && path.contains(nameMap.get(end))) {
//
//			Element successEle = results.createElement("success");
//			resultElement.appendChild(successEle);
//
//			Element parametersEle = print.successProcess(successEle, "shortestPath", id);
//			print.stringParam(parametersEle, "start", start);
//			print.stringParam(parametersEle, "end", end);
//				
//			if (nameMap2 != null) {
//				print.stringParam(parametersEle, "saveMap", nameMap2);
//			}
//				
//			
//			if (nameHTML != null) {
//				print.stringParam(parametersEle, "saveHTML", nameHTML);
//			}
//			
//			Element outputEle = results.createElement("output");
//			successEle.appendChild(outputEle);
//
//			Element pathEle = results.createElement("path");
//			outputEle.appendChild(pathEle);
//				
//
//			Road road;
//			Road newRoad = null;
//
//			if (path.size() == 2) {
//				road = new Road(startCity, endCity);
//				pmQuad.cleanPaths();
//
//				Element roadEle = results.createElement("road");
//				roadEle.setAttribute("start", start);
//				roadEle.setAttribute("end", end);
//				pathEle.appendChild(roadEle);
//				pathEle.setAttribute("length", format.format(road.getDist()));
//				pathEle.setAttribute("hops", "1");
//				return;
//			}
//				
//			City curr = null;
//			City prev;
//			City next = null;
//			String direction;
//				
//			while(iterator.hasNext()) {
//				prev = iterator.next();
//					
//				if (iterator.hasNext()) {
//					curr = iterator.next();
//					if (iterator.hasNext()) {
//						next = path.get(iterator.nextIndex());
//					}
//						
//					if (iterator.hasPrevious()) {
//						iterator.previous();
//					}
//						
//					if (!curr.equals(next)) {
//						hops++;
//						road = new Road(prev, curr);
//						newRoad = new Road(curr, next);
//	
//						dist = dist + road.getDist();
//						angle = new Angle(prev.toPoint2D(), curr.toPoint2D(), next.toPoint2D());
//							
//						direction = angle.getDir();
//	
//						Element roadEle = results.createElement("road");
//						roadEle.setAttribute("start", road.getCityA());
//						roadEle.setAttribute("end", road.getCityB());
//						pathEle.appendChild(roadEle);
//
//						Element dirEle = results.createElement(direction);
//						pathEle.appendChild(dirEle);
//					}
//				}
//			}
//			
//			for (City c : path) {
//				c.setMinDist(Double.POSITIVE_INFINITY);
//				c.setParent(null);
//			}
//			
//			pmQuad.cleanPaths();
//
//			format.setMaximumFractionDigits(3);
//			if (newRoad != null) {
//				Element roadEle = results.createElement("road");
//				roadEle.setAttribute("start", newRoad.getCityA());
//				roadEle.setAttribute("end", newRoad.getCityB());
//				pathEle.appendChild(roadEle);
//				hops++;
//				dist = dist + newRoad.getDist();
//			}
//
//			pathEle.setAttribute("length", format.format(dist));
//			pathEle.setAttribute("hops", Integer.toString(hops));
//
//				
//		} else {
//			pmQuad.cleanPaths();
//			print.shortestPathProcess("noPathExists", id, start, end, nameMap2, nameHTML);
//		}
//	}
	
    public void printTreap(String id) {

        if (treap.isEmpty()) {
        		print.errorProcess("emptyTree", "printTreap", id);
        } else {
        	
			Element successEle = results.createElement("success");
			resultElement.appendChild(successEle);

			Element parametersEle = print.successProcess(successEle, "printTreap", id);
			successEle.appendChild(parametersEle);
			
			Element outputEle = results.createElement("output");
			successEle.appendChild(outputEle);
			
			Element treapEle = results.createElement("treap");
			treapEle.setAttribute("cardinality", Integer.toString(treap.size()));
			treap.createXml(outputEle);
        }
    }
}
