package com.swingy.app.Renderer;

import java.util.ArrayList;

import javax.swing.Popup;

import com.swingy.app.Map;
import com.swingy.app.Renderer.Page.CreateChar;
import com.swingy.app.Renderer.Page.Main;
import com.swingy.app.Renderer.Page.Page;

public abstract class Renderer {
	public enum Menu {
		MAIN,
		CREATE_CHAR,
		CHOOSE_CHAR,
		GAME,
		OPTIONS
	};


	protected Menu				_menu;
	protected Page				_page;
	protected Map				_map;
	protected ArrayList<String>	_popups;


	public Renderer()
	{
		_map = null;
		_menu = Menu.MAIN;
		_page = new Main();
		_popups = new ArrayList<>();
	}
	public Renderer(Renderer renderer) {
		_map = renderer._map;
		_menu = renderer._menu;
		_page = renderer._page;
		_popups = renderer._popups;
	}
	
	protected abstract void	renderPage(Page page);
	protected abstract void	renderMap();
	protected abstract void pageChanged();

	public abstract Input	getInputAction();
	
	public void	render() {
		switch (_menu) {
			case MAIN:
			case CREATE_CHAR:
				renderPage(_page);
				break;
			case GAME:
				renderMap();
				break;
			default:
				throw new RuntimeException("Unknown menu");
		}
	};

	public void	addPopup(String popup) {
		_popups.add(popup);
	}

	public Menu	getMenu() {
		return (_menu);
	}

	public Page	getPage() {
		return (_page);
	}

	public void	setMenu(Menu menu) {
		_menu = menu;
		switch (menu) {
			case MAIN:
				_page = new Main();
				break;

			case CREATE_CHAR:
				_page = new CreateChar();
				break;
		
			default:
				_page = null;
		}
		pageChanged();
	}
}
