package gentools.jpa.core.gen;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.JavaTypeChange;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class FieldBody {
	DbColumn column;
	boolean hasColumnAnno = false;
	boolean idColumn = false;
	String fieldName;
	String fieldType;
	boolean isEnum = false;
	boolean isStringEnum = true;
	boolean autoInc = false;
	boolean multiKey = false;
	boolean isLob = false;
	boolean isJavaKeyString = false; //java 예약어 여부
	boolean hasConverter = false;
	List<String> convertList;
	public FieldBody(DbTable table) {
		if( !table.isMultiPk() ) throw new RuntimeException("is Not Multi Key Table");
		multiKey = true;
		fieldName = "id";
		fieldType = table.getClassName() + "PK";
		idColumn = true;
	}
	public FieldBody(DbColumn column, ConvertInfo myConvertInfo) {
		this.column = column;
		idColumn = column.isPkColumn();
		fieldName = HandlerUtil.columnToFieldName(column.getColumnName());
		isJavaKeyString = HandlerUtil.isJavaKeyString(fieldName);
		hasColumnAnno = !HandlerUtil.isSameColumnName(fieldName, column.getColumnName());
		hasColumnAnno = hasColumnAnno ?  hasColumnAnno : HandlerUtil.isAddColumnNameAnnonString(column.getColumnName());
		if(isJavaKeyString) hasColumnAnno = true;
		fieldType = HandlerUtil.getFullClassNameToClassName( column.getJavaClassName() );
		autoInc = "yes".equals(column.getIsAutoIncrement().toLowerCase() );
		JavaTypeChange enType = DefaultClassMap.getEnumJavaClass(column.getColumnName(), column.getTypeName());
		isEnum = enType != null;
		isStringEnum = enType == null ?false : enType.isString();
		isLob = HandlerUtil.isLobColumn(column.getTypeName());
		
		if(myConvertInfo != null) {
			String columnName = column.getColumnName();
			convertList = myConvertInfo.getColumns().stream().filter( f -> columnName.equalsIgnoreCase(f.getColumnname()) )
			.map(m -> m.getConvertclass() ).collect(Collectors.toList());
		}
		
		if( convertList!= null && ! convertList.isEmpty() ) hasConverter = true;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();

		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		if(idColumn) {
			if(multiKey) {
				sb.append("\t").append("@EmbeddedId").append(System.lineSeparator());
			}else {
				sb.append("\t").append("@Id").append(System.lineSeparator());
			}
		}
		if(isLob) {
			sb.append("\t").append("@Lob").append(System.lineSeparator());
		}
		addColumnAnnotation(sb);
		
		if(autoInc) {
			sb.append("\t").append("@GeneratedValue(strategy = GenerationType.IDENTITY)").append(System.lineSeparator());
		}
		if(isEnum) {
			if(isStringEnum) {
				sb.append("\t").append("@Enumerated(EnumType.STRING)").append(System.lineSeparator());
			}else {
				sb.append("\t").append("@Enumerated(EnumType.ORDINAL)").append(System.lineSeparator());
			}
		}
		if(hasConverter) {
			for(String convClass : convertList) {
				sb.append("\t").append("@Convert(converter = ")
				.append(HandlerUtil.getFullClassNameToClassName(convClass)).append(".class)").append(System.lineSeparator());
			}
		}
		sb.append("\t").append("private ").append(fieldType).append(" ");
		if(isJavaKeyString) {
			sb.append("_");
		}
		sb.append(fieldName).append(";").append(System.lineSeparator());
		sb.append(System.lineSeparator());
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
				sb.append("name=\"");
				//별칭 처리를 위해 [ 와 ]를 넣음
				if( HandlerUtil.isAddColumnNameAnnonString(column.getColumnName())) sb.append("`");
				sb.append(column.getColumnName());
				if( HandlerUtil.isAddColumnNameAnnonString(column.getColumnName())) sb.append("`");
				sb.append("\"");
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
			sb.append(")").append(System.lineSeparator());
		}

		
	}
	public String toStringMethod() {
		StringBuilder sb = new StringBuilder();
		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		sb.append("\t").append("public ").append(fieldType).append(" ")
		.append(HandlerUtil.buildAccessorName("get", fieldName)).append("() {").append(System.lineSeparator());
		sb.append("\t\t").append("return this.");
		if(isJavaKeyString) sb.append("_");
		sb.append(fieldName).append(";").append(System.lineSeparator());
		sb.append("\t").append("}").append(System.lineSeparator()).append(System.lineSeparator());
		if(column != null && !StringUtils.isEmpty(column.getRemarks()) ) HandlerUtil.addMemberComment(sb, column.getRemarks());
		sb.append("\t").append("public ").append("void").append(" ")
		.append(HandlerUtil.buildAccessorName("set", fieldName)).append("(")
		.append(fieldType).append(" ");
		if(isJavaKeyString) sb.append("_");
		sb.append(fieldName).append(") {").append(System.lineSeparator());
		sb.append("\t\t").append("this.");
		if(isJavaKeyString) sb.append("_");
		sb.append(fieldName).append(" = ");
		if(isJavaKeyString) sb.append("_");
		sb.append(fieldName).append(";").append(System.lineSeparator());
		sb.append("\t").append("}").append(System.lineSeparator()).append(System.lineSeparator());
		return sb.toString();
	}
	
	
	
}
