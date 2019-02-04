package gentools.jpa.core.gen;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class FieldBody {
	DbColumn column;
	boolean hasColumnAnno = false;
	boolean idColumn = false;
	String fieldName;
	String fieldType;
	boolean isEnum = false;
	boolean autoInc = false;
	boolean multiKey = false;
	public FieldBody(DbTable table) {
		if( !table.isMultiPk() ) throw new RuntimeException("is Not Multi Key Table");
		multiKey = true;
		fieldName = "id";
		fieldType = table.getClassName() + "PK";
		idColumn = true;
	}
	public FieldBody(DbColumn column) {
		this.column = column;
		idColumn = column.isPkColumn();
		fieldName = HandlerUtil.columnToFieldName(column.getColumnName());
		hasColumnAnno = !HandlerUtil.isSameColumnName(fieldName, column.getColumnName());
		fieldType = HandlerUtil.getFullClassNameToClassName( column.getJavaClassName() );
		autoInc = "true".equals(column.getIsAutoIncrement() );
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if(idColumn) {
			if(multiKey) {
				sb.append("\t").append("@EmbeddedId").append("\n");
			}else {
				sb.append("\t").append("@Id").append("\n");
			}
		}
		if(hasColumnAnno) {
			sb.append("\t").append("@Column(name=\"").append(column.getColumnName()).append("\")").append("\n");
		}
		if(autoInc) {
			sb.append("\t").append("@GeneratedValue(strategy = GenerationType.IDENTITY)").append("\n");
		}
		sb.append("\t").append("private ").append(fieldType).append(" ").append(fieldName).append(";").append("\n");
		sb.append("\n");
		return sb.toString();
	}
	
	public String toStringMethod() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("public ").append(fieldType).append(" ")
		.append(HandlerUtil.buildAccessorName("get", fieldName)).append("() {").append("\n");
		sb.append("\t\t").append("return this.").append(fieldName).append(";").append("\n");
		sb.append("\t").append("}").append("\n\n");
		sb.append("\t").append("public ").append("void").append(" ")
		.append(HandlerUtil.buildAccessorName("set", fieldName)).append("(")
		.append(fieldType).append(" ").append(fieldName).append(") {").append("\n");
		sb.append("\t\t").append("this.").append(fieldName)
		.append(" = ").append(fieldName).append(";").append("\n");
		sb.append("\t").append("}").append("\n");
		return sb.toString();
	}
	
}
