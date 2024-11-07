package com.swingy.app.Mob;

import java.util.Random;

import com.swingy.app.Vector2;

public abstract class Mob {
	private double	_health;

	protected int					_level;
	protected int					_attack;
	protected int					_defence;
	protected int					_hitPoints;
	protected Vector2				_position;

	protected Mob() {
		_level = 1;
		_attack = 1;
		_defence = 0;
		_hitPoints = 1;
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

	public int getLevel() {
		return _level;
	}

	public void setLevel(int level) {
		_level = level;
	}

	public double getHealth() {
		return _health;
	}

	public void setHealth(double health) {
		_health = health;
	}

	public void Attack(Mob target) {
		Random random = new Random();
		if (random.nextDouble() < 0.05) // dodge
			return;

		double health = target.getHealth();
		double attack = this.getAttack();
		if (random.nextDouble() < 0.2)
			attack *= 1.2; // critical
		double absorb = target.getDefence() * 0.8;
		attack -= absorb;
		if (attack <= 0.0)
			return;
		
		health -= attack;
		target.setHealth(health);
	}

	public static long	calculateLvlXp(int level) {
		return (level * 1000 + ((long) Math.pow(level - 1, 2)) * 450);
	}
}
