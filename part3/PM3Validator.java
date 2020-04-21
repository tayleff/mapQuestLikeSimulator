package cmsc420.meeshquest.part3;

import cmsc420.drawing.CanvasPlus;

public class PM3Validator implements PMValidator{

	@Override
	public boolean valid(PMBlack node) {
		if (node.numCities() > 1) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean valid(final PMGrey node) {
		if (node.halfHeight < 1 || node.halfWidth < 1){
			return false;
		} else {
			return true;
		}
	}
}

