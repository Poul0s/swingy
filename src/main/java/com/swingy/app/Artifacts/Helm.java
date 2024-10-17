package com.swingy.app.Artifacts;

public final class Helm extends Artifact {
	private static Helm _instance = null;
	
	private Helm() {}

	public static Helm getInstance() {
		if (_instance == null) {
			_instance = new Helm();
		}
		return _instance;
	}
}
