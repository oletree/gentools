package gentools.jpa.core.gen;

import java.util.List;
import java.util.stream.Collectors;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.info.DbTable;

public class ClazzExtendsSuper {
	private String className;
	private DbTable tableInfo;
	private List<FieldBody> fieldList;
	
	public ClazzExtendsSuper(DbTable t,String pkg) {
		className = t.getClassName();
		tableInfo = t;

		boolean addAll = !t.isMultiPk();
		fieldList = t.getColumns().stream()
				.filter(c->{ return addAll || !c.isPkColumn() ;})
				.map(c->{
					return new FieldBody(c);
				})
				.collect(Collectors.toList());
		
		if(!addAll) {
			FieldBody id = new FieldBody(t);
			fieldList.add(0, id );
		}
		
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		//Super Class Import
		//sb.append(clazzImport.toString()).append("\n");
		HandlerUtil.addClassComment(sb, tableInfo.getRemarks());
		sb.append("@Entity").append("\n");
		sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\")").append("\n");
		sb.append("public class ").append(className).append(" implements Serializable {").append("\n");
		sb.append("\tprivate static final long serialVersionUID = 1L;").append("\n\n");
		for(FieldBody f : fieldList) {
			sb.append(f.toString());
		}
		sb.append("\t").append("public ").append(className).append("()").append(" {").append("\n").append("\t").append("}").append("\n\n");
		for(FieldBody f : fieldList) {
			sb.append(f.toStringMethod());
		}
		sb.append("}");
		return sb.toString();
	}

}
