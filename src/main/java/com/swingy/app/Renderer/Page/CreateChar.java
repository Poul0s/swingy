package com.swingy.app.Renderer.Page;

import java.util.Map;

import com.swingy.app.Game;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.InputText;
import com.swingy.app.Renderer.Element.SelectButton;
import com.swingy.app.Renderer.Element.TextButton;

public class CreateChar extends Page {

	public CreateChar() {
		elements = new Element[] {
			new SelectButton(Map.of(
				"text", "Choose class",
				"x", 0.1f,
				"y", 0.2f
			), new String[] {"Wretch"}),
	
			// new SelectButton(Map.of(
			// 	"text", "Choose Artifact",
			// 	"x", 0.1f,
			// 	"y", 0.3f
			// ), new String[] {"Armor", "Helm", "Weapon"}),
			
			new InputText(Map.of(
				"text", "Character name",
				"x", 0.1f,
				"y", 0.3f
			)),
			
			new TextButton(Map.of(
				"text", "Create character",
				"x", 0.1f,
				"y", 0.4f
			)),
	
			new TextButton(Map.of(
				"text", "Back",
				"x", 0.1f,
				"y", 0.5f
			)),
		}; // todo add start stats (level, xp, attack, defense, hit points)
		backgroundColor = new int[] {103, 168, 162};
	}

	public void HandleInput(Input input, Renderer renderer)
	{

		switch (input.getType())
		{
			case CLICK:
			{
				switch (input.getValue()) {
					case 2:
					{
						String name = ((InputText)elements[1]).getInputText();
						if (name.isBlank())
							renderer.addPopup("Character name cannot be empty");
						else if (Game.heroNameExist(name))
							renderer.addPopup("Name already used.");
						else
						{
							String cls = ((SelectButton)elements[0]).getCurrentElement();
							if (Game.createCharacter(cls, name))
								renderer.setMenu(Renderer.Menu.MAIN);
						}
						break;
					}
					case 3:
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
