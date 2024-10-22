package com.swingy.app.Renderer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.swingy.app.Map;
import com.swingy.app.Position;

public class ConsoleRenderer extends Renderer {
	private int		_consoleSizeX;
	private int		_consoleSizeY;
	private String	_inputError;

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

	public ConsoleRenderer(Map a_map) {
		super(a_map);
		refreshConsoleSize();
		_inputError = "";
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
		System.out.printf("Enter command %s: ", _inputError);
		_inputError = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		try {
			String input = reader.readLine();
			String command = null;
			String value = null;
			if (input == null)
				return Input.NONE;
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
					return (Input.UP);
				case "A":
					return (Input.LEFT);
				case "S":
					return (Input.DOWN);
				case "D":
					return (Input.RIGHT);
				default:
					_inputError = "(input not found) ";
					return Input.NONE;
			}
		} catch (IOException e) {
			_inputError = "(" + e.toString() + ") ";
			return Input.NONE;
		}
	}
}
