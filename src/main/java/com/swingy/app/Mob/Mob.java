package com.swingy.app.Mob;

import com.swingy.app.Vector2;

public abstract class Mob {
	protected int					_attack;
	protected int					_defence;
	protected int					_hitPoints;
	protected Vector2				_position;

	protected Mob() {
		_attack = 0;
		_defence = 0;
		_hitPoints = 0;
		_position = new Vector2();
	}

	public Vector2 getPosition() {
		return _position;
	}

	public int getAttack() {
		return _attack;
	}

	public int getDefence() {
		return _defence;
	}

	public int getHitPoint() {
		return _hitPoints;
	}
}
