package com.swingy.app;

enum Direction {
	NORTH,
	EAST,
	SOUTH,
	WEST
}

public class Position {
	public int			x;
	public int			y;
	public Direction	heading;

	public Position() {
		x = 0;
		y = 0;
		heading = Direction.NORTH;
	}

	public Position(int a_x, int a_y, int a_z, Direction a_heading) {
		a_x = 0;
		a_y = 0;
		a_heading = Direction.NORTH;
	}

	public Position(Position a_Position) {
		x = a_Position.x;
		y = a_Position.y;
		heading = a_Position.heading;
	}
}
