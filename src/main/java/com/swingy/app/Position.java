package com.swingy.app;

import java.util.Objects;

public class Position {
	public int			x;
	public int			y;

	public Position() {
		x = 0;
		y = 0;
	}

	public Position(int a_x, int a_y) {
		x = a_x;
		y = a_y;
	}

	public Position(Position a_Position) {
		x = a_Position.x;
		y = a_Position.y;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Position that = (Position) o;
		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
}
