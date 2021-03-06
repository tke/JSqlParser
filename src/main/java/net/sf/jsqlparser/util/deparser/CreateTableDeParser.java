package net.sf.jsqlparser.util.deparser;

import java.util.Iterator;

import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

/**
 * A class to de-parse (that is, tranform from JSqlParser hierarchy into a
 * string) a {@link net.sf.jsqlparser.statement.create.table.CreateTable}
 */
public class CreateTableDeParser {

	protected StringBuilder buffer;

	/**
	 * @param buffer the buffer that will be filled with the select
	 */
	public CreateTableDeParser(StringBuilder buffer) {
		this.buffer = buffer;
	}

	public void deParse(CreateTable createTable) {
		buffer.append("CREATE TABLE ").append(createTable.getTable().getWholeTableName());
		if (createTable.getColumnDefinitions() != null) {
			buffer.append(" (");
			for (Iterator<ColumnDefinition> iter = createTable.getColumnDefinitions().iterator(); iter.hasNext();) {
				ColumnDefinition columnDefinition = (ColumnDefinition) iter.next();
				buffer.append(columnDefinition.getColumnName());
				buffer.append(" ");
				buffer.append(columnDefinition.getColDataType().toString());
				if (columnDefinition.getColumnSpecStrings() != null) {
					for (Iterator<String> iterator = columnDefinition.getColumnSpecStrings().iterator(); iterator.hasNext();) {
						buffer.append(" ");
						buffer.append((String) iterator.next());
					}
				}

				if (iter.hasNext()) {
					buffer.append(" , ");
				}

			}

			if (createTable.getIndexes() != null) {
				for (Iterator<Index> iter = createTable.getIndexes().iterator(); iter.hasNext();) {
					buffer.append(",");
					Index index = (Index) iter.next();
					buffer.append(index.getType()).append(" ").append(index.getName());
					buffer.append("(");
					for (Iterator<String> iterator = index.getColumnsNames().iterator(); iterator.hasNext();) {
						buffer.append((String) iterator.next());
						if (iterator.hasNext()) {
							buffer.append(", ");
						}
					}
					buffer.append(")");

					if (iter.hasNext()) {
						buffer.append(",");
					}
				}
			}

			buffer.append(" )");
		}
	}

	public StringBuilder getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuilder buffer) {
		this.buffer = buffer;
	}
}
