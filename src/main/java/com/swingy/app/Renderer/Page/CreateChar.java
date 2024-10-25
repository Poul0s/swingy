package com.swingy.app.Renderer.Page;

import java.util.Map;

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
	
			new SelectButton(Map.of(
				"text", "Choose Artifact",
				"x", 0.1f,
				"y", 0.3f
			), new String[] {"Armor", "Helm", "Weapon"}),
			
			new InputText(Map.of(
				"text", "Character",
				"x", 0.1f,
				"y", 0.4f
			)),
			
			new TextButton(Map.of(
				"text", "Create character",
				"x", 0.1f,
				"y", 0.5f
			)),
	
			new TextButton(Map.of(
				"text", "Back",
				"x", 0.1f,
				"y", 0.6f
			)),
		};
		backgroundColor = new int[] {230, 160, 48};
	}

	public void HandleInput(Input input, Renderer renderer)
	{

		// switch (input.getType())
		// {
		// 	case CLICK:
		// 	{
		// 		switch (input.getValue()) {
		// 			case 0:
		// 				renderer.setMenu(Renderer.Menu.CREATE_CHAR);
		// 				break;
		// 			case 1:
		// 				renderer.setMenu(Renderer.Menu.CHOOSE_CHAR);
		// 				break;
		// 			case 2:
		// 				renderer.setMenu(Renderer.Menu.OPTIONS);
		// 				break;
		// 			case 3:
		// 				System.exit(0); // todo save char maybe ???
		// 				break;
		// 		}
		// 		break;
		// 	}
		// }
	}
}
