package gentools.jpa.core.gen;

import gentools.jpa.core.info.DbTable;

public class ClazzBody {
	private String className;
	private DbTable tableInfo;
	public ClazzBody(DbTable t) {
		className = t.getClassName();
		tableInfo = t;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("@Entity").append("\n");
		sb.append("@Table(name = \"").append(tableInfo.getTableName()).append("\")").append("\n");
		sb.append("public class ").append(className).append(" implements Serializable {").append("\n");
		return sb.toString();
	}

}
