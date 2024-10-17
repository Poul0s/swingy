package com.swingy.app.Artifacts;

public final class Armor extends Artifact {
	private static Armor _instance = null;
	
	private Armor() {}

	public static Armor getInstance() {
		if (_instance == null) {
			_instance = new Armor();
		}
		return _instance;
	}
}
