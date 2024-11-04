package com.swingy.app.Mob.Heroes;

public class Wretch extends Hero {
	public static int			startAttack = 0;
	public static int			startDefence = 0;
	public static int			startHitpoint = 0;

	public Wretch(String name) {
		super(name);
		this._attack += startAttack;
		this._defence += startDefence;
		this._hitPoints += startHitpoint;
	}
}
