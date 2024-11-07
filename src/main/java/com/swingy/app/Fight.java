package com.swingy.app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.xml.crypto.Data;

import com.swingy.app.Artifacts.Armor;
import com.swingy.app.Artifacts.Artifact;
import com.swingy.app.Artifacts.Helm;
import com.swingy.app.Artifacts.Weapon;
import com.swingy.app.DataLoader.DataLoader;
import com.swingy.app.Mob.Mob;
import com.swingy.app.Mob.Monster;
import com.swingy.app.Mob.Heroes.Hero;
import com.swingy.app.Renderer.Input;
import com.swingy.app.Renderer.Renderer;

public class Fight {
	private static final String		MSG_FIGHT = "You found a monster level %d (%d attack, %d defence, %d hitpoint). Do you want to fight ?";
	private static final String[]	MSG_FIGHT_CHOICES = {"Fight", "Run away"};
	private static final String		MSG_ARTIFACT = "You found an %s of level %d. Do you want to take it ?";
	private static final String[]	MSG_ARTIFACT_CHOICES = {"Yes", "No"};
	private static final String		MSG_DIE = "You just die and lost everything...";
	private static final String[]	MSG_DIE_CHOICES = {"Go back"};


	private static void FightWon(Hero hero, Monster monster, Renderer renderer) {
		Random random = new Random();
				
		double xpGainPercent = (random.nextDouble() * 0.35) + 1.15; // between 15% and 40%
		long xpGain = (long) (Mob.calculateLvlXp(monster.getLevel() - 1) * xpGainPercent);
		hero.addXp(xpGain, renderer);
		
		Artifact artifact = null;

		if (random.nextDouble() <= 0.75) { // 75% winning artifact
			ArrayList<Artifact> artifacts = new ArrayList<Artifact>();
			if (monster.getAttack() > 0)
				artifacts.add(new Weapon((int) Math.max(monster.getAttack() * 0.4, 1)));
			if (monster.getDefence() > 0)
				artifacts.add(new Armor((int) Math.max(monster.getDefence() * 0.4, 1)));
			if (monster.getHitPoint() > 0)
				artifacts.add(new Helm((int) Math.max(monster.getHitPoint() * 0.4, 1)));

			if (artifacts.size() > 0) {
				artifact = artifacts.get(random.nextInt(artifacts.size()));
			
				int res = renderer.askPopup(String.format(MSG_ARTIFACT, artifact.getClass().getSimpleName(), artifact.getLevel()), MSG_ARTIFACT_CHOICES);
				if (res == 0) {
					hero.addArtifact(artifact);
				}
			}
		}
		
		try {
			DataLoader.saveHero(hero);
			if (artifact != null)
				DataLoader.addArtifact(hero, artifact);
		} catch (SQLException err) {
			System.err.println(err);
		}
	}

	public static void StartFight(Input input, Hero hero, Monster monster, Renderer renderer) {
		int res = renderer.askPopup(String.format(MSG_FIGHT,
										monster.getLevel(),
										monster.getAttack(),
										monster.getDefence(),
										monster.getHitPoint()),
									MSG_FIGHT_CHOICES);
		renderer.render();

		if (res == 1) {
			Random random = new Random();
			if (random.nextBoolean())
				res = 0; // todo popup user failed to run away
			else {
				Game.MovePlayer(input, hero, false);
			}
		}

		if (res == 0) {
			hero.setHealth(hero.getHitPoint());
			monster.setHealth(monster.getHitPoint());

			while (hero.getHealth() > 0.0 && monster.getHealth() > 0.0) {
				hero.Attack(monster);
				if (monster.getHealth() > 0.0)
					monster.Attack(hero);
			}
			if (hero.getHealth() <= 0.0) {
				Game.removeCharacter(hero);
				renderer.askPopup(MSG_DIE, MSG_DIE_CHOICES);
				renderer.closeGame();
			} else 
				FightWon(hero, monster, renderer);
		}
	}
}
