package gentools.jpa.core.gen;

import java.util.List;
import java.util.stream.Collectors;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.info.DbTable;

public class PkClazzBody {

	String className;
	String tableName;
	private ClazzImport clazzImport;
	private List<FieldBody> fieldList;
	
	public String getClassName() {
		return className;
	}
	public PkClazzBody(DbTable table, String pkg){
		tableName = table.getTableName();
		className = table.getClassName() + "PK";
		clazzImport = new ClazzImport(table, pkg, true);
		fieldList = table.getColumns().stream()
				.filter(c->{ return c.isPkColumn() ;})
				.map(c->{
					FieldBody f =  new FieldBody(c);
					f.idColumn = false;
					return f;
				})
				.collect(Collectors.toList());
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(clazzImport.toString()).append("\n");
		HandlerUtil.addClassComment(sb, "The primary key class for the " + tableName + " database table.");
		sb.append("@Embeddable").append("\n");
		sb.append("public class ").append(className).append(" implements Serializable {").append("\n");
		sb.append("\t").append("//default serial version id, required for serializable classes.").append("\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;").append("\n\n");
		for(FieldBody f : fieldList) {
			sb.append(f.toString());
		}
		sb.append("\t").append("public ").append(className).append("()").append(" {").append("\n").append("\t").append("}").append("\n\n");
		for(FieldBody f : fieldList) {
			sb.append(f.toStringMethod());
		}
		sb.append("\n");
		sb.append("\tpublic boolean equals(Object other) {").append("\n");
		sb.append("\t\tif (this == other) {").append("\n");
		sb.append("\t\t\treturn true;").append("\n");
		sb.append("\t\t}").append("\n");
		sb.append("\t\tif (!(other instanceof ").append(className).append(")) {").append("\n");
		sb.append("\t\t\treturn false;").append("\n");
		sb.append("\t\t}").append("\n");
		sb.append("\t\t").append(className).append(" castOther = (").append(className).append(")other;").append("\n");
		sb.append("\t\t").append("return").append("\n");
		boolean isfirst = true;
		for(FieldBody f : fieldList) {
			
			if(isfirst) {
				sb.append("\t\t\t");
				isfirst = false;
			}else {
				sb.append("\n").append("\t\t\t").append("&& ");
			}
			sb.append("this.").append(f.fieldName).append(".equals(castOther.").append(f.fieldName).append(")");
		}
		sb.append(";").append("\n").append("\t}").append("\n\n");
		
		sb.append("\t").append("public int hashCode() {").append("\n");
		sb.append("\t\t").append("final int prime = 31;").append("\n");
		sb.append("\t\t").append("int hash = 17;").append("\n");
		for(FieldBody f : fieldList) {
			sb.append("\t\t").append("hash = hash * prime + this.").append(f.fieldName).append(".hashCode();").append("\n");
		}
		sb.append("\t\t").append("return hash;").append("\n");
		sb.append("\t").append("}").append("\n");
		sb.append("}");

		return sb.toString();
	}
}
