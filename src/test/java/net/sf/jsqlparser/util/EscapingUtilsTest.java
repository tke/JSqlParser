package net.sf.jsqlparser.util;

import static com.google.common.base.Preconditions.checkNotNull;
import static net.sf.jsqlparser.util.EscapingUtils.unescape;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class EscapingUtilsTest {
	private static class EscapingResult {
		private final String path;
		private final String result;

		public EscapingResult(String string, String result) {
			checkNotNull(string, "The string cannot be null");
			checkNotNull(result, "The result cannot be null");
			this.path = string;
			this.result = result;
		}

		public String getString() {
			return path;
		}

		public String getResult() {
			return result;
		}

	}

	private static EscapingResult escapingResult(String string, String result) {
		checkNotNull(string, "The string cannot be null");
		checkNotNull(result, "The result cannot be null");
		return new EscapingResult(string, result);
	}

	@DataPoints
	public static final EscapingResult[] ESCAPING_RESULTS = new EscapingResult[] {
			escapingResult("Table", "Table"),
			escapingResult("Table \"", "Table \""),
			escapingResult("Table name with \"\" in it",
					"Table name with \" in it"),
			escapingResult("Table name with more than one \"\"\"\" in it",
					"Table name with more than one \"\" in it") };

	@Theory
	public void pathsAreCorectlyEscaped(EscapingResult escapingResult) {
		String unescaped = EscapingUtils.unescape(escapingResult.getString(),
				"\"");
		assertThat(unescaped, equalTo(escapingResult.getResult()));
	}

	@Test(expected = NullPointerException.class)
	public void nullString() {
		unescape(null, "\"");
	}

	@Test(expected = NullPointerException.class)
	public void nullEscapeSequence() {
		unescape("BOB", null);
	}

	@Test
	public void emptyEscapingSequence() {
		assertThat(unescape("BOB", ""), equalTo("BOB"));
	}
}
