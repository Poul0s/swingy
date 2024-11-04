package com.swingy.app.Mob;

import java.util.Random;

import com.swingy.app.Vector2;

public class Monster extends Mob {
	public Monster(int level, Vector2 position) {
		Random random = new Random();

		_level = level;

		level -= 4;

		level -= 1; // hitpoint min as 1

		int attr_value = generate_attribute_value(random, level, 3);
		_attack = attr_value;
		level -= attr_value;

		attr_value = generate_attribute_value(random, level, 2);
		_defence = attr_value;
		level -= attr_value;

		_hitPoints += level;

		_position = position;
	}

	private int	generate_attribute_value(Random random, int max, int parts) {
		int res = 0;
		for (int i = 0; i < max; i++)
			if (random.nextDouble() <= 1.0 / parts)
				res++;
		return res;
	}
}
