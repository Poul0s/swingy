package com.swingy.app.Renderer.Element;

import java.util.Map;

public class Element {
	protected float		_x;
	protected float		_y;
	protected String	_text;
	protected int		_fontSize; // useless for console render
	protected int[]		_textColor; // todo need rgb to ansi code for console render OR use default color
	protected int[]		_backgroundColor; // todo need rgb to ansi code for console render OR use default color
	protected int		_minWidth;
	protected int		_minHeight;

	@SuppressWarnings("unchecked")
	static <T> T getParm(Map<String, Object> map, String key, T defaultValue)
	{
		return (map != null && map.containsKey(key) ? (T) map.get(key) : defaultValue);
	}

	public Element(Map<String, Object> parameters)
	{
		_x = getParm(parameters, "x", 0.0f);
		_y = getParm(parameters, "y", 0.0f);
		_text = getParm(parameters, "text", "");
		_fontSize = getParm(parameters, "fontSize", 14);
		_textColor = getParm(parameters, "textColor", new int[] {255, 255, 255});
		_backgroundColor = getParm(parameters, "backgroundColor", null);
		_minWidth = getParm(parameters, "minWidth", 0);
		_minHeight = getParm(parameters, "minHeight", 14);
	}

	public float	getX() {
		return (_x);
	}

	public float	getY() {
		return (_y);
	}

	public String	getText() {
		return (_text);
	}

	public int	getFontSize() {
		return (_fontSize);
	}

	public int[]	getTextColor() {
		return (_textColor);
	}

	public int[]	getBackgroundColor() {
		return (_backgroundColor);
	}

	public int	getMinWidth() {
		return (_minWidth);
	}

	public int	getMinHeight() {
		return (_minHeight);
	}
}
