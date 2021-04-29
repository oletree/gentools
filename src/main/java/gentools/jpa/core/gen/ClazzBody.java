package gentools.jpa.core.gen;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.GeneratorInfo;
import gentools.jpa.core.info.DbTable;

public class ClazzBody extends AbstractExtendProc{
	
	private String className;
	private DbTable tableInfo;
	private ClazzImport clazzImport;
	private List<FieldBody> fieldList;
	
	public ClazzBody(DbTable t,JpaEntityGenProperties prop) {
		entityProp = prop.getEntity();
		if(StringUtils.isEmpty(entityProp.getPrefix()) ) {
			className = t.getClassName();
		}else {
			className = entityProp.getPrefix() + t.getClassName();
		}
		tableInfo = t;
		String lowTableName = tableInfo.getTableName();
		
		ConvertInfo myConvertInfos = HandlerUtil.getTableConverts(lowTableName, entityProp);
		GeneratorInfo myGenerator = HandlerUtil.getTableGenerator(lowTableName, entityProp); 

		clazzImport = new ClazzImport(t, entityProp);
		boolean hasKeyClass = !t.isMultiPk();
		fieldList = t.getColumns().stream()
				.filter(c->{ return (hasKeyClass || !c.isPkColumn()) && canAddThisColumn(tableInfo, c)  ;})
				.map(c->{
					return new FieldBody(c, myConvertInfos, myGenerator);
				})
				.collect(Collectors.toList());
		
		if(!hasKeyClass) {
			FieldBody id = new FieldBody(t);
			fieldList.add(0, id );
		}
		
	}
	
	public String getClassName() {
		return className;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(clazzImport.toString()).append(System.lineSeparator());
		HandlerUtil.addClassComment(sb, tableInfo.getRemarks());
		if( entityProp.isSuperclass() ) {
			sb.append("@MappedSuperclass").append(System.lineSeparator());
		}else {
			sb.append("@Entity").append(System.lineSeparator());
			sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\")").append(System.lineSeparator());
			
		}
		sb.append("public class ").append(className);
		// Extend 처리 
		if(canAddExtendForEntity(tableInfo)) {
			sb.append(" extends ").append(extendClassName());
		}
		sb.append(" implements Serializable {").append(System.lineSeparator());
		sb.append("\tprivate static final long serialVersionUID = 1L;").append(System.lineSeparator()).append(System.lineSeparator());
		for(FieldBody f : fieldList) {
			sb.append(f.toString());
		}
		sb.append("\t").append("public ").append(className).append("()").append(" {").append(System.lineSeparator()).append("\t").append("}").append(System.lineSeparator()).append(System.lineSeparator());
		for(FieldBody f : fieldList) {
			sb.append(f.toStringMethod());
		}
		sb.append("}");
		return sb.toString();
	}

}
