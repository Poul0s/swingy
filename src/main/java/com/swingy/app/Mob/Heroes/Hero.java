package com.swingy.app.Mob.Heroes;

import java.util.ArrayList;

import com.swingy.app.Artifacts.Armor;
import com.swingy.app.Artifacts.Artifact;
import com.swingy.app.Artifacts.Helm;
import com.swingy.app.Artifacts.Weapon;
import com.swingy.app.Mob.Mob;
import com.swingy.app.Renderer.Renderer;

public abstract class Hero extends Mob {
	protected Integer				_id;
	protected String				_name;
	protected long					_experience;
	protected ArrayList<Artifact>	_artifacts;

	protected Hero(String a_name) {
		_name = a_name;
		_experience = 0;
		_id = null;
		_artifacts = new ArrayList<Artifact>();
	}

	public void	addXp(long xp, Renderer renderer) {
		_experience += xp;
		long lvlUpXp = Mob.calculateLvlXp(_level);
		if (_experience >= lvlUpXp) {
			while (_experience >= lvlUpXp) {
				_experience -= lvlUpXp;
				_level += 1;
				lvlUpXp = Mob.calculateLvlXp(_level);
				renderer.addPopup("You just leveled up to level " + _level + "!");
			}
			renderer.getMap().refreshMapSize();
		}

	}

	public void addArtifact(Artifact artifact)
	{
		_artifacts.add(artifact);
		if (artifact instanceof Weapon)
			_attack += artifact.getLevel();
		else if (artifact instanceof Armor)
			_defence += artifact.getLevel();
		else if (artifact instanceof Helm)
			_hitPoints += artifact.getLevel();
		// else
		/// todo test failed
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
