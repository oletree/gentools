package gentools.jpa.core.gen;

import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DefaultClassMap {

	private static final String ANY_PREFIX = "${prefix}";
	private static TreeMap<String, String> defaultMap = new TreeMap<>();
	private static TreeSet<String> noImportSet = new TreeSet<>();
	private static TreeMap<String, String> customMap = new TreeMap<>();
	private static TreeMap<String, String> enumMap = new TreeMap<>();
	
	static {
		defaultMap.put("java.sql.Timestamp", "java.util.Date");
		defaultMap.put("java.sql.Time", "java.util.Date");
		
		noImportSet.add("java.lang.Long");
	}
	
	public static boolean getEnumJavaClass(String columnName) {
		Set<String> keys = enumMap.keySet();
		String lowColumnName = columnName.toLowerCase();
		for(String key : keys) {
			if(key.startsWith(ANY_PREFIX)) {
				String suffix = key.substring(ANY_PREFIX.length()).toLowerCase();
				if(lowColumnName.endsWith(suffix)) {
					return true;
				}
			}else if( key.equals(lowColumnName)){ 
				return true;
				
			}
		}
		return false;
	}
	public static String getColumnJavaCalss(String clazzName, String dbType, String columnName) {
		Set<Entry<String, String>> keyValues = enumMap.entrySet();
		String lowColumnName = columnName.toLowerCase();
		for(Entry<String, String> keyval : keyValues) {
			String key = keyval.getKey();
			if(key.startsWith(ANY_PREFIX)) {
				String suffix = key.substring(ANY_PREFIX.length()).toLowerCase();
				if(lowColumnName.endsWith(suffix)) {
					return keyval.getValue();
				}
			}else if( key.equals(lowColumnName)){ 
				return keyval.getValue();
				
			}
		}
		if(customMap.containsKey(dbType.toLowerCase())) {
			return customMap.get(dbType.toLowerCase());
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
	
	public static void addEnumMap(String columnName, String enumClazz) {
		enumMap.put(columnName.toLowerCase(), enumClazz);
	}
}
