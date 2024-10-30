package com.swingy.app.Renderer.Element;

import java.util.Map;

public class TextElement extends Element {
	private String	baseText;
	public TextElement(Map<String, Object> parameters) {
		super(parameters);
		baseText = getText();
	}

	public void setValues(String... args) {
		String newText = baseText;

		for (String arg : args) {
			newText = newText.replaceFirst("\\?", arg);
		}
		setText(newText);
	}
}
