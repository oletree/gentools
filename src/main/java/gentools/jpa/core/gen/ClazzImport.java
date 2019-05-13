package gentools.jpa.core.gen;

import java.util.TreeSet;

import gentools.jpa.core.config.JpaEntityGenProperties.Entity;
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
	
	public ClazzImport(DbTable table, String pkg, boolean onlyPk) {
		pakcageOut = pkg;
		pkClass = onlyPk;
		tableInfo = table;
		init();
	}

	private void init() {
		
		for( DbColumn c : tableInfo.getColumns() ) {
			if(canAddThisColumn(tableInfo, c)) {
				if(DefaultClassMap.addImport(c.getJavaClassName())  && ! importOut.contains(c.getJavaClassName())) {
					if(pkClass) {
						if(c.isPkColumn()) importOut.add(c.getJavaClassName());
					}else {
						if( !tableInfo.isMultiPk() || !c.isPkColumn()) {
							importOut.add(c.getJavaClassName());						
						}
					}
				}
			}
		}
		if(canAddExtendForEntity(tableInfo)) {
			importOut.add(entityProp.getExtendinfo().getExtendclass());
		}
	}


	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("package ").append(pakcageOut).append(";").append("\n");
		for (String s : defaultImport) {
			sb.append("import ").append(s).append(";").append("\n");
		}
		//key용 package와 entity용 package 분리에 따른 처리
		if(!pkClass && StringUtils.isNotEmpty( entityProp.getKeypackage()) && tableInfo.isMultiPk()) {
			if(!entityProp.getBasepackage().equals(entityProp.getKeypackage())) {
				sb.append("import ").append(entityProp.getKeypackage()).append(".*").append(";").append("\n");
			}
		}
		sb.append("\n");
		for (String s : importOut) {
			sb.append("import ").append(s).append(";").append("\n");
		}
		return sb.toString();
	}

}
