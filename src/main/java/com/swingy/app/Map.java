package com.swingy.app;

import com.swingy.app.Heroes.Hero;

public class Map {
	private Hero	_hero;

	public enum blockType {
		NONE,
		EMPTY,
		PLAYER,
		MONSTER
	}

	
	public Map(Hero a_hero) {
		_hero = a_hero;
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
		// todo check monster
		return blockType.EMPTY;
	}

	public Hero getHero() {
		return _hero;
	}
}
