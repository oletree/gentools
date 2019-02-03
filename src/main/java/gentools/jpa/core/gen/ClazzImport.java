package gentools.jpa.core.gen;

import java.util.TreeSet;

import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class ClazzImport {

	private String[] defaultImport = {"import javax.persistence.*"};
	private String pakcageOut;
	private TreeSet<String> importOut = new TreeSet<>();
	
	public ClazzImport(DbTable table, String pkg){
		pakcageOut = pkg;
		
		for( DbColumn c : table.getColumns() ) {			
			if(DefaultClassMap.addImport(c.getJavaClassName())  && ! importOut.contains(c.getJavaClassName())) {
				importOut.add(c.getJavaClassName());
			}
		}
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("package ").append(pakcageOut).append(";").append("\n");
		for(String s : defaultImport) {			
			sb.append(s).append(";").append("\n");
		}
		sb.append("\n");
		for(String s : importOut) {
			sb.append(s).append(";").append("\n");
		}
		return sb.toString();
	}
	
}
