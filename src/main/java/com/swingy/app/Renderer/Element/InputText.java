package com.swingy.app.Renderer.Element;

import java.util.Map;

public class InputText extends Element {
	private String	_input;

	public InputText(Map<String, Object> a_parameters) {
		super(a_parameters);
		_input = "";
	}

	public String getInputText() {
		return _input;
	}

	public void	setInputText(String input) {
		_input = input;
	}
}
