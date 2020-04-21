package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.function.BiConsumer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Printing {
	protected Document results;
	protected Element resultElement;
	
	//sets the spatialWidth and Height in the PRQuadtree as well
  	public void setResults(Document results, Element resultElement) {
		this.results = results;
		this.resultElement = resultElement;
 	}

	//template for adding an error to the DOM
	public Element errorProcess(String errorType, String command, String id) {
		Element errorEle = results.createElement("error");
		errorEle.setAttribute("type", errorType);
		resultElement.appendChild(errorEle);
		
		Element commandEle = results.createElement("command");
		commandEle.setAttribute("name", command);
		if (id != null) {
			commandEle.setAttribute("id", id);
		}
		errorEle.appendChild(commandEle);
		
		Element parametersEle = results.createElement("parameters");
		errorEle.appendChild(parametersEle);
		
		return parametersEle;
	}
	
	//template for adding a successful action to the DOM
	public Element successProcess(Element successEle, String command, String id) {
		Element commandEle = results.createElement("command");
		commandEle.setAttribute("name", command);
		if (id != null) {
			commandEle.setAttribute("id", id);
		}
		successEle.appendChild(commandEle);
		
		Element parametersEle = results.createElement("parameters");
		successEle.appendChild(parametersEle);
		
		return parametersEle;
	}
	
 	public void stringParam(Element parametersEle, String eleParam, String value) {
		Element startEle = results.createElement(eleParam);
		startEle.setAttribute("value", value);
		parametersEle.appendChild(startEle);
 	}
 	
	//template to add all the City attributes to the DOM for createCity
 	public void attributesCreateCity(Element ele, City city) {
		
		Element nameEle = results.createElement("name");
		nameEle.setAttribute("value", city.name);
		ele.appendChild(nameEle);
		
		Element xEle = results.createElement("localX");
		xEle.setAttribute("value", Integer.toString(city.getLocalX()));
		ele.appendChild(xEle);

		Element yEle = results.createElement("localY");
		yEle.setAttribute("value", Integer.toString(city.getLocalY()));
		ele.appendChild(yEle);
		
		Element xRemoteEle = results.createElement("remoteX");
		xRemoteEle.setAttribute("value", Integer.toString(city.getRemoteX()));
		ele.appendChild(xRemoteEle);

		Element yRemoteEle = results.createElement("remoteY");
		yRemoteEle.setAttribute("value", Integer.toString(city.getRemoteY()));
		ele.appendChild(yRemoteEle);
		
		Element radiusEle = results.createElement("radius");
		radiusEle.setAttribute("value", Integer.toString(city.radius));
		ele.appendChild(radiusEle);
	
		Element colorEle = results.createElement("color");
		colorEle.setAttribute("value", city.color);
		ele.appendChild(colorEle);
		
    }
 	
 	public void attributesMapAirport(Element ele, Airport a, Terminal t) { 		
		Element nameEle = results.createElement("name");
		nameEle.setAttribute("value", a.getName());
		ele.appendChild(nameEle);
		
		Element xEle = results.createElement("localX");
		xEle.setAttribute("value", Integer.toString(a.getLocalX()));
		ele.appendChild(xEle);

		Element yEle = results.createElement("localY");
		yEle.setAttribute("value", Integer.toString(a.getLocalY()));
		ele.appendChild(yEle);
		
		Element xRemoteEle = results.createElement("remoteX");
		xRemoteEle.setAttribute("value", Integer.toString(a.getRemoteX()));
		ele.appendChild(xRemoteEle);

		Element yRemoteEle = results.createElement("remoteY");
		yRemoteEle.setAttribute("value", Integer.toString(a.getRemoteY()));
		ele.appendChild(yRemoteEle);
		
		Element tNameEle = results.createElement("terminalName");
		tNameEle.setAttribute("value", t.getName());
		ele.appendChild(tNameEle);
		
		Element xTermEle = results.createElement("terminalX");
		xTermEle.setAttribute("value", Integer.toString(t.getLocalX()));
		ele.appendChild(xTermEle);

		Element yTermEle = results.createElement("terminalY");
		yTermEle.setAttribute("value", Integer.toString(t.getLocalY()));
		ele.appendChild(yTermEle);
		
		Element tCityEle = results.createElement("terminalCity");
		tCityEle.setAttribute("value", t.getCity());
		ele.appendChild(tCityEle);
 	}

	public Element cityProcess(Element cityEle, City city) {
		
		cityEle.setAttribute("color", city.color);
		cityEle.setAttribute("name", city.name);
		cityEle.setAttribute("radius", Integer.toString(city.radius));
		cityEle.setAttribute("localX", Integer.toString(city.getLocalX()));
		cityEle.setAttribute("localY", Integer.toString(city.getLocalY()));
		cityEle.setAttribute("remoteX", Integer.toString(city.getRemoteX()));
		cityEle.setAttribute("remoteY", Integer.toString(city.getRemoteY()));
		
		return cityEle;
	}
	
	public Element cityProcess2(Element cityEle, City city) {
		
		cityEle.setAttribute("name", city.name);
		cityEle.setAttribute("localX", Integer.toString(city.getLocalX()));
		cityEle.setAttribute("localY", Integer.toString(city.getLocalY()));
		cityEle.setAttribute("remoteX", Integer.toString(city.getRemoteX()));
		cityEle.setAttribute("remoteY", Integer.toString(city.getRemoteY()));
		cityEle.setAttribute("color", city.color);
		cityEle.setAttribute("radius", Integer.toString(city.radius));
		
		return cityEle;
	}

	public void rangeCityProcess(int x, int y, int radius, Element parametersEle) {
		Element xEle = results.createElement("remoteX");
		xEle.setAttribute("value", Integer.toString(x));
		parametersEle.appendChild(xEle);
		
		Element yEle = results.createElement("remoteY");
		yEle.setAttribute("value", Integer.toString(y));
		parametersEle.appendChild(yEle);
		
		Element radiusEle = results.createElement("radius");
		radiusEle.setAttribute("value", Integer.toString(radius));
		parametersEle.appendChild(radiusEle);

	}

	public void roadProcess(Element roadEle, Road road) {
		roadEle.setAttribute("end", road.getEndName());
		roadEle.setAttribute("start", road.getStartName());
	}	
	
	public void shortestPathProcess(String errorType, String id, String start, String end, String nameMap2, String nameHTML) {
		Element parametersEle = errorProcess(errorType, "shortestPath", id);
		stringParam(parametersEle, "start", start);
		stringParam(parametersEle, "end", end);
		
		if (nameMap2 != null) {
			Element nameMapEle = results.createElement("saveMap");
			nameMapEle.setAttribute("value", nameMap2);
			parametersEle.appendChild(nameMapEle);
		}
		
		if (nameHTML != null) {
			Element nameHTMLEle = results.createElement("saveHTML");
			nameHTMLEle.setAttribute("value", nameHTML);
			parametersEle.appendChild(nameHTMLEle);
		}
	}
}
