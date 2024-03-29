package gentools.jpa.core.info;

import java.util.List;

public class DbTable {

	private String schemaName;
	private String tableName;
	private String className;
	private String remarks;
	private boolean multiPk = false;
	private List<DbColumn> columns;
	private boolean persistable = false;
	private boolean addschema = false;
	
	public boolean isMultiPk() {
		return multiPk;
	}

	public void setMultiPk(boolean multiPk) {
		this.multiPk = multiPk;
	}



	public boolean isAddschema() {
		return addschema;
	}

	public void setAddschema(boolean addschema) {
		this.addschema = addschema;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
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
	
	
	
	public boolean isPersistable() {
		return persistable;
	}

	public void setPersistable(boolean persistable) {
		this.persistable = persistable;
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
