package com.swingy.app.Renderer;

import java.util.ArrayList;

import javax.swing.Popup;

import com.swingy.app.Map;

public abstract class Renderer {
	public enum Menu {
		MAIN,
		CREATE_CHAR,
		CHOOSE_CHAR,
		GAME,
		OPTIONS
	};


	protected Menu				_menu;
	protected Map				_map;
	protected ArrayList<String>	_popups;


	public Renderer()
	{
		_map = null;
		_menu = Menu.MAIN;
		_popups = new ArrayList<>();
	}
	public Renderer(Renderer renderer) {
		_map = renderer._map;
		_menu = renderer._menu;
		_popups = renderer._popups;
	}
	
	protected abstract void	renderMain();
	// protected abstract void	renderCreateChar();
	// protected abstract void	renderCharSelection();
	protected abstract void	renderMap();
	// protected abstract void	renderOptions();

	public abstract Input	getInputAction();
	
	public void	render() {
		switch (_menu) {
			case MAIN:
				renderMain();
				break;
			// case CREATE_CHAR:
			// 	renderCreateChar();
			// 	break;
			// case CHOOSE_CHAR:
			// 	renderCharSelection();
			// 	break;
			case GAME:
				renderMap();
				break;
			// case OPTIONS:
			// 	renderOptions();
			// 	break;
		
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
}
