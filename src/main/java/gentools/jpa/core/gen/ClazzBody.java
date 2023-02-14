package gentools.jpa.core.gen;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.GeneratorInfo;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public class ClazzBody extends AbstractExtendProc{
	
	private String className;
	private DbTable tableInfo;
	private ClazzImport clazzImport;
	private List<FieldBody> fieldList;
	private String keyClassName = null;
	private boolean hasKeyClass = false;
	
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
		hasKeyClass = !t.isMultiPk();
		FieldBody keyFiled = null;
		fieldList = new ArrayList<FieldBody>();
		
		for(DbColumn c : t.getColumns()) {
			if(c.isPkColumn() && !hasKeyClass) keyFiled = new FieldBody(c, myConvertInfos, myGenerator); 
			if ( (hasKeyClass || !c.isPkColumn()) && canAddThisColumn(tableInfo, c)){
				fieldList.add( new FieldBody(c, myConvertInfos, myGenerator) );
			}
		}
				
		
		if(!hasKeyClass) {
			keyFiled = new FieldBody(t);
			fieldList.add(0, keyFiled  );
		}
		if(keyFiled != null) keyClassName = keyFiled.getFieldType(); 
		
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
			sb.append("@Table(name=\"").append(tableInfo.getTableName()).append("\"");
			if(tableInfo.isAddschema()) {
				sb.append(", schema=\"").append(tableInfo.getSchemaName()).append("\"");
				sb.append(", catalog=\"").append(tableInfo.getSchemaName()).append("\"");
			}
			sb.append(")").append(System.lineSeparator());
			
		}
		sb.append("public class ").append(className);
		// Extend 처리 
		if(canAddExtendForEntity(tableInfo)) {
			sb.append(" extends ").append(extendClassName());
		}
		sb.append(" implements Serializable");
		if(! entityProp.isSuperclass() && canAddExtendPersistable(tableInfo)) {
			sb.append(", Persistable<").append(keyClassName).append(">");
		}
		sb.append(" {").append(System.lineSeparator());
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
