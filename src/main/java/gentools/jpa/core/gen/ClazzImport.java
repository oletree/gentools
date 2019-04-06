package gentools.jpa.core.gen;

import java.util.TreeSet;

import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class ClazzImport {

	private String[] defaultImport = {"java.io.Serializable","javax.persistence.*"};
	private String pakcageOut;
	private TreeSet<String> importOut = new TreeSet<>();
	
	public ClazzImport(DbTable table, String pkg){
		this(table, pkg, false);
		
	}
	
	public ClazzImport(DbTable table, String pkg, boolean onlyPk){
		pakcageOut = pkg;
		
		for( DbColumn c : table.getColumns() ) {			
			if(DefaultClassMap.addImport(c.getJavaClassName())  && ! importOut.contains(c.getJavaClassName())) {
				if(onlyPk) {
					if(c.isPkColumn()) importOut.add(c.getJavaClassName());
				}else {
					if( !table.isMultiPk() || !c.isPkColumn()) {
						importOut.add(c.getJavaClassName());						
					}
				}
			}
		}
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(pakcageOut).append(";").append("\n");
		for(String s : defaultImport) {			
			sb.append("import ").append(s).append(";").append("\n");
		}
		sb.append("\n");
		for(String s : importOut) {
			sb.append("import ").append(s).append(";").append("\n");
		}
		return sb.toString();
	}
	
}
