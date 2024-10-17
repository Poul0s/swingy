package com.swingy.app.Renderer;

import com.swingy.app.Map;
import com.swingy.app.Position;

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

	public ConsoleRenderer(Map a_map) {
		super(a_map);
		refreshConsoleSize();
	}

	protected void renderMap() {
		String mapRenderStr = "\u001b[2J";
		Position playerPos = _map.getHero().getPosition();
		int offY = (_consoleSizeY - 1) / 2;
		int offX = _consoleSizeX / 2;

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
}
