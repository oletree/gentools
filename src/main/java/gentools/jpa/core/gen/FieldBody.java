package gentools.jpa.core.gen;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class FieldBody {
	DbColumn column;
	boolean hasColumnAnno;
	String fieldName;
	String fieldType;
	boolean multiKey = false;
	public FieldBody(DbTable table) {
		if( !table.isMultiPk() ) throw new RuntimeException("is Not Multi Key Table");
		multiKey = true;
		fieldName = "id";
		fieldType = table.getClassName() + "PK";
		
	}
	public FieldBody(DbColumn column) {
		this.column = column;
		fieldName = HandlerUtil.columnToFieldName(column.getColumnName());
		hasColumnAnno = !HandlerUtil.isSameColumnName(fieldName, column.getColumnName());
		fieldType = HandlerUtil.getFullClassNameToClassName( column.getJavaClassName() );
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(hasColumnAnno) {
			sb.append("\t").append("@Column(name");
		}
		sb.append("\t").append("private ");
		return sb.toString();
	}
	
	
}
