package net.sf.jsqlparser.test.select;

import static java.lang.String.format;
import static net.sf.jsqlparser.util.EscapingUtils.unescapeDoubleQuotes;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class DoubleQuoteEscapingTest {

	private final CCJSqlParserManager parserManager = new CCJSqlParserManager();

	@DataPoints
	public static final String[] STRINGS = new String[] { "bob", "\"Name\"",
			"\"A Name with \"\" in it\"",
			"\"A Name with a lot of \"\"\"\"\"\"\"\"\"\"\"\" in it\"",
			"\"A Name with '\"" };

	@Theory
	public void tableNamesBehaveWhenTheyContainEscapedDoubelQuotes(
			String tableName) throws JSQLParserException {
		String sql = format("SELECT * FROM %s BOB WHERE BOB.v = 'Bla'",
				tableName);
		Statement parsed = parserManager.parse(new StringReader(sql));
		assertThat(parsed, instanceOf(Select.class));
		SelectBody selectBody = ((Select) parsed).getSelectBody();
		assertThat(selectBody, instanceOf(PlainSelect.class));
		PlainSelect plainSelect = (PlainSelect) selectBody;
		FromItem fromItem = plainSelect.getFromItem();
		assertThat(fromItem, instanceOf(Table.class));
		Table table = (Table) fromItem;
		assertThat(table.getName(), equalTo(unescapeDoubleQuotes(tableName)));
	}

	@Theory
	public void columnNamesBehaveWhenTheyContainEscapedDoubelQuotes(
			String columnName) throws JSQLParserException {
		String sql = format("SELECT * FROM BOB B WHERE %s = 'Bla'", columnName);
		Statement parsed = parserManager.parse(new StringReader(sql));
		assertThat(parsed, instanceOf(Select.class));
		SelectBody selectBody = ((Select) parsed).getSelectBody();
		assertThat(selectBody, instanceOf(PlainSelect.class));
		PlainSelect plainSelect = (PlainSelect) selectBody;
		Expression where = plainSelect.getWhere();
		assertThat(where, instanceOf(EqualsTo.class));
		EqualsTo equalsTo = (EqualsTo) where;
		Expression leftExpression = equalsTo.getLeftExpression();
		assertThat(leftExpression, instanceOf(Column.class));
		Column column = (Column) leftExpression;
		assertThat(column.getColumnName(),
				equalTo(unescapeDoubleQuotes(columnName)));
	}
}
