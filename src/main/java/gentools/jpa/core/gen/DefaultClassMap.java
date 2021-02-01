package gentools.jpa.core.gen;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.util.StringUtils;

import gentools.jpa.core.config.JpaEntityGenProperties.JavaTypeChange;

public class DefaultClassMap {

	private static final String ANY_PREFIX = "{prefix}";
	private static TreeMap<String, String> defaultMap = new TreeMap<>();
	private static TreeSet<String> noImportSet = new TreeSet<>();
	private static TreeMap<String, String> customMap = new TreeMap<>();
	private static TreeMap<String, JavaTypeChange> enumMap = new TreeMap<>();
	
	static {
		defaultMap.put("java.sql.Timestamp", "java.util.Date");
		defaultMap.put("java.sql.Time", "java.util.Date");
		
		noImportSet.add("java.lang.Long");
	}
	
	public static JavaTypeChange getEnumJavaClass(String columnName, String dbType) {
		Set<Entry<String, JavaTypeChange>> keyValues = enumMap.entrySet();
		String lowColumnName = columnName.toLowerCase();
		String lowDbType = dbType.toLowerCase();
		for(Entry<String, JavaTypeChange> keyval : keyValues) {
			String key = keyval.getKey();
			JavaTypeChange cd = keyval.getValue();
			if(key.startsWith(ANY_PREFIX)) {
				String suffix = key.substring(ANY_PREFIX.length()).toLowerCase();
				if(lowColumnName.endsWith(suffix)) {
					if(StringUtils.isEmpty(cd.getDbtype()) ) {
					return cd;
					}else if(lowDbType.equals(cd.getDbtype())) {
						return cd;
					}
				}
			}else if( key.equals(lowColumnName)){ 
				return cd;
				
			}
		}
		return null;
	}
	public static String getColumnJavaCalss(String clazzName, String dbType, String columnName) {
		Set<Entry<String, JavaTypeChange>> keyValues = enumMap.entrySet();
		String lowColumnName = columnName.toLowerCase();
		String lowDbType = dbType.toLowerCase();
		for(Entry<String, JavaTypeChange> keyval : keyValues) {
			String key = keyval.getKey();
			JavaTypeChange cd = keyval.getValue();
			if(key.startsWith(ANY_PREFIX)) {
				String suffix = key.substring(ANY_PREFIX.length()).toLowerCase();
				if(lowColumnName.endsWith(suffix) ) {
					if(StringUtils.isEmpty(cd.getDbtype()) ) {
						return cd.getAfter();
					}else if(lowDbType.equals(cd.getDbtype()) ){
						return cd.getAfter();
					}
				}
			}else if( key.equals(lowColumnName)){ 
				return cd.getAfter();
				
			}
		}
		if(customMap.containsKey(lowDbType)) {
			return customMap.get(lowDbType);
		}
		return clazzName;
	}
	
	public static String getJavaClass(String clazzName) {
		if(defaultMap.containsKey(clazzName)) {
			return defaultMap.get(clazzName);
		}
		return clazzName;
	}
	
	public static boolean addImport(final String className) {
		if( className.startsWith("java.lang.") ) {
			String afterName = className.substring(9, className.length());
			if(afterName.indexOf(".") > 0) return true;
			return false;
		}
		return true;
	}
	
	public static void addCustMap(String dbType, String javaType) {
		
		customMap.put(dbType.toLowerCase(), javaType)	;
	}
	
	public static void addEnumMap(String columnName, JavaTypeChange enumClazz) {
		enumMap.put(columnName.toLowerCase(), enumClazz);
	}
}
