package net.sf.jsqlparser.statement.drop;

import java.util.List;

import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.select.PlainSelect;

public class Drop implements Statement {
	private String type;
	private String name;
	private List<String> parameters;

	@Override
	public void accept(StatementVisitor statementVisitor) {
		statementVisitor.visit(this);
	}

	public String getName() {
		return name;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public String getType() {
		return type;
	}

	public void setName(String string) {
		name = string;
	}

	public void setParameters(List<String> list) {
		parameters = list;
	}

	public void setType(String string) {
		type = string;
	}

	@Override
	public String toString() {
		String sql = "DROP " + type + " " + name;

		if (parameters != null && parameters.size() > 0) {
			sql += " " + PlainSelect.getStringList(parameters);
		}

		return sql;
	}
}
