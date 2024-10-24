package com.swingy.app.Renderer.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.swingy.app.Position;
import com.swingy.app.Renderer.Page.Element.Element;
import com.swingy.app.Renderer.Page.Element.TextButton;

public final class MainPage extends Page {
	public static final Element[] elements = new Element[] {
		new TextButton("1", Map.of(
			"text", "Character creation",
			"x", 0.1f,
			"y", 0.2f
		)),
		new TextButton("2", Map.of(
			"text", "Character selection",
			"x", 0.1f,
			"y", 0.3f
		)),
		new TextButton("3", Map.of(
			"text", "Options",
			"x", 0.1f,
			"y", 0.4f
		)),
		new TextButton("4", Map.of(
			"text", "Exit",
			"x", 0.1f,
			"y", 0.5f
		)),
	};
}
