package com.swingy.app;

import com.swingy.app.Heroes.Hero;
import com.swingy.app.Heroes.Wretch;
import com.swingy.app.Renderer.ConsoleRenderer;
import com.swingy.app.Renderer.Renderer;

public class Game {
	private static Renderer _renderer = null;

	public static void HandleInput(Renderer.Input input, Hero hero)
	{
		// switch with current menu
		switch (input)
		{
			case UP:
				hero.getPosition().x--;
				break;
			case DOWN:
				hero.getPosition().x++;
				break;
			case LEFT:
				hero.getPosition().y--;
				break;
			case RIGHT:
				hero.getPosition().y++;
				break;
		}
	}

	public static void main(String[] args) throws Exception {

		if (args.length != 1 || !(args[0].equals("console") || args[0].equals("gui")))
		{
			System.out.println("Usage: java -jar `App.jar` console|gui");
			System.exit(1);
		}

		Hero hero = new Wretch("UNDEF_NAME");
		Map map = new Map(hero);
		
		if (args[0].equals("console"))
			_renderer = new ConsoleRenderer(map);
		if (args[0].equals("gui"))
			throw new Exception("Unimplemented render");

		while (true)
		{
			_renderer.render();
			_renderer.getInputAction();
		}
	}
}