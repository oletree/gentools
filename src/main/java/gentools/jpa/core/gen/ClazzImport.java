package gentools.jpa.core.gen;

import java.util.TreeSet;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.Entity;
import gentools.jpa.core.config.JpaEntityGenProperties.GeneratorInfo;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;
import liquibase.util.StringUtils;

public class ClazzImport extends AbstractExtendProc {
	private boolean pkClass = false;
	protected static String[] defaultImport = { "java.io.Serializable", "javax.persistence.*" };
	private String pakcageOut;
	private TreeSet<String> importOut = new TreeSet<>();
	private DbTable tableInfo;
	public ClazzImport(DbTable table, Entity prop){
		tableInfo = table;
		pakcageOut = prop.getBasepackage();
		pkClass = false;
		entityProp = prop;
		init();
	}
	
	public ClazzImport(DbTable table, String pkg, Entity prop, boolean onlyPk) {
		pakcageOut = pkg;
		pkClass = onlyPk;
		tableInfo = table;
		entityProp = prop;
		init();
	}

	private void addImportOut(String clazzName) {
		if(clazzName == null) return;
		if(DefaultClassMap.addImport(clazzName)  && ! importOut.contains(clazzName)) {
			importOut.add(clazzName);
		}
	}
	private void init() {
		//Column Convert 추가 하기
		String tableName = tableInfo.getTableName();
		ConvertInfo myConvertInfo = HandlerUtil.getTableConverts(tableName, entityProp);
		GeneratorInfo myGenerator = HandlerUtil.getTableGenerator(tableName, entityProp);
		for( DbColumn c : tableInfo.getColumns() ) {
			if(canAddThisColumn(tableInfo, c)) {
				if(pkClass) {
					if( c.isPkColumn() ) {
						addImportOut(c.getJavaClassName());
						String convertClazz = HandlerUtil.getColumnConverts(c.getColumnName(), myConvertInfo);
						addImportOut(convertClazz);
					}
				}else if( !tableInfo.isMultiPk() || !c.isPkColumn()) {
					addImportOut(c.getJavaClassName());
					String convertClazz = HandlerUtil.getColumnConverts(c.getColumnName(), myConvertInfo);
					addImportOut(convertClazz);
				}
			}
		}
		if(canAddExtendForEntity(tableInfo)) {
			importOut.add(entityProp.getExtendinfo().getExtendclass());
		}
		if(myGenerator != null) {
			importOut.add("org.hibernate.annotations.GenericGenerator");
		}
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("package ").append(pakcageOut).append(";").append(System.lineSeparator());
		for (String s : defaultImport) {
			sb.append("import ").append(s).append(";").append(System.lineSeparator());
		}
		//key용 package와 entity용 package 분리에 따른 처리
		if(!pkClass && StringUtils.isNotEmpty( entityProp.getKeypackage()) && tableInfo.isMultiPk()) {
			if(!entityProp.getBasepackage().equals(entityProp.getKeypackage())) {
				sb.append("import ").append(entityProp.getKeypackage()).append(".*").append(";").append(System.lineSeparator());
			}
		}
		sb.append(System.lineSeparator());
		for (String s : importOut) {
			sb.append("import ").append(s).append(";").append(System.lineSeparator());
		}
		return sb.toString();
	}

}
