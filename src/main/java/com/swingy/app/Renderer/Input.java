package com.swingy.app.Renderer;

public class Input {
	public enum InputType {
		NONE,
		UP,
		RIGHT,
		DOWN,
		LEFT,
		CLICK,
	};

	private InputType	_input;
	private String		_value;

	public Input(InputType a_type, String a_value)
	{
		_input = a_type;
		_value = a_value;
	}

	public InputType	getType()
	{
		return _input;
	}

	public String	getValue()
	{
		return _value;
	}
}
