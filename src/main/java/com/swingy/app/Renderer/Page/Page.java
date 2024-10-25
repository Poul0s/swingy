package com.swingy.app.Renderer.Page;

import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Element.Element;

public abstract class Page {
	// public static final	ArrayList<Element>	_elements;
	// public static final	int					_backgroundColor[];
	public Element[]	elements;
	public int[]		backgroundColor;

	public abstract	void HandleInput(Input input, Renderer renderer);
}
