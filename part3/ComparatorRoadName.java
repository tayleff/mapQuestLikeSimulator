package cmsc420.meeshquest.part3;

import java.util.Comparator;

public class ComparatorRoadName implements Comparator<Road>{

	@Override
	public int compare(Road o1, Road o2) {
	
		if (o1.getStartName().compareTo(o2.getStartName()) >= 1) {
			return -1;
		}
		
		if (o1.getStartName().compareTo(o2.getStartName()) <= -1) {
			return 1;
		}
		
		if (o1.getEndName().compareTo(o2.getEndName()) >= 1) {
			return -1;
		}
		
		if (o1.getEndName().compareTo(o2.getEndName()) <= -1) {
			return 1;
		}

		return 0;
	}

}
