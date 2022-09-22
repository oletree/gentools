package gentools.jpa.core.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jpaentity", ignoreUnknownFields = false)
public class JpaEntityGenProperties {

	private final Selector selector = new Selector();
	private final Database database = new Database();
	private final Entity entity = new Entity();
	private final ListData changetype = new ListData();
	
	
	public Selector getSelector() {
		return selector;
	}

	public Database getDatabase() {
		return database;
	}
	
	public Entity getEntity() {
		return entity;
	}


	public ListData getChangetype() {
		return changetype;
	}


	public static class Entity{
		private boolean addschema = false;
		private boolean superclass = false;
		private String prefix = "";
		private String writepath;
		private String basepackage;
		private String keypackage;
		private String entpackage;
		private ExtendEntity extendinfo;
		private List<ConvertInfo> convertinfos;
		private List<GeneratorInfo> generators;
		private List<String> persistable;
		
		public boolean isAddschema() {
			return addschema;
		}
		public void setAddschema(boolean addschema) {
			this.addschema = addschema;
		}
		public boolean isSuperclass() {
			return superclass;
		}
		public void setSuperclass(boolean superclass) {
			this.superclass = superclass;
		}
		public String getPrefix() {
			return prefix;
		}
		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}
		public String getWritepath() {
			return writepath;
		}
		public void setWritepath(String writepath) {
			this.writepath = writepath;
		}
		public String getBasepackage() {
			return basepackage;
		}
		public void setBasepackage(String pakage) {
			this.basepackage = pakage;
		}
		public String getKeypackage() {
			return keypackage;
		}
		public void setKeypackage(String keypackage) {
			this.keypackage = keypackage;
		}
		public String getEntpackage() {
			return entpackage;
		}
		public void setEntpackage(String entpackage) {
			this.entpackage = entpackage;
		}
		public ExtendEntity getExtendinfo() {
			return extendinfo;
		}
		public void setExtendinfo(ExtendEntity extendinfo) {
			this.extendinfo = extendinfo;
		}
		public List<ConvertInfo> getConvertinfos() {
			return convertinfos;
		}
		public void setConvertinfos(List<ConvertInfo> convertinfos) {
			this.convertinfos = convertinfos;
		}
		public List<GeneratorInfo> getGenerators() {
			return generators;
		}
		public void setGenerators(List<GeneratorInfo> generators) {
			this.generators = generators;
		}
		public List<String> getPersistable() {
			return persistable;
		}
		public void setPersistable(List<String> persistable) {
			this.persistable = persistable;
		}
		
	}

	public static class Database {
		private String jdbcDriver;
		private String url;
		private String username;
		private String password;
		private String limitQuery = "";

		public String getJdbcDriver() {
			return jdbcDriver;
		}

		public void setJdbcDriver(String jdbcDriver) {
			this.jdbcDriver = jdbcDriver;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getLimitQuery() {
			return limitQuery;
		}

		public void setLimitQuery(String limitQuery) {
			this.limitQuery = limitQuery;
		}

	}
	public static class Selector{
		private List<String> include;
		private List<String> exclude;
		private boolean lowercase;
		public List<String> getInclude() {
			return include;
		}
		public void setInclude(List<String> include) {
			this.include = include;
		}
		public List<String> getExclude() {
			return exclude;
		}
		public void setExclude(List<String> exclude) {
			this.exclude = exclude;
		}
		public boolean isLowercase() {
			return lowercase;
		}
		public void setLowercase(boolean lowercase) {
			this.lowercase = lowercase;
		}
		
	}
	public static class ListData {
		private List<JavaTypeChange> typechange;
		private List<JavaTypeChange> enumchange;
		public List<JavaTypeChange> getTypechange() {
			return typechange;
		}
		public void setTypechange(List<JavaTypeChange> typechange) {
			this.typechange = typechange;
		}
		public List<JavaTypeChange> getEnumchange() {
			return enumchange;
		}
		public void setEnumchange(List<JavaTypeChange> enumchange) {
			this.enumchange = enumchange;
		}
		
	}
	public static class JavaTypeChange{
		private String before;
		private String after;
		private String dbtype;
		private boolean isString = true; // Enum String 
		private boolean isEnum = true;
		private boolean isPrimitive = false;
		
		public String getBefore() {
			return before;
		}
		public void setBefore(String before) {
			this.before = before;
		}
		public String getAfter() {
			return after;
		}
		public void setAfter(String after) {
			this.after = after;
		}
		public String getDbtype() {
			return dbtype;
		}
		public void setDbtype(String dbtype) {
			this.dbtype = dbtype;
		}
		public boolean isString() {
			return isString;
		}
		public void setString(boolean isString) {
			this.isString = isString;
		}
		public boolean isEnum() {
			return isEnum;
		}
		public void setEnum(boolean isEnum) {
			this.isEnum = isEnum;
		}
		public boolean isPrimitive() {
			return isPrimitive;
		}
		public void setPrimitive(boolean isPrimitive) {
			this.isPrimitive = isPrimitive;
		}
		
		
	}
	public static class ExtendEntity {
		
		private String extendclass;
		private List<String> columns;
		private List<String> matchs;
		public String getExtendclass() {
			return extendclass;
		}
		public void setExtendclass(String extendclass) {
			this.extendclass = extendclass;
		}
		public List<String> getColumns() {
			return columns;
		}
		public void setColumns(List<String> columns) {
			this.columns = columns;
		}
		public List<String> getMatchs() {
			return matchs;
		}
		public void setMatchs(List<String> matchs) {
			this.matchs = matchs;
		}
	}
	
	public static class ConvertInfo {
		private String tablename;
		private List<ConvertColumn> columns;
		public String getTablename() {
			return tablename;
		}
		public void setTablename(String tablename) {
			this.tablename = tablename;
		}
		public List<ConvertColumn> getColumns() {
			return columns;
		}
		public void setColumns(List<ConvertColumn> columns) {
			this.columns = columns;
		}
	}
	
	public static class ConvertColumn {
		private String columnname;
		private String convertclass;
		public String getColumnname() {
			return columnname;
		}
		public void setColumnname(String columnname) {
			this.columnname = columnname;
		}
		public String getConvertclass() {
			return convertclass;
		}
		public void setConvertclass(String convertclass) {
			this.convertclass = convertclass;
		}
		
	}
	public static class GeneratorInfo {
		private String tablename;
		private String columnname;
		private String generatorname;
		private String generatorpkg;
		public String getTablename() {
			return tablename;
		}
		public void setTablename(String tablename) {
			this.tablename = tablename;
		}
		public String getColumnname() {
			return columnname;
		}
		public void setColumnname(String columnname) {
			this.columnname = columnname;
		}
		public String getGeneratorname() {
			return generatorname;
		}
		public void setGeneratorname(String generatorname) {
			this.generatorname = generatorname;
		}
		public String getGeneratorpkg() {
			return generatorpkg;
		}
		public void setGeneratorpkg(String generatorpkg) {
			this.generatorpkg = generatorpkg;
		}
		
		
	}
}
