package com.swingy.app.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Scanner;

import com.swingy.app.Map;
import com.swingy.app.Position;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.InputText;
import com.swingy.app.Renderer.Element.SelectButton;
import com.swingy.app.Renderer.Element.TextButton;
import com.swingy.app.Renderer.Page.CreateChar;
import com.swingy.app.Renderer.Page.Main;
import com.swingy.app.Renderer.Page.Page;

import main.java.com.swingy.app.RawConsoleInput;

public class ConsoleRenderer extends Renderer {
	private int		_consoleSizeX;
	private int		_consoleSizeY;
	private int		_currentButtonId;

	private String	rgbToANSICode(int color[], Boolean background)
	{
		// todo add protecc for color len
		return ("\u001b[" + (background ? "4" : 3) + "8;2;" + color[0] + ";" + color[1] + ";" + color[2] + "m");
	}

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
		_currentButtonId = 0;
	}

	private String fillTerminalColor(int color[])
	{
		// todo check size color
		String res = "\u001b[H";
		res += rgbToANSICode(color, true);
		for (int i = 0; i < _consoleSizeY - 1; i++)
			for (int j = 0; j < _consoleSizeX; j++)
				res += ' ';
		res += "\u001b[0m";
		return (res);
	}

	private String renderElements(Element[]	elements, Page page) {
		String	res = "";

		for (int i = 0; i < elements.length; i++) {
			Element elem = elements[i];

			int	screenX = (int) (elem.getX() * _consoleSizeX);
			int	screenY = (int) (elem.getY() * (_consoleSizeY - 1));
			res += "\u001b[" + screenY + ";" + screenX + "H"; // place cursor at element render position
			if (i == _currentButtonId)
				res += rgbToANSICode(new int[] {120, 120, 120}, true); // todo custom color
			else if (elem.getBackgroundColor() != null)
				res += rgbToANSICode(elem.getBackgroundColor(), true);
			else
				res += rgbToANSICode(page.backgroundColor, true);
			res += rgbToANSICode(elem.getTextColor(), false);
			if (elem instanceof TextButton)
				res += "• ";
			if (elem instanceof TextButton || elem instanceof InputText || elem instanceof SelectButton)
				res += "[" + i + "] ";
			res += elem.getText();
			if (elem instanceof SelectButton)
				res += " : < " + ((SelectButton) elem).getCurrentElement() + " >";
			if (elem instanceof InputText)
				res += " : " + ((InputText) elem).getInputText();
		}
		res += "\u001b[0m";
		return (res);
	}

	protected void pageChanged() {
		_currentButtonId = 0;
	}

	protected void renderPage(Page page) {
		String	renderStr = "\u001b[2J";

		renderStr += fillTerminalColor(page.backgroundColor);
		
		renderStr += renderElements(page.elements, page);
		
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

	private void handleControlInput() throws IOException
	{
		int c = RawConsoleInput.read(false);
		_popups.add(0, "yes : " + String.valueOf(c));
		
		
		if (c == '[') {
			c = RawConsoleInput.read(false);
			switch (c) {
				case 68: //left
					break;
				case 67: //right
					break;
				case 65: //up
					_currentButtonId--;
					if (_currentButtonId == -1)
						_currentButtonId = _page.elements.length - 1;
					break;
				case 66: //down
					_currentButtonId++;
					if (_currentButtonId >= _page.elements.length)
						_currentButtonId = 0;
					break;
				}
		}
	}

	public Input getInputAction()
	{
		if (_popups.size() != 0)
			System.out.print(_popups.remove(0));

		try {
			int c = RawConsoleInput.read(true);

			if (c == '\u001b') {
				handleControlInput();
				return new Input(Input.InputType.NONE, 0);
			} else {
				switch (c) {
					case 10:
						return new Input(Input.InputType.CLICK, _currentButtonId);
					default:
						return new Input(Input.InputType.NONE, 0);
				}

			}
		} catch (IOException e) {
			_popups.add(0, e.toString());
			return new Input(Input.InputType.NONE, 0);
		}



		// String inputError = "";
		// if (_popups.size() != 0) {
		// 	inputError = "(" + _popups.remove(0) + ") ";
		// }
		// System.out.printf("Enter command %s: ", inputError);

		
		// try {

		// 	char c = (char) RawConsoleInput.read(true);
		// 	System.out.printf("readed : %c    ", c);

		// 	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		// 	String input = reader.readLine();
		// 	String command = null;
		// 	String value = null;
		// 	if (input == null)
		// 		return new Input(Input.InputType.NONE, null);
		// 	int sep = input.indexOf(':');
		// 	if (sep == -1) {
		// 		command = input;
		// 		value = null;
		// 	} else {
		// 		command = input.substring(0, sep);
		// 		value = input.substring(sep + 1);
		// 	}
		// 	command = command.trim().toUpperCase();
		// 	if (value != null)
		// 		value = value.trim();
			
		// 	switch (command)
		// 	{
		// 		case "W":
		// 			return new Input(Input.InputType.UP, value);
		// 		case "A":
		// 			return (new Input(Input.InputType.LEFT, value));
		// 		case "S":
		// 			return (new Input(Input.InputType.DOWN, value));
		// 		case "D":
		// 			return (new Input(Input.InputType.RIGHT, value));
		// 		case "CLICK":
		// 			return (new Input(Input.InputType.CLICK, value));

		// 		case "":
		// 			return new Input(Input.InputType.NONE, value);
		// 		default:
		// 			_popups.add(0, "input not found");
		// 			return new Input(Input.InputType.NONE, value);
		// 	}
		// } catch (IOException e) {
		// 	_popups.add(0, e.toString());
		// 	return new Input(Input.InputType.NONE, null);
		// }
	}
}
