package gentools.jpa.core.info;

import java.util.List;

public class DbTable {

	private String tableName;
	private String className;
	private String remarks;
	private boolean multiPk = false;
	private List<DbColumn> columns;
	
	public boolean isMultiPk() {
		return multiPk;
	}

	public void setMultiPk(boolean multiPk) {
		this.multiPk = multiPk;
	}



	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<DbColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DbColumn> columns) {
		this.columns = columns;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("tableName=").append(tableName).append(",");
		sb.append("className=").append(className).append(",");
		sb.append("remarks=").append(remarks).append(",");
		sb.append("multiPk=").append(multiPk).append(",");
		sb.append("columns={").append("\n");
		for(DbColumn c : columns) {
			sb.append(c.toString()).append("\n");
		}
		sb.append("}");

		return sb.toString();
	}
}
