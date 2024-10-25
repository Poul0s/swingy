package com.swingy.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.swingy.app.DataLoader.DataLoader;
import com.swingy.app.Heroes.Hero;
import com.swingy.app.Heroes.Wretch;
import com.swingy.app.Renderer.ConsoleRenderer;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Page.Main;

public class Game {
	private static Renderer 	_renderer = null;
	private static List<Hero>	_heroes = null;

	public static void HandleInput(Input input, Hero hero) //maybe move to each page ???
	{
		if (_renderer.getMenu() == Renderer.Menu.GAME)
		{
			switch (input.getType())
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
				// case back && exit && maybe save ???
				default:
					_renderer.addPopup("invalid input");
					break;
			}
		}
		else
			_renderer.getPage().HandleInput(input, _renderer);
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 1 || !(args[0].equals("console") || args[0].equals("gui")))
		{
			System.out.println("Usage: java -jar `App.jar` console|gui");
			System.exit(1);
		}

		
		if (args[0].equals("console"))
		_renderer = new ConsoleRenderer();
		if (args[0].equals("gui"))
		throw new Exception("Unimplemented render");
		
		try {
			_heroes = DataLoader.loadData();
		} catch (SQLException e) {
			_renderer.addPopup("Failed to load characters : " + e.toString());
			_heroes = new ArrayList<Hero>();
		}
		
		while (true)
		{
			_renderer.render();
			HandleInput(_renderer.getInputAction(), null);
		}
	}
}