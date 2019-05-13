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
		sb.append("package ").append(entpackage).append(";").append("\n");
		sb.append("import ").append(entityProp.getBasepackage()).append(".*;").append("\n").append("\n");
		for (String s : ClazzImport.defaultImport) {
			sb.append("import ").append(s).append(";").append("\n");
		}
		HandlerUtil.addClassComment(sb, tableInfo.getRemarks());
		sb.append("@Entity").append("\n");
		sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\")").append("\n");
		sb.append("public class ").append(className).append(" extends ").append(entityProp.getPrefix()).append(className);
		sb.append(" implements Serializable {").append("\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;").append("\n\n");
		sb.append("}");
		return sb.toString();
	}

}
