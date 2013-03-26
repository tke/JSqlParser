package net.sf.jsqlparser.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

/**
 * Utility class whose methods deal with escaping
 * 
 * @author therealluigi
 * 
 */
public class EscapingUtils {
	private EscapingUtils() {
	}

	/**
	 * Removes escaping of the input escapedSequence from the input string. The
	 * escaping is assumed to be a double occurrence of the input escapedString
	 * within the input string. For example if the input string is abba"" and
	 * the input escapedString is ", then the output will be abba".
	 * 
	 * @param string
	 *            The string that may contain escaped sequences. cannot be
	 *            {@code null}.
	 * @param escapeSequence
	 *            The escaped sequence to be detected.
	 * @return A un-escaped version of the input string. If no escaping occurred
	 *         in the original input, then (a copy of) the input string will be
	 *         returned.
	 * @throws NullPointerException
	 *             if either input is {@code null}.
	 */
	public static String unescape(String string, String escapeSequence) {
		checkNotNull(string, "The string cannot be null");
		checkNotNull(escapeSequence, "The escaped string cannot be null");
		return string.replaceAll(
				format("%s%s", escapeSequence, escapeSequence), escapeSequence);
	}

	/**
	 * Removes escaping of double quotes from the input string. The escaping is
	 * assumed to be a double occurrence of the character " in the input string.
	 * For example if the input string is abba"" then the output will be abba".
	 * 
	 * @param string
	 *            The string that may contain escaped sequences. cannot be
	 *            {@code null}.
	 * @return A un-escaped version of the input string. If no escaping occurred
	 *         in the original input, then (a copy of) the input string will be
	 *         returned.
	 * @throws NullPointerException
	 *             if the input is {@code null}.
	 */
	public static String unescapeDoubleQuotes(String string) {
		checkNotNull(string, "The string cannot be null");
		return unescape(string, "\"");
	}

}