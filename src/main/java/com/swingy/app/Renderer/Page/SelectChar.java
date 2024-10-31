package com.swingy.app.Renderer.Page;

import java.util.Map;

import com.swingy.app.Game;
import com.swingy.app.Mob.Heroes.Hero;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;
import com.swingy.app.Renderer.Element.Element;
import com.swingy.app.Renderer.Element.InputText;
import com.swingy.app.Renderer.Element.SelectButton;
import com.swingy.app.Renderer.Element.TextButton;
import com.swingy.app.Renderer.Element.TextElement;
import com.swingy.app.Renderer.Page.Page;

public class SelectChar extends Page {
	public SelectChar() {
		String[] heroesName = Game.getHeroesName();

		elements = new Element[] {
			new SelectButton(Map.of(
				"text", "Choose char",
				"x", 0.1f,
				"y", 0.2f
			), heroesName, this::onHeroSwitch),
	
			new TextButton(Map.of(
				"text", "Select char",
				"x", 0.1f,
				"y", 0.3f,
				"onClick", (Element.OnClickEvent) this::loadHero
			)),

			new TextButton(Map.of(
				"text", "Back",
				"x", 0.1f,
				"y", 0.4f,
				"onClick", (Element.OnClickEvent) (r) -> r.setMenu(Renderer.Menu.MAIN)
			)),

			new TextElement(Map.of(
				"text", "Class: ?",
				"x", 0.6f,
				"y", 0.2f
			)),

			new TextElement(Map.of(
				"text", "Level: ?",
				"x", 0.6f,
				"y", 0.3f
			)),

			new TextElement(Map.of(
				"text", "Xp: ?",
				"x", 0.6f,
				"y", 0.4f
			)),

			new TextElement(Map.of(
				"text", "Attack: ?",
				"x", 0.6f,
				"y", 0.5f
			)),

			new TextElement(Map.of(
				"text", "Defence: ?",
				"x", 0.6f,
				"y", 0.6f
			)),

			new TextElement(Map.of(
				"text", "Hit point: ?",
				"x", 0.6f,
				"y", 0.7f
			)),
		};
		backgroundColor = new int[] {103, 168, 162};

		onHeroSwitch(heroesName.length == 0 ? -1 : 0);
	}

	private void onHeroSwitch(int idx) {
		if (idx == -1) {
			for (int i = 3; i <= 8; i++)
				((TextElement) (elements[i])).setValues("?");
		} else {
			Hero hero = Game.getHero(idx);
			((TextElement) (elements[3])).setValues(hero.getClass().getSimpleName());
			((TextElement) (elements[4])).setValues(String.valueOf(hero.getLevel()));
			((TextElement) (elements[5])).setValues(String.valueOf(hero.getXp()));
			((TextElement) (elements[6])).setValues(String.valueOf(hero.getAttack()));
			((TextElement) (elements[7])).setValues(String.valueOf(hero.getDefence()));
			((TextElement) (elements[8])).setValues(String.valueOf(hero.getHitPoint()));
		}
	}

	private void loadHero(Renderer renderer) {

		int heroIdx = ((SelectButton) elements[0]).getCurrentElementIdx();
		if (heroIdx == -1)
			return;
		Hero hero = Game.getHero(heroIdx);
		renderer.openGame(hero);
	}

	public void HandleInput(Input input, Renderer renderer) {
		switch (input.getType())
		{
			case ESC:
				renderer.setMenu(Renderer.Menu.MAIN);
				break;
			default:
				break;
		}
	}

}
