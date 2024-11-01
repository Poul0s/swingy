package com.swingy.app.Renderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Base64;

import com.swingy.app.Map;
import com.swingy.app.Vector2;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.InputText;
import com.swingy.app.Renderer.Element.SelectButton;
import com.swingy.app.Renderer.Element.TextButton;
import com.swingy.app.Renderer.Page.CreateChar;
import com.swingy.app.Renderer.Page.Main;
import com.swingy.app.Renderer.Page.Page;

import com.swingy.app.RawConsoleInput;

public class ConsoleRenderer extends Renderer {
	private int		_consoleSizeX;
	private int		_consoleSizeY;
	private int		_currentButtonId;

	private String	rgbToANSICode(int color[], Boolean background)
	{
		// todo test color len
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
	// 	try {
	// 		System.out.print("\u001b[9999;9999H\u001b[6n");
	// 		final int nbBytesMax = 2 + 4 + 1 + 4 + 1; // ESC[ + w + ; + h + R + '\0'
	// 		byte[] bytes = new byte[nbBytesMax + 1];
	// 		int off = 0;

	// 		while ((bytes[off] = (byte) RawConsoleInput.read(true)) >= 0) {
	// 			if (bytes[off] == '\u001b')
	// 				off = 1;
	// 			else if (off != 0 && bytes[off] == 'R')
	// 				break;
	// 			else if (off == nbBytesMax)
	// 				off = 0;
	// 			else if (off != 0)
	// 				off++;
	// 		}
	// 		if (off != 0 && bytes[off] == 'R') {
	// 			bytes[off + 1] = '\0';
	// 			bytes[0] = 'E';
	// 			String res = Base64.getEncoder().encodeToString(bytes);
	// 			System.err.println(res);
	// 		} else {
	// 			String res = Base64.getEncoder().encodeToString(bytes);
	// 			System.err.println("no : " + res);
	// 		}
	// 	} catch (IOException e) {
	// 			System.err.println("Cannot get console size : " + e.toString());
	// 			_consoleSizeX = 30;
	// 			_consoleSizeY = 30;
	// 	}
	// }

	public ConsoleRenderer() {
		super();
		refreshConsoleSize();
		_currentButtonId = 0;
	}

	private String fillTerminalColor(int color[])
	{
		// todo test size color 
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
		Vector2 playerPos = _map.getHero().getPosition();
		int offY = (_consoleSizeY - 1) / 2 - playerPos.y;
		int offX = (_consoleSizeX) / 2 - playerPos.x;

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
					case EMPTY:
						mapRenderStr += 'F';
						break;
				}
			}
			mapRenderStr += '\n';
		}
		System.out.print(mapRenderStr);
	}

	public int askPopup(String message, String[] choices) {
		// todo disable cursor or set pos at last line


		// separate message in lines
		int maxChar = (int) (_consoleSizeX * 0.75);
		int maxLines = (int) (_consoleSizeY * 0.5);
		String[] words = message.split("\\s+");
		ArrayList<String> lines = new ArrayList();
		String currentLine = null;
		int maxDescLineLength = 0;
		for (int i = 0; i < words.length; i++) {
			boolean pushLine = false;
			
			// long word is splitted to next line, maybe do something to split it also to previous line ?
			if (currentLine == null) {
				if (words[i].length() > maxChar) {
					currentLine = words[i].substring(0, maxChar);
					words[i] = words[i].substring(maxChar);
					i--; // decrement to make another iteration to this word
					pushLine = true;
				}
				else
					currentLine = words[i];
			}
			else if (currentLine.length() + 1 + words[i].length() > maxChar)
				pushLine = true;
			else
				currentLine += " " + words[i];
			
			if (pushLine) {
				if (lines.size() == maxLines - 1) {
					if (currentLine.length() + 3 > maxChar)
						currentLine = currentLine.substring(0, maxChar - 3);
					currentLine = currentLine + "...";
					i = words.length;
				}
				lines.add(currentLine);
				maxDescLineLength = Math.max(maxDescLineLength, currentLine.length());
				currentLine = null;
			}
		}
		if (currentLine != null) {
			lines.add(currentLine);
			maxDescLineLength = Math.max(maxDescLineLength, currentLine.length());
		}

		// resize choices
		int choicesLineLength = (choices.length - 1);
		int choicesMaxLen = (maxChar - (choices.length - 1)) / choices.length;
		for (int i = 0; i < choices.length; i++) {
			if (choices[i].length() > choicesMaxLen)
			choices[i] = choices[i].substring(0, choicesMaxLen);
			choicesLineLength += choices[i].length();
		}

		// define popup length
		int popupWidth = Math.max(choicesLineLength, maxDescLineLength);
		choicesMaxLen = (popupWidth - (choices.length - 1)) / choices.length;
		int popupHeight = lines.size() + 2;

		// draw desc
		int startPosY = (_consoleSizeY - popupHeight) / 2;
		int startPosX = (_consoleSizeX - popupWidth) / 2;
		String renderStr = "\u001b[" + startPosY + ";" + startPosX + "H\u001b[44m";
		for (String line : lines) {
			// todo center lines
			int paddingLeft = (popupWidth - line.length()) / 2;
			renderStr += " ".repeat(paddingLeft)
						+ line
						+ " ".repeat((popupWidth - line.length()) - paddingLeft)
						+ "\n\u001b[" + startPosX + "G";
		}
		renderStr += " ".repeat(popupWidth) + "\n";
		System.out.print(renderStr);

		
		
		// handle choices
		int currentChoice = 0;
		while (true) {
			renderStr = "\u001b[" + startPosX + "G";
			for (int i = 0; i < choices.length; i++) {
				int paddingLeft = (choicesMaxLen - choices[i].length()) / 2;
				renderStr += " ".repeat(paddingLeft);
				if (i == currentChoice)
					renderStr += "\u001b[41m";
				else
					renderStr += "\u001b[48;5;7m";
				renderStr += choices[i] + "\u001b[44m" + " ".repeat((choicesMaxLen - choices[i].length()) - paddingLeft);
				if (i != choices.length - 1)
					renderStr += " ";
			}
			System.out.print(renderStr);
			Input input = getInputAction();
			switch (input.getType()) {
				case ENTER:
					System.out.print("\u001b[0m");
					return currentChoice;
				case LEFT:
					currentChoice--;
					if (currentChoice == -1)
						currentChoice = Math.max(0, choices.length - 1);
					break;
				case RIGHT:
					currentChoice++;
					if (currentChoice >= choices.length)
						currentChoice = 0;
					break;
				default:
					break;

			}
		}
	}

	private Input	handleControlInput() throws IOException
	{
		int c = RawConsoleInput.read(false);
		if (c == '[') {
			c = RawConsoleInput.read(false);
			switch (c) {
				case 68: //left
					if (_page == null)
						return new Input(Input.InputType.LEFT, 0);
					if (_page.elements[_currentButtonId] instanceof SelectButton)
						((SelectButton) _page.elements[_currentButtonId]).previous();
					break;
				case 67: //right
					if (_page == null)
						return new Input(Input.InputType.RIGHT, 0);
					if (_page.elements[_currentButtonId] instanceof SelectButton)
						((SelectButton) _page.elements[_currentButtonId]).next();
					break;
				case 65: //up // todo prevent from currentButtonId point to textElement
					if (_page == null)
						return new Input(Input.InputType.UP, 0);
					_currentButtonId--;
					if (_currentButtonId == -1)
						_currentButtonId = _page.elements.length - 1;
					break;
				case 66: //down // todo prevent from currentButtonId point to textElement
					if (_page == null)
						return new Input(Input.InputType.DOWN, 0);
					_currentButtonId++;
					if (_currentButtonId >= _page.elements.length)
						_currentButtonId = 0;
					break;
			}
		} else if (c == -2)
			return new Input(Input.InputType.ESC, 0);
		return new Input(Input.InputType.NONE, 0);
	}

	public String inputLine(String input) throws IOException
	{
		RawConsoleInput.resetConsoleMode();
		System.out.print(input);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String res = reader.readLine();
		if (res == null)
			return "";
		else
			return res;
	}

	public Input getInputAction()
	{
		if (_popups.size() != 0)
			System.out.print(_popups.remove(0));

		try {
			int c = RawConsoleInput.read(true);

			if (c == '\u001b') {
				return handleControlInput();
			} else {
				switch (c) {
					case 10:
						if (_page != null) {
							if (_page.elements[_currentButtonId] instanceof InputText)
								((InputText) _page.elements[_currentButtonId]).setInputText(inputLine("Enter new value : "));
							else
								_page.elements[_currentButtonId].onClick(this);
						}
						else
							return new Input(Input.InputType.ENTER, 0);
						break;
					case 'w':
					case 'W':
						return new Input(Input.InputType.UP, 0);
					case 's':
					case 'S':
						return new Input(Input.InputType.DOWN, 0);
					case 'a':
					case 'A':
						return new Input(Input.InputType.LEFT, 0);
					case 'd':
					case 'D':
						return new Input(Input.InputType.RIGHT, 0);

				}
				return new Input(Input.InputType.NONE, 0);

			}
		} catch (IOException e) {
			_popups.add(0, e.toString());
			return new Input(Input.InputType.NONE, 0);
		}
	}
}
