package cmsc420.meeshquest.part3;

import java.awt.geom.Point2D;
import java.util.TreeMap;

public class Airport extends City{
	protected String name;
	protected TreeMap<String, Terminal> terminals = new TreeMap<String, Terminal>(new ComparatorName());
	
	public Airport(String name, int localX, int localY, int remoteX, int remoteY, Terminal t) {
		super(name, localX, localY, remoteX, remoteY, 0, "black", 1);	
		terminals.put(t.getName(), t);
	}
	
	public Terminal getTerminal(String name) {
		return terminals.get(name);
	}
}
