package com.swingy.app.DataLoader;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.swingy.app.Artifacts.Artifact;
import com.swingy.app.Heroes.Hero;

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
				y INTEGER NOT NULL,
				h INTEGER NOT NULL
			);
			
			CREATE TABLE IF NOT EXISTS artifacts (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				heroId INTEGER NOT NULL,
				type VARCHAR(64) NOT NULL,
				
				FOREIGN KEY(heroId) REFERENCES heroes(id)
			);
		""");
	}

	private static void connectDb() throws SQLException
	{
		if (!DB_CONN.isValid(5))
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
		PreparedStatement statement = DB_CONN.prepareStatement("SELECT * FROM artifacts WHERE id = ?;");
		statement.setInt(0, hero.getId());
		
		ResultSet query = statement.executeQuery();
		while (query.next())
		{
			try {
				Class<?> cls = Class.forName(query.getString("type"));
				if (cls == Artifact.class || !cls.isAssignableFrom(Artifact.class))
					throw new ClassNotFoundException(query.getString("type"));
				Artifact artifact = (Artifact) cls.getDeclaredConstructor().newInstance();
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
				Class<?> cls = Class.forName(query.getString("class"));
				if (cls == Hero.class || !cls.isAssignableFrom(Hero.class))
					throw new ClassNotFoundException(query.getString("class"));

				Hero hero = (Hero) cls.getDeclaredConstructor().newInstance(query.getString("name"));
				hero.setLevel(query.getInt("level"));
				hero.setXp(query.getInt("xp"));
				
				loadArtifacts(hero);
				res.add(hero);
			} catch (ClassNotFoundException
				| InvocationTargetException
				| SecurityException
				| InstantiationException
				| IllegalAccessException
				| IllegalArgumentException
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
	}

	public static void	addArtifact(Hero hero, Artifact artifact) throws SQLException
	{
		connectDb();
	}

	public static void	removeArtifact(Hero hero, Artifact artifact) throws SQLException
	{
		connectDb();
	}
}
