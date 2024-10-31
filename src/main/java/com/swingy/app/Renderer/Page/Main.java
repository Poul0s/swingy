package com.swingy.app.Renderer.Page;

import java.util.Map;

import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.TextButton;

public final class Main extends Page {

	public Main() {
		elements = new Element[] {
			new TextButton(Map.of(
				"text", "Character creation",
				"x", 0.1f,
				"y", 0.2f,
				"onClick", (Element.OnClickEvent) (renderer) -> renderer.setMenu(Renderer.Menu.CREATE_CHAR)
			)),
			new TextButton(Map.of(
				"text", "Character selection",
				"x", 0.1f,
				"y", 0.3f,
				"onClick", (Element.OnClickEvent) (renderer) -> renderer.setMenu(Renderer.Menu.CHOOSE_CHAR)
			)),
			new TextButton(Map.of(
				"text", "Options",
				"x", 0.1f,
				"y", 0.4f,
				"onClick", (Element.OnClickEvent) (renderer) -> renderer.setMenu(Renderer.Menu.OPTIONS)
			)),
			new TextButton(Map.of(
				"text", "Exit",
				"x", 0.1f,
				"y", 0.5f,
				"onClick", new Element.OnClickEvent() {
					public void callback(Renderer renderer) {
						System.exit(1);
					};
				}
			)),
		};

		backgroundColor = new int[] {103, 168, 162};
	}

	public void HandleInput(Input input, Renderer renderer)
	{}
}
