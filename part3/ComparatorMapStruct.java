package cmsc420.meeshquest.part3;

import java.util.Comparator;

public class ComparatorMapStruct implements Comparator<MapStruct>{

	@Override
	public int compare(MapStruct o1, MapStruct o2) {
		String s1 = null;
		String s2 = null;
		String s12 = null;
		String s22 = null;


		if (o1.getType() == MapStruct.CITY) {
			City city = (City) o1;
			s1 = city.getName();
		} else if (o1.getType() == MapStruct.ROAD) {
			Road r = (Road) o1;
			s1 = r.getStartName();
			s12 = r.getEndName();
		}
		
		if (o2.getType() == MapStruct.CITY) {
			City city = (City) o2;
			s2 = city.getName();
		} else if (o2.getType() == MapStruct.ROAD) {
			Road r = (Road) o2;
			s2 = r.getStartName();
			s22 = r.getEndName();
		}
		
		if (o1.getType() == MapStruct.CITY && o2.getType() == MapStruct.CITY) {
			return -(s1.compareTo(s2));
		}
		
		if (o1.getType() == MapStruct.CITY && o2.getType() == MapStruct.ROAD) {
			if (s1.compareTo(s2) >= 1) {
				return -1;
			}
			
			if (s1.compareTo(s2) <= -1) {
				return 1;
			}
			
			if (s1.compareTo(s22) >= 1) {
				return -1;
			}
			
			if (s1.compareTo(s22) <= -1) {
				return 1;
			}

		}
		
		if (o1.getType() == MapStruct.ROAD && o2.getType() == MapStruct.CITY) {
			if (s1.compareTo(s2) >= 1) {
				return -1;
			}
			
			if (s1.compareTo(s2) <= -1) {
				return 1;
			}
			
			if (s12.compareTo(s2) >= 1) {
				return -1;
			}
			
			if (s12.compareTo(s2) <= -1) {
				return 1;
			}

		}
		
		if (o1.getType() == MapStruct.ROAD && o2.getType() == MapStruct.ROAD) {
			if (s1.compareTo(s2) >= 1) {
				return -1;
			}
			
			if (s1.compareTo(s2) <= -1) {
				return 1;
			}
			
			if (s12.compareTo(s22) >= 1) {
				return -1;
			}
			
			if (s12.compareTo(s22) <= -1) {
				return 1;
			}

		}

		return 0;
	}
}
