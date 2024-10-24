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

	public Position(int a_x, int a_y) {
		x = a_x;
		y = a_y;
		heading = Direction.NORTH;
	}

	public Position(int a_x, int a_y, Direction a_heading) {
		x = a_x;
		y = a_y;
		heading = a_heading;
	}

	public Position(Position a_Position) {
		x = a_Position.x;
		y = a_Position.y;
		heading = a_Position.heading;
	}
}
