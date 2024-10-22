package com.swingy.app.Heroes;

import java.util.ArrayList;
import java.util.Map;

import com.swingy.app.Position;
import com.swingy.app.Artifacts.Artifact;

public abstract class Hero {
	private Integer				_id;
	private String				_name;
	private String				_class;
	private int					_level;
	private long				_experience;
	private int					_attack;
	private int					_defence;
	private int					_hitPoints;
	private Position			_position;
	private ArrayList<Artifact>	_artifacts;

	protected Hero(String a_name, String a_class, Artifact a_artifact) {
		_name = a_name;
		_class = a_class;
		_level = 1;
		_experience = 0;
		_attack = 0;
		_defence = 0;
		_hitPoints = 0;
		_position = new Position();
		_id = null;
		_artifacts = new ArrayList<Artifact>();
	}

	private	long calculateLvlUpXpRequired() {
		return (_level * 1000 + ((long) Math.pow(_level - 1, 2)) * 450);
	}

	public void	addXp(long xp) {
		_experience += xp;
		long lvlUpXp = calculateLvlUpXpRequired();
		while (_experience >= lvlUpXp) {
			_experience -= lvlUpXp;
			_level += 1;
			lvlUpXp = calculateLvlUpXpRequired();
			throw new RuntimeException("lvl up unsupported yet"); // todo notificate renderer
		}
	}

	public void addArtifact(Artifact artifact)
	{
		_artifacts.add(artifact);
		// todo change attributes
	}

	public int getLevel() {
		return _level;
	}

	public void setLevel(int level) {
		_level = level;
	}

	public long getXp() {
		return _experience;
	}

	public void setXp(long xp) {
		_experience = xp;
	}

	public Position getPosition() {
		return _position;
	}

	public Integer getId() {
		return _id;
	}

	public void setId(Integer id) {
		_id = id;
	}
}
