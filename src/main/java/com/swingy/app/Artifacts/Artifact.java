package com.swingy.app.Artifacts;

public abstract class Artifact {
	private Integer _id;
	private int		_level;

	protected Artifact(int level) {
		_id = null;
		_level = level;
	}

	public Integer getId() {
		return _id;
	}

	public void setId(Integer id) {
		_id = id;
	}

	public int getLevel() {
		return (_level);
	}
}
