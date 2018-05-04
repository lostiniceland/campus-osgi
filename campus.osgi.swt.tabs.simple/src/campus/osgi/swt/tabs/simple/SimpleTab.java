package campus.osgi.swt.tabs.simple;

import campus.osgi.swt.ui.AppScreen;

public class SimpleTab implements AppScreen {

	@Override
	public String getName() {
		return "Simple Tab";
	}

	@Override
	public int getPosition() {
		return 0;
	}

}
