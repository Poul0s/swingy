package com.swingy.app.Renderer;

import com.swingy.app.Map;

public abstract class Renderer {
	enum Menu {
		MAIN,
		CREATE_CHAR,
		CHOOSE_CHAR,
		GAME,
		OPTIONS
	};
	public enum Input {
		NONE,
		UP,
		RIGHT,
		DOWN,
		LEFT
	};

	protected Menu	_menu;
	protected Map	_map;


	public Renderer(Map a_map)
	{
		_map = a_map;
		_menu = Menu.GAME;
	}
	public Renderer(Renderer renderer) {
		_map = renderer._map;
		_menu = renderer._menu;
	}
	
	// protected abstract void	renderMain();
	// protected abstract void	renderCreateChar();
	// protected abstract void	renderCharSelection();
	protected abstract void	renderMap();
	// protected abstract void	renderOptions();
	public abstract Input	getInputAction(); // maybe return two arguments for input value if goto or enter text
	
	public void	render() {
		switch (_menu) {
			// case MAIN:
			// 	renderMain();
			// 	break;
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
}
