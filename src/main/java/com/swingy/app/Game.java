package com.swingy.app;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.swingy.app.DataLoader.DataLoader;
import com.swingy.app.Mob.Heroes.Hero;
import com.swingy.app.Mob.Monster;
import com.swingy.app.Renderer.ConsoleRenderer;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;

public class Game {
	public static Renderer		_renderer = null; // todo set as private
	private static List<Hero>	_heroes = null;

	public static void MovePlayer(Input input, Hero hero, boolean forward) {
		// todo test direction must be UP/DOWN/LEFT/RIGHT
		Vector2 direction = new Vector2();

		switch (input.getType()) {
			case UP:
				direction.y--;
				break;
			case DOWN:
				direction.y++;
				break;
			case LEFT:
				direction.x--;
				break;
			case RIGHT:
				direction.x++;
				break;
		}
		if (!forward)
			direction.multiply(-1);
		hero.getPosition().add(direction);
	}

	private static void HandleGameInput(Input input) {
		Map map = _renderer.getMap();
		Hero hero = map.getHero();
		if (input.getType() == Input.InputType.NONE)
			return;

		switch (input.getType())
		{
			case NONE:
				break;
			
			case UP:
			case DOWN:
			case LEFT:
			case RIGHT: {
				MovePlayer(input, hero, true);
				_renderer.render();
				Monster monster = map.getMonsterAtPos(hero.getPosition());
				if (monster != null)
					Fight.StartFight(input, hero, monster, _renderer);
				break;
			}
			case ESC:
				_renderer.closeGame();
				break;
			default:
				_renderer.addPopup("invalid input");
				break;
		}
	}

	public static void HandleInput(Input input) {
		if (_renderer.getMenu() == Renderer.Menu.GAME)
			HandleGameInput(input);
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
			// todo add test failed
			_renderer.addPopup("Failed to load characters : " + e.toString());
			_heroes = new ArrayList<Hero>();
		}
		
		while (true)
		{
			_renderer.render();
			HandleInput(_renderer.getInputAction());
		}
	}

	public static boolean heroNameExist(String name)
	{
		if (_heroes == null)
			return (false);
		for (Hero hero : _heroes)
			if (hero.getName().equals(name))
				return (true);
		return (false);
	}

	public static String[] getHeroesName() {
		// todo test _heroes must not be null
		String[] res = new String[_heroes.size()];
		for (int i = 0; i < _heroes.size(); i++) {
			res[i] = _heroes.get(i).getName();
		}
		return res;
	}

	public static Hero	getHero(int idx) {
		// todo test _heroes must not be null and idx < heroes size
		return _heroes.get(idx);
	}

	public static boolean createCharacter(String a_cls, String a_name) {
		try {
			Class<?> cls = Class.forName("com.swingy.app.Mob.Heroes." + a_cls);
			if (cls == Hero.class || !Hero.class.isAssignableFrom(cls))
				throw new ClassNotFoundException(a_cls);
	
			Hero hero = (Hero) cls.getDeclaredConstructor(String.class).newInstance(a_name);
			_heroes.add(hero);
			DataLoader.saveHero(hero);
			// todo add bdd
			return (true);
		} catch (ClassNotFoundException
			| SecurityException
			| InstantiationException
			| IllegalAccessException
			| IllegalArgumentException
			| InvocationTargetException
			| NoSuchMethodException
			| SQLException e) {
				// todo add test failed
				if (e instanceof ClassNotFoundException)
					_renderer.addPopup("Hero class '" + a_cls + "' doesnt exist." + e.toString());
				else
					_renderer.addPopup("An error occured while generating Hero : "+ e.toString());
			return (false);
		}
	}

	public static void removeCharacter(Hero hero) {
		try {
			DataLoader.removeHero(hero);
			_heroes.remove(hero);
		} catch (Exception err) {
			System.err.println(err); // todo
		}
	}
}