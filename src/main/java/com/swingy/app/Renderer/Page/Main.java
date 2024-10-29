package com.swingy.app.Renderer.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.swingy.app.Position;
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
				"y", 0.2f
			)),
			new TextButton(Map.of(
				"text", "Character selection",
				"x", 0.1f,
				"y", 0.3f
			)),
			new TextButton(Map.of(
				"text", "Options",
				"x", 0.1f,
				"y", 0.4f
			)),
			new TextButton(Map.of(
				"text", "Exit",
				"x", 0.1f,
				"y", 0.5f
			)),
		};

		backgroundColor = new int[] {103, 168, 162};
	}

	public void HandleInput(Input input, Renderer renderer)
	{
		switch (input.getType())
		{
			case CLICK:
			{
				switch (input.getValue()) {
					case 0:
						renderer.setMenu(Renderer.Menu.CREATE_CHAR);
						break;
					case 1:
						renderer.setMenu(Renderer.Menu.CHOOSE_CHAR);
						break;
					case 2:
						renderer.setMenu(Renderer.Menu.OPTIONS);
						break;
					case 3:
						System.exit(0); // todo save char maybe ???
						break;
				}
				break;
			}
		}
	}
}
