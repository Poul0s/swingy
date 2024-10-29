package com.swingy.app.Renderer;

public class Input {
	public enum InputType {
		NONE,
		UP,
		RIGHT,
		DOWN,
		LEFT,
		CLICK,
		ESC
	};

	private InputType	_input;
	private int			_value;

	public Input(InputType a_type, int a_value)
	{
		_input = a_type;
		_value = a_value;
	}

	public InputType	getType()
	{
		return _input;
	}

	public int	getValue()
	{
		return _value;
	}
}
