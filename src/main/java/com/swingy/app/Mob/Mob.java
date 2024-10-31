package com.swingy.app.Mob;

import com.swingy.app.Position;

public abstract class Mob {
	protected int					_attack;
	protected int					_defence;
	protected int					_hitPoints;
	protected Position				_position;

	protected Mob() {
		_attack = 0;
		_defence = 0;
		_hitPoints = 0;
		_position = new Position();
	}

	public Position getPosition() {
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
