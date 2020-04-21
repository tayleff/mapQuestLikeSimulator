package cmsc420.meeshquest.part3;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class AdjacencyList {

	protected TreeMap<City, ArrayList<City>> mappedRoads;
	protected TreeMap<Terminal, Airport> termToAirport;
	protected TreeMap<Terminal, City> termToCity;
	
	public AdjacencyList() {
		mappedRoads = new TreeMap<City, ArrayList<City>>(new ComparatorCity());
		termToAirport = new TreeMap<Terminal, Airport>(new ComparatorTerminal());
	}

	public void addTermAirport(Terminal t, Airport a) {
		termToAirport.put(t, a);
	}
	
	public void addTermCity(Terminal t, City c) {
		termToCity.put(t, c);
	}
	
	public void addMappedRoad(Road road) {
		if (road.getEndCity().getCityType() == City.CITY && road.getStartCity().getCityType() == City.CITY) {
		if (mappedRoads.containsKey(road.getStartCity())) {
			// adds road to list if it hasn't been mapped yet
			ArrayList<City> array = mappedRoads.get(road.getStartCity());
			if (!array.contains(road.getEndCity())) {
				array.add(road.getEndCity());
			}
		} else if (!mappedRoads.containsKey(road.getStartCity())) {
			// adds city to list if it has not had any roads added to it yet
			ArrayList<City> array = new ArrayList<City>();
			array.add(road.getEndCity());
			mappedRoads.put(road.getStartCity(), array);
		}

		if (mappedRoads.containsKey(road.getEndCity())) {
			// adds road to list if it hasn't been mapped yet
			ArrayList<City> array = mappedRoads.get(road.getEndCity());
			if (!array.contains(road.getStartCity())) {
				array.add(road.getStartCity());
			}
		} else if (!mappedRoads.containsKey(road.getEndCity())) {
			// adds city to list if it has not had any roads added to it yet
			ArrayList<City> array = new ArrayList<City>();
			array.add(road.getStartCity());
			mappedRoads.put(road.getEndCity(), array);
		}
		}
	}

	public boolean containsCity(City city) {
		if (mappedRoads.containsKey(city)) {
			return true;
		}
		return false;
	}
	
	public boolean containsRoad(Road road) {
		if (mappedRoads.containsKey(road.getStartCity())) {
			if (mappedRoads.get(road.getStartCity()).contains(road.getEndCity())) {
				return true;
			}
		} else if (mappedRoads.containsKey(road.getEndCity())) {
			if (mappedRoads.get(road.getEndCity()).contains(road.getStartCity())) {
				return true;
			}
		}
		return false;
	}
	
	public TreeMap<City, ArrayList<City>> getMappedRoads() {
		return mappedRoads;
	}
}
