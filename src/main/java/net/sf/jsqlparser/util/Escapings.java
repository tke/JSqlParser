package net.sf.jsqlparser.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

import com.google.common.base.Function;

public class Escapings {

	private final static class Escaping implements Function<String, String> {

		private final String escapeString;
		private final Function<String, String> escapingStrategy;

		public Escaping(String escapeString, Function<String, String> escapingStrategy) {
			this.escapeString = checkNotNull(escapeString, "The string to escape cannot be null");
			this.escapingStrategy = checkNotNull(escapingStrategy, "The escaping strategy cannot be null");
		}

		@Override
		public String apply(String input) {

			return input == null ? input : input.replaceAll(this.escapeString,
					this.escapingStrategy.apply(escapeString));
		}
	}

	private final static class DoubleItEscapingStrategy implements Function<String, String> {

		@Override
		public String apply(String input) {
			return input == null ? input : format("%s%1$s", input);
		}

	}

	private Escapings() {
		// Utility classes should not be instantiated
	}

	public static final DoubleItEscapingStrategy DOUBLE_IT_ESCAPING_STRATEGY = new DoubleItEscapingStrategy();
	public static final Function<String, String> ESCAPE_DOUBLE_QUOTES = new Function<String, String>() {

		@Override
		public String apply(String input) {

			return input == null ? null : doEscape(input);
		}

		private String doEscape(String input) {
			String toReturn = input;
			boolean quoted = input.startsWith("\"") && input.endsWith("\"");
			if (quoted) {
				toReturn = toReturn.substring(1, toReturn.length() - 1);
			}
			toReturn = new Escaping("\"", DOUBLE_IT_ESCAPING_STRATEGY).apply(toReturn);
			return quoted ? format("\"%s\"", toReturn) : toReturn;
		}
	};

	public final static Function<String, String> UNESCAPE_DOUBLE_QUOTES = new Function<String, String>() {

		@Override
		public String apply(String input) {
			return input == null ? null : doUnsescape(input);
		}

		private String doUnsescape(String input) {
			String toReturn = input;
			boolean quoted = input.startsWith("\"") && input.endsWith("\"");
			if (quoted) {
				toReturn = toReturn.substring(1, toReturn.length() - 1);
			}
			return toReturn = toReturn.replaceAll("\"\"", "\"");
		}
	};

}
