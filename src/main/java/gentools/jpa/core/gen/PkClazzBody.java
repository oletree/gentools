package gentools.jpa.core.gen;

import java.util.List;
import java.util.stream.Collectors;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.info.DbTable;

public class PkClazzBody {

	String className;
	String tableName;
	String pkgName;
	private ClazzImport clazzImport;
	private List<FieldBody> fieldList;
	
	public String getClassName() {
		return className;
	}
	
	public PkClazzBody(DbTable table, JpaEntityGenProperties prop){
		
		pkgName = prop.getEntity().getKeypackage();
		tableName = table.getTableName();
		className = table.getClassName() + "PK";
		clazzImport = new ClazzImport(table, pkgName,prop.getEntity(), true);
		
		ConvertInfo myConvertInfos = HandlerUtil.getTableConverts(tableName, prop.getEntity());
		
		fieldList = table.getColumns().stream()
				.filter(c->{ return c.isPkColumn() ;})
				.map(c->{
					FieldBody f =  new FieldBody(c, myConvertInfos);
					f.idColumn = false;
					return f;
				})
				.collect(Collectors.toList());
	}
	
	public String getImportClass() {
		return pkgName + "." + className;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(clazzImport.toString()).append(System.lineSeparator());
		HandlerUtil.addClassComment(sb, "The primary key class for the " + tableName + " database table.");
		sb.append("@Embeddable").append(System.lineSeparator());
		sb.append("public class ").append(className).append(" implements Serializable {").append(System.lineSeparator());
		sb.append("\t").append("//default serial version id, required for serializable classes.").append(System.lineSeparator());
		sb.append("\tprivate static final long serialVersionUID = 1L;").append(System.lineSeparator()).append(System.lineSeparator());
		for(FieldBody f : fieldList) {
			sb.append(f.toString());
		}
		sb.append("\t").append("public ").append(className).append("()").append(" {").append(System.lineSeparator()).append("\t").append("}").append(System.lineSeparator()).append(System.lineSeparator());
		for(FieldBody f : fieldList) {
			sb.append(f.toStringMethod());
		}
		sb.append(System.lineSeparator());
		sb.append("\tpublic boolean equals(Object other) {").append(System.lineSeparator());
		sb.append("\t\tif (this == other) {").append(System.lineSeparator());
		sb.append("\t\t\treturn true;").append(System.lineSeparator());
		sb.append("\t\t}").append(System.lineSeparator());
		sb.append("\t\tif (!(other instanceof ").append(className).append(")) {").append(System.lineSeparator());
		sb.append("\t\t\treturn false;").append(System.lineSeparator());
		sb.append("\t\t}").append(System.lineSeparator());
		sb.append("\t\t").append(className).append(" castOther = (").append(className).append(")other;").append(System.lineSeparator());
		sb.append("\t\t").append("return").append(System.lineSeparator());
		boolean isfirst = true;
		for(FieldBody f : fieldList) {
			
			if(isfirst) {
				sb.append("\t\t\t");
				isfirst = false;
			}else {
				sb.append(System.lineSeparator()).append("\t\t\t").append("&& ");
			}
			sb.append("this.").append(f.fieldName).append(".equals(castOther.").append(f.fieldName).append(")");
		}
		sb.append(";").append(System.lineSeparator()).append("\t}").append(System.lineSeparator()).append(System.lineSeparator());
		
		sb.append("\t").append("public int hashCode() {").append(System.lineSeparator());
		sb.append("\t\t").append("final int prime = 31;").append(System.lineSeparator());
		sb.append("\t\t").append("int hash = 17;").append(System.lineSeparator());
		for(FieldBody f : fieldList) {
			sb.append("\t\t").append("hash = hash * prime + this.").append(f.fieldName).append(".hashCode();").append(System.lineSeparator());
		}
		sb.append("\t\t").append("return hash;").append(System.lineSeparator());
		sb.append("\t").append("}").append(System.lineSeparator());
		sb.append("}");

		return sb.toString();
	}
}
