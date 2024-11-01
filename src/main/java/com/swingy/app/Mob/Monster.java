package com.swingy.app.Mob;

import java.util.Random;

import com.swingy.app.Vector2;

public class Monster extends Mob {
	public Monster(int level, Vector2 position) {
		Random random = new Random();

		int attr_value = generate_attribute_value(random, level);
		_attack = attr_value;
		level -= attr_value;

		attr_value = generate_attribute_value(random, level);
		_defence = attr_value;
		level -= attr_value;

		_hitPoints = level;

		_position = position;
	}

	private int	generate_attribute_value(Random random, int max) {
		int res = 0;
		for (int i = 0; i < max; i++)
			if (random.nextDouble() <= 1/3)
				res++;
		return res;
	}
}
