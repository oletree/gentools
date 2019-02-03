package gentools.jpa.core.gen;

import java.util.TreeMap;
import java.util.TreeSet;

public class DefaultClassMap {

	private static TreeMap<String, String> defaultMap = new TreeMap<>();
	private static TreeSet<String> noImportSet = new TreeSet<>();
	
	static {
		defaultMap.put("java.sql.Timestamp", "java.util.Date");
		defaultMap.put("java.sql.Time", "java.util.Date");
		
		noImportSet.add("java.lang.Long");
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
}
