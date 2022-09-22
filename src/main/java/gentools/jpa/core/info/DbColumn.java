package gentools.jpa.core.info;

public class DbColumn {

	private String columnName;
	private String typeName;
	private Integer columnSize;
	private String remarks;
	private String isNullAble;
	private String isAutoIncrement;
	private String javaClassName;
	private boolean pkColumn = false;
	private boolean isPrimitive = false;
	
	public boolean isPkColumn() {
		return pkColumn;
	}
	public void setPkColumn(boolean pkColumn) {
		this.pkColumn = pkColumn;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Integer getColumnSize() {
		return columnSize;
	}
	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getIsNullAble() {
		return isNullAble;
	}
	public void setIsNullAble(String isNullAble) {
		this.isNullAble = isNullAble;
	}
	public String getIsAutoIncrement() {
		return isAutoIncrement;
	}
	public void setIsAutoIncrement(String isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public String getJavaClassName() {
		return javaClassName;
	}
	public void setJavaClassName(String sqlDataType) {
		this.javaClassName = sqlDataType;
	}
	
	public boolean isPrimitive() {
		return isPrimitive;
	}
	public void setPrimitive(boolean isPrimitive) {
		this.isPrimitive = isPrimitive;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("columnName=").append(columnName).append(",");
		sb.append("typeName=").append(typeName).append(",");
		sb.append("columnSize=").append(columnSize).append(",");
		sb.append("remarks=").append(remarks).append(",");
		sb.append("isNullAble=").append(isNullAble).append(",");
		sb.append("isAutoIncrement=").append(isAutoIncrement).append(",");
		sb.append("javaClassName=").append(javaClassName).append(",");
		sb.append("pkColumn=").append(pkColumn).append(",");
		return sb.toString();
	}
	
}
