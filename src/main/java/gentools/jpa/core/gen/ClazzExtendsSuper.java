package gentools.jpa.core.gen;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.Entity;
import gentools.jpa.core.info.DbTable;

public class ClazzExtendsSuper {
	private String className;
	private DbTable tableInfo;
	private String entpackage;
	private Entity entityProp;
	
	public ClazzExtendsSuper(DbTable t,JpaEntityGenProperties prop) {
		className = t.getClassName();
		tableInfo = t;
		entityProp = prop.getEntity();
		entpackage = prop.getEntity().getEntpackage();
		
	}
	
	public String getClassName() {
		return className;
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();
		//Super Class Import
		sb.append("package ").append(entpackage).append(";").append(System.lineSeparator());
		sb.append("import ").append(entityProp.getBasepackage()).append(".*;").append(System.lineSeparator()).append(System.lineSeparator());
		for (String s : ClazzImport.defaultImport) {
			sb.append("import ").append(s).append(";").append(System.lineSeparator());
		}
		HandlerUtil.addClassComment(sb, tableInfo.getRemarks());
		sb.append("@Entity").append(System.lineSeparator());
		sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\")").append(System.lineSeparator());
		sb.append("public class ").append(className).append(" extends ").append(entityProp.getPrefix()).append(className);
		sb.append(" implements Serializable {").append(System.lineSeparator());
		sb.append("\tprivate static final long serialVersionUID = 1L;").append(System.lineSeparator()).append(System.lineSeparator());
		sb.append("}");
		return sb.toString();
	}

}
