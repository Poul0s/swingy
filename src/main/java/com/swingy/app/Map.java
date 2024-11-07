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

	private double	getLineDistRatio(int posMin, int posMax, int pos) {
		if (pos < 0)
			return ((double) -pos) / ((double) -posMin);
		else
			return ((double) pos) / ((double) posMax);
	}

	private void	generateMonsters() {
		int size = getSize();
		int	posMin = -((int)(size / 2));
		int	posMax = ((size - 1) / 2);
		Random random = new Random();

		_monsters.clear();

		// todo maybe parralelize this loop
		for (int y = posMin; y < posMax; y++) {
			double distRatioY = getLineDistRatio(posMin, posMax, y);

			for (int x = posMin; x < posMax; x++) {
				if ((x == 0 && y == 0) 
					|| (x == _hero.getPosition().x && y == _hero.getPosition().y))
					continue;

				double distRatio = Math.max(distRatioY, getLineDistRatio(posMin, posMax, x)); // not opti

				double spawnPossibility = 0.2 + (0.5 * distRatio); // 20% if in the middle, 70% at bordure
				if (random.nextDouble() <= spawnPossibility)
					generateMonster(new Vector2(x, y), Math.max(Math.abs(x), Math.abs(y)), distRatio);
			}
		}
	}

	private void	generateMonster(Vector2 position, int dist, double distRatio) {
		int level = (int) (dist); // 1 if in the middle, 55 + 35-45% of player level
		
		double additionalLevel = (0.45 * distRatio) + ((new Random().nextDouble()) * 0.1) - 0.1;
		if (additionalLevel > 0)
			level += (int) additionalLevel;

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

	public void refreshMapSize() {
		generateMonsters();
	}
}
