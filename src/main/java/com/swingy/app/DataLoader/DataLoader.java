package com.swingy.app.DataLoader;

import java.util.List;
import java.util.ArrayList;
import java.lang.Thread.State;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.swingy.app.Game;
import com.swingy.app.Artifacts.Artifact;
import com.swingy.app.Mob.Heroes.Hero;

public class DataLoader {
	private static final String DB_URL = "jdbc:sqlite:game.db";
	private static Connection DB_CONN = null;

	private static void initTables() throws SQLException
	{
		Statement statement = DB_CONN.createStatement();
		statement.execute("""
			CREATE TABLE IF NOT EXISTS heroes (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				name VARCHAR(64) NOT NULL,
				class VARCHAR(64) NOT NULL,
				level INTEGER NOT NULL DEFAULT 0,
				xp LONG NOT NULL DEFAULT 0,
				x INTEGER NOT NULL,
				y INTEGER NOT NULL
			);
		""");

		statement.execute("""
			CREATE TABLE IF NOT EXISTS artifacts (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				heroId INTEGER NOT NULL,
				type VARCHAR(64) NOT NULL,
				level INTEGER NOT NULL,
				
				FOREIGN KEY(heroId) REFERENCES heroes(id)
			);
		""");
	}

	private static void connectDb() throws SQLException
	{
		if (DB_CONN != null && !DB_CONN.isValid(5))
			DB_CONN = null;
		if (DB_CONN == null)
		{
			DB_CONN = DriverManager.getConnection(DB_URL);
			initTables();
		}
	}

	private static void			loadArtifacts(Hero hero) throws SQLException
	{
		connectDb();
		PreparedStatement statement = DB_CONN.prepareStatement("SELECT * FROM artifacts WHERE heroId = ?;");
		statement.setInt(1, hero.getId());
		
		ResultSet query = statement.executeQuery();
		while (query.next())
		{
			try {
				Class<?> cls = Class.forName("com.swingy.app.Artifacts." + query.getString("type"));
				if (cls == Artifact.class || !Artifact.class.isAssignableFrom(cls))
					throw new ClassNotFoundException(query.getString("type"));
				Artifact artifact = (Artifact) cls.getDeclaredConstructor(int.class).newInstance(query.getInt("level"));
				artifact.setId(query.getInt("id"));
				hero.addArtifact(artifact);
			} catch (ClassNotFoundException
			| InvocationTargetException
			| SecurityException
			| InstantiationException
			| IllegalAccessException
			| IllegalArgumentException
			| NoSuchMethodException e) {
				if (e instanceof ClassNotFoundException)
					System.err.printf("Artifact class '%s' doesnt exist.", query.getString("type"));
				else
					System.err.printf("An error occured while generating Artifact '%s' (%d): %s", query.getString("type"), query.getInt("id"), e.toString());
			}
		}
	}

	public static List<Hero>	loadData() throws SQLException
	{
		ArrayList<Hero>	res;
		connectDb();

		res = new ArrayList<Hero>();
		Statement statement = DB_CONN.createStatement();
		ResultSet query = statement.executeQuery("SELECT * FROM heroes;");
		
		while (query.next())
		{
			try {
				Class<?> cls = Class.forName("com.swingy.app.Mob.Heroes." + query.getString("class"));
				if (cls == Hero.class || !Hero.class.isAssignableFrom(cls))
					throw new ClassNotFoundException(query.getString("class"));

				Hero hero = (Hero) cls.getDeclaredConstructor(String.class).newInstance(query.getString("name"));
				hero.setLevel(query.getInt("level"));
				hero.setXp(query.getLong("xp"));
				hero.getPosition().x = query.getInt("x");
				hero.getPosition().y = query.getInt("y");
				hero.setId(query.getInt("id"));
				
				loadArtifacts(hero);
				res.add(hero);
			} catch (ClassNotFoundException
				| SecurityException
				| InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
				| InvocationTargetException
				| NoSuchMethodException e) {
					if (e instanceof ClassNotFoundException)
						System.err.printf("Hero class '%s' doesnt exist.", query.getString("class"));
					else
						System.err.printf("An error occured while generating Hero '%s' (%d): %s", query.getString("name"), query.getInt("id"), e.toString());
			}
		}

		return res;
	}

	public static void	saveHero(Hero hero) throws SQLException
	{
		connectDb();
		if (hero.getId() == null)
		{
			PreparedStatement createStmt = DB_CONN.prepareStatement("""
				INSERT INTO heroes
					(name, class, level, xp, x, y)
					VALUES
					(?, ?, ?, ?, ?, ?);
				""");
			createStmt.setString(1, hero.getName());
			createStmt.setString(2, hero.getClass().getSimpleName());
			createStmt.setInt(3, hero.getLevel());
			createStmt.setLong(4, hero.getXp());
			createStmt.setInt(5, hero.getPosition().x);
			createStmt.setInt(6, hero.getPosition().y);
			createStmt.execute();

			Statement getIdStmt = DB_CONN.createStatement();
			ResultSet res = getIdStmt.executeQuery("SELECT last_insert_rowid() AS row_id;");
			res.next(); // todo add test for -1 result
			int id = res.getInt("row_id");
			hero.setId(id);
		}
		else
		{
			PreparedStatement updateStmt = DB_CONN.prepareStatement("""
				UPDATE heroes
					SET
						level = ?, xp = ?, x = ?, y = ?
					WHERE
						id = ?
			""");
			updateStmt.setInt(1, hero.getLevel());
			updateStmt.setLong(2, hero.getXp());
			updateStmt.setInt(3, hero.getPosition().x);
			updateStmt.setInt(4, hero.getPosition().y);
			updateStmt.setInt(5, hero.getId());
			updateStmt.execute();
		}
	}

	public static void removeHero(Hero hero) throws SQLException
	{
		connectDb();
		PreparedStatement rmStmt = DB_CONN.prepareStatement("""
			DELETE FROM heroes
				WHERE id = ?;
		""");
		rmStmt.setInt(1, hero.getId());
		rmStmt.execute();

		rmStmt = DB_CONN.prepareStatement("""
			DELETE FROM artifacts
				WHERE heroId = ?;
		""");
		rmStmt.setInt(1, hero.getId());
		rmStmt.execute();
	}

	public static void	addArtifact(Hero hero, Artifact artifact) throws SQLException
	{
		connectDb();
		PreparedStatement addStmt = DB_CONN.prepareStatement("""
			INSERT INTO artifacts
				(heroId, type, level)
				VALUES
				(?, ?, ?)
			""");
		addStmt.setInt(1, hero.getId());
		addStmt.setString(2, artifact.getClass().getSimpleName());
		addStmt.setInt(3, artifact.getLevel());
		addStmt.execute();
	}

	public static void	removeArtifact(Hero hero, Artifact artifact) throws SQLException
	{
		connectDb();
		// todo
	}
}
