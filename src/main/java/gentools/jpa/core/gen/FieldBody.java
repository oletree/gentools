package gentools.jpa.core.gen;


import org.springframework.util.StringUtils;

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
		autoInc = "yes".equals(column.getIsAutoIncrement().toLowerCase() );
		isEnum = DefaultClassMap.getEnumJavaClass(column.getColumnName());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		if(idColumn) {
			if(multiKey) {
				sb.append("\t").append("@EmbeddedId").append("\n");
			}else {
				sb.append("\t").append("@Id").append("\n");
			}
		}
		addColumnAnnotation(sb);
		
		if(autoInc) {
			sb.append("\t").append("@GeneratedValue(strategy = GenerationType.IDENTITY)").append("\n");
		}
		if(isEnum) {
			sb.append("\t").append("@Enumerated(EnumType.STRING)").append("\n");
		}
		sb.append("\t").append("private ").append(fieldType).append(" ").append(fieldName).append(";").append("\n");
		sb.append("\n");
		return sb.toString();
	}
	
	private void addColumnAnnotation(StringBuilder sb) {
		boolean addLength = false;
		boolean addColumnDefinition= false;
		boolean addUnique = false;
		boolean addNullable = false;
		if(multiKey) return;
		int length = column.getColumnSize();
		if(column.getJavaClassName().equals("java.lang.String")) {
			addLength = true;
		}
		if("char".equals(column.getTypeName().toLowerCase()) ) {
			addColumnDefinition = true;
		}
		if("no".equals(column.getIsNullAble().toLowerCase())) {
			addNullable = true;
		}
		if(column.isPkColumn()) {
			addUnique = true;
		}
		boolean hasfirst = false;
		if(hasColumnAnno ||addLength || addColumnDefinition || addUnique || addNullable) {
			sb.append("\t@Column(");
			if(hasColumnAnno) {
				sb.append("name=\"").append(column.getColumnName()).append("\"");
				hasfirst = true;
			}
			if(addUnique) {
				if(hasfirst)sb.append(", ");
				sb.append("unique=true");
				hasfirst = true;
			}
			if(addNullable) {
				if(hasfirst)sb.append(", ");
				sb.append("nullable=false");
				hasfirst = true;
			}
			if(addLength) {
				if(hasfirst)sb.append(", ");
				sb.append("length=").append(Integer.toString(length));
				hasfirst = true;
				
			}
			if(addColumnDefinition) {
				if(hasfirst)sb.append(", ");
				sb.append("columnDefinition=\"char(").append(Integer.toString(length)).append(")\"");
				hasfirst = true;				
			}
			sb.append(")").append("\n");
		}

		
	}
	public String toStringMethod() {
		StringBuilder sb = new StringBuilder();
		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		sb.append("\t").append("public ").append(fieldType).append(" ")
		.append(HandlerUtil.buildAccessorName("get", fieldName)).append("() {").append("\n");
		sb.append("\t\t").append("return this.").append(fieldName).append(";").append("\n");
		sb.append("\t").append("}").append("\n\n");
		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		sb.append("\t").append("public ").append("void").append(" ")
		.append(HandlerUtil.buildAccessorName("set", fieldName)).append("(")
		.append(fieldType).append(" ").append(fieldName).append(") {").append("\n");
		sb.append("\t\t").append("this.").append(fieldName)
		.append(" = ").append(fieldName).append(";").append("\n");
		sb.append("\t").append("}").append("\n\n");
		return sb.toString();
	}
	
}
