package gentools.jpa.core;

import java.util.List;

import gentools.jpa.core.config.JpaEntityGenProperties;

public class HandlerUtil {
	
	public static boolean tableSelector(String tableName, JpaEntityGenProperties prop) {
		String checkName = prop.getSelector().isLowercase() ? tableName.toLowerCase(): tableName;
		List<String> include = prop.getSelector().getInclude();
		List<String> exculde = prop.getSelector().getExclude();
		boolean ret = false;
		if(include != null && include.size() > 0) {
			for(String reg : include) {
				ret = checkName.matches(reg);
				if(ret) break;
			}
		}else {
			ret = true;
		}
		if(ret) {
			boolean inexclude = false;
			for(String regx : exculde) {
				inexclude = checkName.matches(regx);
				if(inexclude) break;
			}
			ret = !inexclude;
		}
		return ret;
		
	}
	public static String capitailizeWord(String str) {
		return capitailizeWord(str, false);
	}
	
	
	public static String columnToFieldName(String column) {
		String fieldName = column.toLowerCase().replaceAll("_", " ");
		return capitailizeWord(fieldName, true);
	}
	
	public static boolean isSameColumnName(String field, String column) {
		return field.toLowerCase().equals(column.toLowerCase());
	}
	
	public static String getFullClassNameToClassName(String className) {
		int length = className.length();
		int pos = className.lastIndexOf(".");
		return className.substring(pos + 1, length);
	}
    public static String capitailizeWord(String str, boolean skipFirst) { 
        StringBuffer s = new StringBuffer(); 
        char ch = skipFirst ? '-' : ' '; 
        for (int i = 0; i < str.length(); i++) { 
              
            // If previous character is space and current 
            // character is not space then it shows that 
            // current letter is the starting of the word 
            if (ch == ' ' && str.charAt(i) != ' ') 
                s.append(Character.toUpperCase(str.charAt(i))); 
            else
                s.append(str.charAt(i)); 
            ch = str.charAt(i); 
        } 
  
        // Return the string with trimming 
        return s.toString().trim(); 
    } 
	
	public static String buildAccessorName(String prefix, String suffix) {
		if (suffix.length() == 0) return prefix;
		if (prefix.length() == 0) return suffix;
		
		char first = suffix.charAt(0);
		if (Character.isLowerCase(first)) {
			boolean useUpperCase = suffix.length() > 2 &&
				(Character.isTitleCase(suffix.charAt(1)) || Character.isUpperCase(suffix.charAt(1)));
			suffix = String.format("%s%s",
					useUpperCase ? Character.toUpperCase(first) : Character.toTitleCase(first),
					suffix.subSequence(1, suffix.length()));
		}
		return String.format("%s%s", prefix, suffix);
	}
}
