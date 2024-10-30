package com.swingy.app.Heroes;

public class Wretch extends Hero {
	public static int			startAttack = 1;
	public static int			startDefence = 1;
	public static int			startHitpoint = 1;

	public Wretch(String name) {
		super(name);
		this._attack += startAttack;
		this._defence += startDefence;
		this._hitPoints += startHitpoint;
	}
}
