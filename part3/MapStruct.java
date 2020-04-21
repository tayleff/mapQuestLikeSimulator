package cmsc420.meeshquest.part3;

public abstract class MapStruct {
	public static final int CITY = 0;
	public static final int ROAD = 1;
	protected final int type;

	protected MapStruct (final int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
