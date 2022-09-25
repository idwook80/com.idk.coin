package com.idk.coin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Usage:
 * <li>String msg = Ansi.Red.and(Ansi.BgYellow).format("Hello %s", name)</li>
 * <li>String msg = Ansi.Blink.colorize("BOOM!")</li>
 * 
 * Or, if you are adverse to that, you can use the constants directly:
 * <li>String msg = new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Green money")</li>
 * Or, even:
 * <li>String msg = Ansi.BLUE + "scientific"</li>
 * 
 * NOTE: Nothing stops you from combining multiple FG colors or BG colors, 
 *       but only the last one will display.
 * 
 * @author dain
 *
 */
public final class ANSI {

	// Color code strings from:
	// http://www.topmudsites.com/forums/mud-coding/413-java-ansi.html
	public static final String	SANE				= "\u001B[0m";

	public static final String	HIGH_INTENSITY		= "\u001B[1m";
	public static final String	LOW_INTENSITY		= "\u001B[2m";

	public static final String	ITALIC				= "\u001B[3m";
	public static final String	UNDERLINE			= "\u001B[4m";
	public static final String	BLINK				= "\u001B[5m";
	public static final String	RAPID_BLINK			= "\u001B[6m";
	public static final String	REVERSE_VIDEO		= "\u001B[7m";
	public static final String	INVISIBLE_TEXT		= "\u001B[8m";

	public static final String	BLACK				= "\u001B[30m";
	public static final String	RED					= "\u001B[31m";
	public static final String	GREEN				= "\u001B[32m";
	public static final String	YELLOW				= "\u001B[33m";
	public static final String	BLUE				= "\u001B[34m";
	public static final String	MAGENTA				= "\u001B[35m";
	public static final String	CYAN				= "\u001B[36m";
	public static final String	WHITE				= "\u001B[37m";

	public static final String	BACKGROUND_BLACK	= "\u001B[40m";
	public static final String	BACKGROUND_RED		= "\u001B[41m";
	public static final String	BACKGROUND_GREEN	= "\u001B[42m";
	public static final String	BACKGROUND_YELLOW	= "\u001B[43m";
	public static final String	BACKGROUND_BLUE		= "\u001B[44m";
	public static final String	BACKGROUND_MAGENTA	= "\u001B[45m";
	public static final String	BACKGROUND_CYAN		= "\u001B[46m";
	public static final String	BACKGROUND_WHITE	= "\u001B[47m";

	public static final ANSI HighIntensity = new ANSI(HIGH_INTENSITY);
	public static final ANSI Bold = HighIntensity;
	public static final ANSI LowIntensity = new ANSI(LOW_INTENSITY);
	public static final ANSI Normal = LowIntensity;
	
	public static final ANSI Italic = new ANSI(ITALIC);
	public static final ANSI Underline = new ANSI(UNDERLINE);
	public static final ANSI Blink = new ANSI(BLINK);
	public static final ANSI RapidBlink = new ANSI(RAPID_BLINK);
	
	public static final ANSI Black = new ANSI(BLACK);
	public static final ANSI Red = new ANSI(RED);
	public static final ANSI Green = new ANSI(GREEN);
	public static final ANSI Yellow = new ANSI(YELLOW);
	public static final ANSI Blue = new ANSI(BLUE);
	public static final ANSI Magenta = new ANSI(MAGENTA);
	public static final ANSI Cyan = new ANSI(CYAN);
	public static final ANSI White = new ANSI(WHITE);
	
	public static final ANSI BgBlack = new ANSI(BACKGROUND_BLACK);
	public static final ANSI BgRed = new ANSI(BACKGROUND_RED);
	public static final ANSI BgGreen = new ANSI(BACKGROUND_GREEN);
	public static final ANSI BgYellow = new ANSI(BACKGROUND_YELLOW);
	public static final ANSI BgBlue = new ANSI(BACKGROUND_BLUE);
	public static final ANSI BgMagenta = new ANSI(BACKGROUND_MAGENTA);
	public static final ANSI BgCyan = new ANSI(BACKGROUND_CYAN);
	public static final ANSI BgWhite = new ANSI(BACKGROUND_WHITE);
	
	final private String[] codes;
	final private String codes_str; 
	public ANSI(String... codes) {
		this.codes = codes;
		String _codes_str = "";
		for (String code : codes) {
			_codes_str += code;
		}
		codes_str = _codes_str;
	}
	
	public ANSI and(ANSI other) {
		List<String> both = new ArrayList<String>();
	    Collections.addAll(both, codes);
	    Collections.addAll(both, other.codes);
		return new ANSI(both.toArray(new String[] {}));
	}

	public String colorize(String original) {
		return codes_str + original + SANE;
	}
	
	public String format(String template, Object... args) {
		return colorize(String.format(template, args));
	}
}