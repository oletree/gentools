package gentools.jpa.core;

import java.util.List;

import org.h2.util.StringUtils;

import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertColumn;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertInfo;
import gentools.jpa.core.config.JpaEntityGenProperties.Entity;
import gentools.jpa.core.config.JpaEntityGenProperties.GeneratorInfo;

public class HandlerUtil {
	
	public static GeneratorInfo getTableGenerator(String tableName, Entity prop) {
		if(prop.getGenerators() != null) {
			for(GeneratorInfo c: prop.getGenerators()) {
				if(tableName.equalsIgnoreCase(c.getTablename())) return c;
			}
		}
		return null;
	}
	
	public static ConvertInfo getTableConverts(String tableName, Entity prop) {
		
		if( prop.getConvertinfos() != null) {
			for(ConvertInfo c : prop.getConvertinfos()) {
				if(tableName.equalsIgnoreCase(c.getTablename()) ) return c;
			}
		}
		return null;
	}
	
	public static String getColumnConverts(String columnName, ConvertInfo myConvertInfo) {
		if(myConvertInfo != null) {
			for(ConvertColumn ce : myConvertInfo.getColumns()) {
				if( columnName.equalsIgnoreCase(ce.getColumnname()) ) return ce.getConvertclass(); 
			}
		}
		return null;
	}
	
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
		String newFieldName = capitailizeWord(fieldName, true).replaceAll(" ", "");
		String firstLetter = newFieldName.substring(0, 1);
		if( StringUtils.isNumber(firstLetter) ) {
			return "_" + newFieldName.substring(1, newFieldName.length());
		}
		
		return newFieldName;
	}
	
	public static boolean isSameColumnName(String field, String column) {
		return field.toLowerCase().equals(column.toLowerCase());
	}
	
	public static boolean isJavaKeyString(String field) {
		if("package".equals(field)) {
			return true;
		}
		return false;
	}
	
	public static boolean isAddColumnNameAnnonString(String field) {
		String lowfield = field.toLowerCase();
		if("column".equals(lowfield)) {
			return true;
		}
		if("value".equals(lowfield)) {
			return true;
		}
		if("desc".equals(lowfield)) {
			return true;
		}
		return false;
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
	
	public static void addClassComment(StringBuilder sb, String... comments) {
		addComment(sb, "", comments);
	}
	public static void addMemberComment(StringBuilder sb, String... comments) {
		addComment(sb, "\t", comments);
	}
	public static void addComment(StringBuilder sb,String prefix,  String... comments) {
		sb.append(prefix).append("/**").append(System.lineSeparator());
		for(String value : comments) {			
			sb.append(prefix).append(String.format(" * %s", value)).append(System.lineSeparator());
		}
		sb.append(prefix).append(" */").append(System.lineSeparator());
	}
	public static boolean isLobColumn(String typeName) {
		String lowtypeName = typeName.toLowerCase();
		if("text".equals(lowtypeName)) return true;
		if("longtext".equals(lowtypeName)) return true;
		return false;
	}


}
