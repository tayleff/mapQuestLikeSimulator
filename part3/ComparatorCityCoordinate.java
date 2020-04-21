package cmsc420.meeshquest.part3;

import java.util.Comparator;

public class ComparatorCityCoordinate implements Comparator<City>{

	@Override
	public int compare(City o1, City o2) {
		int result = 0;
		
		if (o1.getRemote().getY() > o2.getRemote().getY()) {
			return 1;
		}
		
		if (o1.getRemote().getY() < o2.getRemote().getY()) {
			return -1;
		}
		
		if (o1.getRemote().getX() > o2.getRemote().getX()) {
			return 1;
		}
		
		if (o1.getRemote().getX() < o2.getRemote().getX()) {
			return -1;
		}
		
		if (o1.getLocal().getY() > o2.getLocal().getY()) {
			return 1;
		}
		
		if (o1.getLocal().getY() < o2.getLocal().getY()) {
			return -1;
		}
		
		if (o1.getLocal().getX() > o2.getLocal().getX()) {
			return 1;
		}
		
		if (o1.getLocal().getX() < o2.getLocal().getX()) {
			return -1;
		}
		
		return result;
	}

}
