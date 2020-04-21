package cmsc420.meeshquest.part3;

import java.util.Comparator;

public class ComparatorTerminal implements Comparator<Terminal>{

	@Override
	public int compare(Terminal o1, Terminal o2) {
		
		return -(o1.getName().compareTo(o2.getName()));
	}
}