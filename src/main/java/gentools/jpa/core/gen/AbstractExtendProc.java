package gentools.jpa.core.gen;

import gentools.jpa.core.config.JpaEntityGenProperties.Entity;
import gentools.jpa.core.config.JpaEntityGenProperties.ExtendEntity;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

public abstract class AbstractExtendProc {

	protected Entity entityProp;
	private ExtendEntity extendInfo;
	protected boolean canAddExtendForEntity(DbTable table) {
		
		if(entityProp == null) return false;
		extendInfo = entityProp.getExtendinfo();
		if(extendInfo == null) return false;
		String tableName = table.getTableName();
		for(String regex :  extendInfo.getMatchs() ) {
			if( tableName.matches(regex) ) return true;
		}
		return false;
	}
	
	protected boolean canAddThisColumn(DbTable table, DbColumn c) {
		if( canAddExtendForEntity(table) ) {
			for( String regex : extendInfo.getColumns() ) {
				if(c.getColumnName().matches(regex)) return false;
			}
		}
		return true;
	}

	protected String extendClassName() {
		String className = extendInfo.getExtendclass();
		int pos = className.lastIndexOf(".");
		return className.substring(pos + 1);
	}
}
