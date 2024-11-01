package com.swingy.app;

import java.util.Objects;

public class Vector2 {
	public int			x;
	public int			y;

	public Vector2() {
		x = 0;
		y = 0;
	}

	public Vector2(int a_x, int a_y) {
		x = a_x;
		y = a_y;
	}

	public Vector2(Vector2 vec2) {
		x = vec2.x;
		y = vec2.y;
	}

	public void multiply(int val) {
		x *= val;
		y *= val;
	}

	public void add(Vector2 vec2) {
		x += vec2.x;
		y += vec2.y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Vector2 that = (Vector2) o;
		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
