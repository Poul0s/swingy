package com.swingy.app.Renderer.Element;

import java.util.Map;

public class SelectButton extends Element {
	private int			_current;
	private String[]	_choices;

	public SelectButton(Map<String, Object> parameters, String choices[]) {
		super(parameters);
		
		// todo check null for choices 
		_choices = choices;
		_current = 0;
	}

	public String getCurrentElement() {
		// todo check null for choices or current > length
		if (_choices.length == 0)
			return (null);
		return (_choices[_current]);
	}

	public void next() {
		// todo check null
		_current++;
		if (_current >= _choices.length)
			_current = 0;
	}

	public void previous() {
		// todo check null
		_current--;
		if (_current == -1)
			_current = _choices.length == 0 ? 0 : _choices.length - 1;
	}

	public void setChoices(String choices[]) {
		// todo check null
		_choices = choices;
		if (_current >= _choices.length)
			_current = _choices.length - 1;
	}
}
