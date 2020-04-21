package cmsc420.meeshquest.part3;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import cmsc420.drawing.CanvasPlus;
import cmsc420.xml.XmlUtility;

public class MeeshQuest {
	private Document results = null;
	private Element resultElement;
	private static CanvasPlus canvas = new CanvasPlus("MeeshQuest");
	private static Command command = new Command();
	private final Printing print = new Printing();
	
    public static void main(String[] args) {
    		MeeshQuest m = new MeeshQuest();

    		m.start();
    }
    
    private void start() {
        try {
    		Document doc = XmlUtility.validateNoNamespace(System.in);
        	//Document doc = XmlUtility.validateNoNamespace(new File("part3.public.airport.input.xml"));
       //Document doc = XmlUtility.validateNoNamespace(new File("testInput_1.xml"));
     //  Document doc = XmlUtility.validateNoNamespace(new File("testinput2.xml"));


        	results = XmlUtility.getDocumentBuilder().newDocument();
	
        	Element commandNode = doc.getDocumentElement();
        	
        	//set up results element
        resultElement = results.createElement("results");
        	results.appendChild(resultElement);
        	parseCommands(commandNode);
        	
        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
        			
        			parseCommands(commandNode);
        		}
        	}
        	
        } catch (SAXException | IOException | ParserConfigurationException e) {
        	
        	try {
				results = XmlUtility.getDocumentBuilder().newDocument();
        			Element fatalElement = results.createElement("fatalError");
        			results.appendChild(fatalElement);
			} catch (ParserConfigurationException e1) {
				System.exit(-1);
			}

		} finally {
            try {
				XmlUtility.print(results);
			
				//canvas.draw();
				//	canvas.dispose();
				
			} catch (TransformerException e) {
				e.printStackTrace();
				System.exit(-1);
			}
        }
    }
    
    private void parseCommands(Element commandNode) {
	    final String type = commandNode.getNodeName();
	    String id = commandNode.getAttribute("id");

	    switch(type) {
	    
	    case "commands":
	    		command.commandNode(commandNode, canvas, results, resultElement, print);
	        	print.setResults(results, resultElement);
	    		command.setCanvas();
	    		break;
	    		
	    case "createCity":
	    		String name = commandNode.getAttribute("name");
	    		String localX = commandNode.getAttribute("localX");
	    		String localY = commandNode.getAttribute("localY");
	    		String remoteX = commandNode.getAttribute("remoteX");
	    		String remoteY = commandNode.getAttribute("remoteY");
	    		String radius = commandNode.getAttribute("radius");
	    		String color = commandNode.getAttribute("color");		
	    		
	    		City city = new City(name, Integer.parseInt(localX), Integer.parseInt(localY) , Integer.parseInt(remoteX), Integer.parseInt(remoteY), Integer.parseInt(radius), color, 0);
	    		command.createCity(city, id);
	      	break;
	    	
	    case "listCities":
	    		String sortBy = commandNode.getAttribute("sortBy");
	    		command.listCities(sortBy, id);
	    		break;
	    	
	    case "clearAll":
	    		command.clearAll(id);
	    		break;
	    		
	    case "printPMQuadtree":
	    		remoteX = commandNode.getAttribute("remoteX");
	    		remoteY = commandNode.getAttribute("remoteY");
			command.printQuadtree(remoteX, remoteY, id);
			break;
		
	    case "printTreap":
			command.printTreap(id);
			break;
			
	    case "saveMap":
	    		name = commandNode.getAttribute("name");
			command.saveMap(name, id);
			break;
			
	    case "mapRoad":
    			String start = commandNode.getAttribute("start");
    			String end = commandNode.getAttribute("end");
			command.mapRoad(start, end, id);
			break;	
			
	    case "mapAirport":
    			name = commandNode.getAttribute("name");
    			localX = commandNode.getAttribute("localX");
    			localY = commandNode.getAttribute("localY");
    			remoteX = commandNode.getAttribute("remoteX");
    			remoteY = commandNode.getAttribute("remoteY");
    			String tName = commandNode.getAttribute("terminalName");
    			String tX = commandNode.getAttribute("terminalX");	
    			String tY = commandNode.getAttribute("terminalY");	
    			String tCity = commandNode.getAttribute("terminalCity");	

    			Terminal terminal = new Terminal(tName, Integer.parseInt(tX), Integer.parseInt(tY), Integer.parseInt(remoteX), Integer.parseInt(remoteY), tCity, name);
    			Airport airport = new Airport(name, Integer.parseInt(localX), Integer.parseInt(localY), Integer.parseInt(remoteX), Integer.parseInt(remoteY), terminal);
    			command.mapAirport(airport, terminal, id);
	    		break;
	    		
//	    case "unmapRoad":
//			 start = commandNode.getAttribute("start");
//			String end = commandNode.getAttribute("end");
//			command.mapRoad(start, end, id);
//		break;	
			
	    case "globalRangeCities":
			remoteX = commandNode.getAttribute("remoteX");
			remoteY = commandNode.getAttribute("remoteY");
    			radius = commandNode.getAttribute("radius");

    			command.rangeCities(Integer.parseInt(remoteX), Integer.parseInt(remoteY), Integer.parseInt(radius), id);
    			break;
//    			
//	    case "rangeRoads":
//			x = commandNode.getAttribute("x");
//			y = commandNode.getAttribute("y");
//			radius = commandNode.getAttribute("radius");
//			
//			if (commandNode.hasAttribute("saveMap")) {
//				name = commandNode.getAttribute("saveMap");
//			} else {
//				name = null;
//			}
//
//			command.rangeRoads(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(radius), name, id);
//			break;		
//			
	    case "nearestCity":
			localX = commandNode.getAttribute("localX");
			localY = commandNode.getAttribute("localY");
			remoteX = commandNode.getAttribute("remoteX");
			remoteY = commandNode.getAttribute("remoteY");
    			command.nearestCity(Integer.parseInt(localX), Integer.parseInt(localY), Integer.parseInt(remoteX), Integer.parseInt(remoteY), id);
    			break;
//    			
//	    case "nearestIsolatedCity":
//			x = commandNode.getAttribute("x");
//			y = commandNode.getAttribute("y");
//    			command.nearestIsolatedCity(Integer.parseInt(x), Integer.parseInt(y), id);
//    			break;
//    		
//	    case "nearestRoad":
//			x = commandNode.getAttribute("x");
//			y = commandNode.getAttribute("y");
//    			command.nearestRoad(Integer.parseInt(x), Integer.parseInt(y), id);
//    			break;
//    			
//	    case "nearestCityToRoad":
//	    		String start = commandNode.getAttribute("start");
//	    		String	end = commandNode.getAttribute("end");
//    			command.nearestCityToRoad(start, end, id);
//    			break;
//    			
//    			
//	    case "shortestPath":
//    			start = commandNode.getAttribute("start");
//    			end = commandNode.getAttribute("end");
//    			String nameHTML;
//       		
//    			if (commandNode.hasAttribute("saveMap")) {
//    				nameMap = commandNode.getAttribute("saveMap");
//    			} else {
//    				nameMap = null;
//    			}
//    			
//    			if (commandNode.hasAttribute("saveHTML")) {
//    				nameHTML = commandNode.getAttribute("saveHTML");
//    			} else {
//    				nameHTML = null;
//    			}
//    			
//			command.shortestPath(start, end, id, nameMap, nameHTML);
//			break;
	    		
	    	default: 
        		System.exit(-1);
	    		break;
	    }	
    }

	public static CanvasPlus getCanvas() {
		return canvas;
	}
}
