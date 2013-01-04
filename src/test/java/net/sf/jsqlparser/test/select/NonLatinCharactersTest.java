package net.sf.jsqlparser.test.select;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
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
public class NonLatinCharactersTest {

	private final CCJSqlParserManager parserManager = new CCJSqlParserManager();

	@DataPoints
	public static final String[] STRINGS = new String[] { "p", "aColumnName",
			"α", "β", "Γ", "γ", "δ", "ε", "ζ", "η", "Θ", "θ", "ι", "â", "æ",
			"â", "à", "ç", "ÿ", "û", "œ", "ä", "âæâàçÿûœä", "ぱ", "ぱぱhgjgj",
			" ÀÂœ" };

	@Theory
	public void columnNamesBehaveWhenQuoted(String columnName)
			throws JSQLParserException {
		assumeTrue(!columnName.startsWith("\""));
		assumeTrue(!columnName.endsWith("\""));
		String sql = format("SELECT * FROM BOB WHERE \"%s\" = 'Bla'",
				columnName);
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
				equalTo(format("\"%s\"", columnName)));
	}

	@Theory
	public void valuesBehave(String values) throws JSQLParserException {
		String sql = format("SELECT * FROM BOB WHERE p LIKE '%s'", values);
		Statement parsed = parserManager.parse(new StringReader(sql));
		assertThat(parsed, instanceOf(Select.class));
		SelectBody selectBody = ((Select) parsed).getSelectBody();
		assertThat(selectBody, instanceOf(PlainSelect.class));
		PlainSelect plainSelect = (PlainSelect) selectBody;
		Expression where = plainSelect.getWhere();
		assertThat(where, instanceOf(LikeExpression.class));
		LikeExpression likeExpression = (LikeExpression) where;
		Expression rightExpression = likeExpression.getRightExpression();
		assertThat(rightExpression, instanceOf(StringValue.class));
		StringValue stringValue = (StringValue) rightExpression;
		assertThat(stringValue.getValue(), equalTo(format("%s", values)));
	}

	@Theory
	public void tableNamesBehaveWhenQuoted(String tableName)
			throws JSQLParserException {
		assumeTrue(!tableName.startsWith("\""));
		assumeTrue(!tableName.endsWith("\""));
		String sql = format("SELECT * FROM \"%s\" BOB WHERE BOB.v = 'Bla'",
				tableName);
		Statement parsed = parserManager.parse(new StringReader(sql));
		assertThat(parsed, instanceOf(Select.class));
		SelectBody selectBody = ((Select) parsed).getSelectBody();
		assertThat(selectBody, instanceOf(PlainSelect.class));
		PlainSelect plainSelect = (PlainSelect) selectBody;
		FromItem fromItem = plainSelect.getFromItem();
		assertThat(fromItem, instanceOf(Table.class));
		Table table = (Table) fromItem;
		assertThat(table.getName(), equalTo(format("\"%s\"", tableName)));
	}
}
