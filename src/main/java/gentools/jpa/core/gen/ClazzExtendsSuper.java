package gentools.jpa.core.gen;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.GeneratorInfo;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class ClazzExtendsSuper extends AbstractExtendProc {
	private String className;
	private DbTable tableInfo;
	private String entpackage;
	private String keyClassName;
	private String keyClassImport = null;
	
	public ClazzExtendsSuper(DbTable t,JpaEntityGenProperties prop) {
		className = t.getClassName();
		tableInfo = t;
		entityProp = prop.getEntity();
		entpackage = prop.getEntity().getEntpackage();
		

		String lowTableName = tableInfo.getTableName();
		
		ConvertInfo myConvertInfos = HandlerUtil.getTableConverts(lowTableName, entityProp);
		GeneratorInfo myGenerator = HandlerUtil.getTableGenerator(lowTableName, entityProp); 

		boolean hasKeyClass = t.isMultiPk();
		FieldBody keyFiled = null;

		if(t.isPersistable()) {
			for(DbColumn c : t.getColumns()) {
				if(c.isPkColumn() && hasKeyClass) {
					keyFiled = new FieldBody(c, myConvertInfos, myGenerator);
					keyClassImport = HandlerUtil.getColumnConverts(c.getColumnName(), myConvertInfos);
					if(keyClassImport == null) {
						keyClassImport = c.getJavaClassName();
					}
					
				}
			}
		}
		
		if(hasKeyClass) {
			keyFiled = new FieldBody(t);
			keyClassImport = null;
		}
		if(keyFiled != null) keyClassName = keyFiled.getFieldType(); 

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
		if(canAddExtendPersistable(tableInfo)) {
			if(tableInfo.isMultiPk()) {
				if(!entityProp.getBasepackage().equals(entityProp.getKeypackage())) {
					sb.append("import ").append(entityProp.getKeypackage()).append(".*").append(";").append(System.lineSeparator());
				}
			}else {
				if(keyClassImport != null)
					sb.append("import ").append(keyClassImport).append(";").append(System.lineSeparator());
			}
			sb.append(System.lineSeparator()).append("import org.springframework.data.domain.Persistable;").append(System.lineSeparator());
		}
		
		HandlerUtil.addClassComment(sb, tableInfo.getRemarks());
		sb.append("@Entity").append(System.lineSeparator());
		
		sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\"");
		if(tableInfo.isAddschema()) {
			sb.append(", schema=\"").append(tableInfo.getSchemaName()).append("\"");
			sb.append(", catalog=\"").append(tableInfo.getSchemaName()).append("\"");
		}
		sb.append(")").append(System.lineSeparator());
		
		sb.append("public class ").append(className).append(" extends ").append(entityProp.getPrefix()).append(className);
		
		sb.append(" implements Serializable");
		if( canAddExtendPersistable(tableInfo) ) {
			sb.append(", Persistable<").append(keyClassName).append(">");
		}
		sb.append(" {").append(System.lineSeparator());
		sb.append("\tprivate static final long serialVersionUID = 1L;").append(System.lineSeparator()).append(System.lineSeparator());
		
		if( canAddExtendPersistable(tableInfo) ) {
			sb.append("\t@Transient").append(System.lineSeparator());
			sb.append("\tprivate boolean isNew = true;").append(System.lineSeparator()).append(System.lineSeparator());
			sb.append("\tpublic boolean isNew() {").append(System.lineSeparator());
			sb.append("\t\treturn isNew;").append(System.lineSeparator());
			sb.append("\t}").append(System.lineSeparator()).append(System.lineSeparator());
			
			sb.append("\t@PrePersist").append(System.lineSeparator());
			sb.append("\t@PostLoad").append(System.lineSeparator());
			sb.append("\tpublic void markNotNew() {").append(System.lineSeparator());
			sb.append("\t\tisNew = false;").append(System.lineSeparator());
			sb.append("\t}").append(System.lineSeparator()).append(System.lineSeparator());
		}
		
		sb.append("}");
		return sb.toString();
	}

}
