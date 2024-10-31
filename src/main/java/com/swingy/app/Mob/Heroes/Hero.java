package com.swingy.app.Mob.Heroes;

import java.util.ArrayList;

import com.swingy.app.Artifacts.Artifact;
import com.swingy.app.Mob.Mob;

public abstract class Hero extends Mob {
	protected Integer				_id;
	protected String				_name;
	protected long					_experience;
	protected int					_level;
	protected ArrayList<Artifact>	_artifacts;

	protected Hero(String a_name) {
		_name = a_name;
		_level = 1;
		_experience = 0;
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

	public Integer getId() {
		return _id;
	}

	public void setId(Integer id) {
		_id = id;
	}

	public String getName() {
		return _name;
	}
}
