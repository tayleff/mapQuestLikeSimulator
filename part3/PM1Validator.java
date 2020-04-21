package cmsc420.meeshquest.part3;

import java.util.TreeSet;

import cmsc420.drawing.CanvasPlus;

public class PM1Validator implements PMValidator {
	@Override
	public boolean valid(PMBlack node) {
		// at most 1 city with all the roads connecting to it
		// or
		// only one road
		int numCities = node.numCities();
		
		if (numCities > 1) {
			return false;
		}
			
		if ((node.numStructs() == 1 && numCities == 0))
			return true;
			
			// One city with all roads going to it
			if (numCities == 1){
				TreeSet<MapStruct> temp = new TreeSet<MapStruct>(new ComparatorMapStruct());
				temp.addAll(node.getStructs());
				
				temp.remove((MapStruct)node.getCity());
				Road r;
				for (MapStruct m : temp) {
					r = (Road) m;
					if (!r.getEndCity().equals(node.getCity()) && !r.getStartCity().equals(node.getCity())) {
						return false;	
					}
				}
				return true;
			}
			
			return false;
	}

	@Override
	public boolean valid(final PMGrey node){
		if (node.halfHeight < 1 || node.halfWidth < 1){
			return false;
		} else {
			return true;
		}
	}
}
