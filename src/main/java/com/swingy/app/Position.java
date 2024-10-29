package com.swingy.app;

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
}
