package com.swingy.app.Renderer.Page;

import java.util.Map;

import com.swingy.app.Game;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.InputText;
import com.swingy.app.Renderer.Element.SelectButton;
import com.swingy.app.Renderer.Element.TextButton;
import com.swingy.app.Renderer.Page.Page;

public class SelectChar extends Page {
	public SelectChar() {
		elements = new Element[] {
			new SelectButton(Map.of(
				"text", "Choose class",
				"x", 0.1f,
				"y", 0.2f
			), new String[] {}),
	
			new TextButton(Map.of(
				"text", "Back",
				"x", 0.1f,
				"y", 0.3f
			)),
		}; // todo add stats (level, xp, attack, defense, hit points)
		backgroundColor = new int[] {103, 168, 162};
	}

	public void HandleInput(Input input, Renderer renderer) {
		switch (input.getType())
		{
			case CLICK:
			{
				switch (input.getValue()) {
					case 1:
						renderer.setMenu(Renderer.Menu.MAIN);
						break;
				}
				break;
			}
			case ESC:
				renderer.setMenu(Renderer.Menu.MAIN);
				break;
			default:
				break;
		}
	}

}
