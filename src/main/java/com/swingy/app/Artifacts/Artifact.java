package com.swingy.app.Artifacts;

public abstract class Artifact {
	private Integer _id;

	protected Artifact() {
		_id = null;
	}

	public Integer getId() {
		return _id;
	}

	public void setId(Integer id) {
		_id = id;
	}
}
