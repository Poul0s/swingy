package com.swingy.app.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.swingy.app.Map;
import com.swingy.app.Position;
import com.swingy.app.Renderer.Page.MainPage;
import com.swingy.app.Renderer.Page.Page;
import com.swingy.app.Renderer.Page.Element.Element;
import com.swingy.app.Renderer.Page.Element.TextButton;

public class ConsoleRenderer extends Renderer {
	private int		_consoleSizeX;
	private int		_consoleSizeY;

	private void refreshConsoleSize()
	{
		try {
			String cols = System.getenv("COLUMNS");
			String lines = System.getenv("LINES");
			int colsValue = Integer.valueOf(cols);
			int linesValue = Integer.valueOf(lines);
			_consoleSizeX = colsValue;
			_consoleSizeY = linesValue;
		} catch (NumberFormatException exception) {
			System.err.println("Cannot get console size : " + exception.toString());
			_consoleSizeX = 30;
			_consoleSizeY = 30;
		}
	}
	// private void refreshConsoleSize() // standard input waiting for close (ctrl+d)...
	// {
		// try {
			// final int nbBytesMax = 2 + 4 + 1 + 4 + 1; // ESC[ + w + ; + h + R
			// byte[] bytes = new byte[nbBytesMax + 1];
			// int off = 0;
			// while (System.in.read(bytes, off, 1) != -1) {
			// 	System.out.println("rade");
			// 	if (bytes[off] == '\u001b')
			// 		off = 1;
			// 	else if (off != 0 && bytes[off] == 'R')
			// 		break;
			// 	else if (off == nbBytesMax)
			// 		off = 0;
			// 	else if (off != 0)
			// 		off++;
			// }
			// if (off != 0 && bytes[off] == 'R') {
			// 	bytes[off + 1] = '\0';
			// 	bytes[0] = 'E';
			// 	String res = Base64.getEncoder().encodeToString(bytes);
			// 	System.out.println(res);
			// } else {
			// 	String res = Base64.getEncoder().encodeToString(bytes);
			// 	System.out.println("no : " + res);
			// }
		// } catch (IOException e) {
		// 		System.err.println("Cannot get console size : " + e.toString());
		// 		_consoleSizeX = 30;
		// 		_consoleSizeY = 30;
		// }
	// }

	public ConsoleRenderer() {
		super();
		refreshConsoleSize();
	}

	private String renderElements(Element[]	elements) {
		String	res = "";
		for (Element elem : elements) {
			int	screenX = (int) (elem.getX() * _consoleSizeX);
			int	screenY = (int) (elem.getY() * (_consoleSizeY - 1));
			res += "\u001b[" + screenY + ";" + screenX + "H"; // place cursor at element render position
			// todo add colors and minWidth and minHeight for backgroundColor
			if (elem instanceof TextButton)
			res += "[" + elem.getId() + "] ";
			res += elem.getText();
		}
		return (res);
	}

	protected void renderMain() {
		String	renderStr = "\u001b[2J";
		
		renderStr += renderElements(MainPage.elements);
		
		renderStr += "\u001b[" + _consoleSizeY + ";0H";
		System.out.print(renderStr);
	}

	protected void renderMap() {
		String mapRenderStr = "\u001b[2J";
		Position playerPos = _map.getHero().getPosition();
		int offY = (_consoleSizeY - 1) / 2 - playerPos.x;
		int offX = (_consoleSizeX) / 2 - playerPos.y;

		for (int y = 0; y < _consoleSizeY - 1; y++) {
			for (int x = 0; x < _consoleSizeX; x++) {
				Map.blockType bT = _map.getBlockAtPos(x - offX, y - offY);
				switch (bT) {
					case NONE:
						mapRenderStr += ' ';
						break;
					case PLAYER:
						mapRenderStr += 'P';
						break;
					case MONSTER:
						mapRenderStr += 'M';
						break;
					case EMPTY:
						mapRenderStr += 'F';
						break;
				}
			}
			mapRenderStr += '\n';
		}
		System.out.print(mapRenderStr);
	}

	public Input getInputAction()
	{
		String inputError = "";
		if (_popups.size() != 0) {
			inputError = "(" + _popups.remove(0) + ") ";
		}
		System.out.printf("Enter command %s: ", inputError);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			String input = reader.readLine();
			String command = null;
			String value = null;
			if (input == null)
				return new Input(Input.InputType.NONE, null);
			int sep = input.indexOf(':');
			if (sep == -1) {
				command = input;
				value = null;
			} else {
				command = input.substring(0, sep);
				value = input.substring(sep + 1);
			}
			System.out.println("command : " + command);
			System.out.println("value : " + value);
			command = command.trim().toUpperCase();
			if (value != null)
				value = value.trim();
			
			switch (command)
			{
				case "W":
					return new Input(Input.InputType.UP, value);
				case "A":
					return (new Input(Input.InputType.LEFT, value));
				case "S":
					return (new Input(Input.InputType.DOWN, value));
				case "D":
					return (new Input(Input.InputType.RIGHT, value));
				case "CLICK":
					return (new Input(Input.InputType.CLICK, value));

				case "":
					return new Input(Input.InputType.NONE, value);
				default:
					_popups.add(0, "input not found");
					return new Input(Input.InputType.NONE, value);
			}
		} catch (IOException e) {
			_popups.add(0, e.toString());
			return new Input(Input.InputType.NONE, null);
		}
	}
}
