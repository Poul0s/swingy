package com.swingy.app;

import java.util.HashMap;
import java.util.Random;

import com.swingy.app.Mob.Monster;
import com.swingy.app.Mob.Heroes.Hero;

public class Map {
	private Hero						_hero;
	private HashMap<Vector2, Monster>	_monsters;

	public enum blockType {
		NONE,
		EMPTY,
		PLAYER
	}

	
	public Map(Hero a_hero) {
		_monsters = new HashMap<>();
		_hero = a_hero;
		generateMonsters();
	}

	private double	getLineDist(int posMin, int posMax, int pos) {
		if (pos < 0)
			return -pos / (-posMin - 1);
		else
			return pos / (posMax - 1);
	}

	private void	generateMonsters() {
		int size = getSize();
		int	posMin = -((int)(size / 2));
		int	posMax = ((size - 1) / 2);
		Random random = new Random();

		_monsters.clear();

		// todo maybe parralelize this loop
		for (int y = posMin; y < posMax; y++) {
			double dist = getLineDist(posMin, posMax, y);
			boolean xSuperior = false;


			for (int x = posMin; x < posMax; x++) {
				if (x == 0 && y == 0)
					continue;


				if (xSuperior == false && Math.abs(x) > Math.abs(y))
					xSuperior = true;
				if (xSuperior == true)
					dist = getLineDist(posMin, posMax, x);
				
				double spawnPossibility = 0.1 + (0.3 * dist); // 10% if in the middle, 40% at bordure
				if (random.nextDouble() <= spawnPossibility)
					generateMonster(new Vector2(x, y), dist);
			}
		}
	}


	private void	generateMonster(Vector2 position, double dist) {

		int level = (int) (50 * dist); // 1 if in the middle, 50 + 35-45% of player level

		double additionalLevel = (0.45 * dist) + ((new Random().nextDouble()) * 0.1) - 0.1;
		if (additionalLevel > 0)
			level += (int) additionalLevel;
		
		if (level == 0)
			level++;

		Monster monster = new Monster(level, position);
		_monsters.put(position, monster);
	}

	private int getSize() {
		return (_hero.getLevel() - 1) * 5 + 10 - (_hero.getLevel() % 2);
	}

	public blockType getBlockAtPos(int x, int y) {
		if (_hero.getPosition().x == x && _hero.getPosition().y == y)
			return blockType.PLAYER;
		
		int	size = getSize();
		int	posMin = -((int)(size / 2));
		int	posMax = ((size - 1) / 2);
		if (x < posMin || x > posMax
			|| y < posMin || y > posMax)
			return blockType.NONE;
		return blockType.EMPTY;
	}

	public Monster getMonsterAtPos(Vector2 position) {
		Monster monster = _monsters.get(position);
		return monster;
	}

	public Hero getHero() {
		return _hero;
	}
}
