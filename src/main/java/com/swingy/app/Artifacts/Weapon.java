package com.swingy.app.Artifacts;

public final class Weapon extends Artifact {
	private static Weapon _instance = null;
	
	private Weapon() {}

	public static Weapon getInstance() {
		if (_instance == null) {
			_instance = new Weapon();
		}
		return _instance;
	}
}
