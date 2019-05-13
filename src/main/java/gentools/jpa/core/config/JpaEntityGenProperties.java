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
		private boolean superclass = false;
		private String prefix = "";
		private String writepath;
		private String basepackage;
		private String keypackage;
		private ExtendEntity extendinfo;
		
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
		public ExtendEntity getExtendinfo() {
			return extendinfo;
		}
		public void setExtendinfo(ExtendEntity extendinfo) {
			this.extendinfo = extendinfo;
		}

		
	}
	public static class Database {
		private String jdbcDriver;
		private String url;
		private String username;
		private String password;

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
		private List<ConvertData> typechange;
		private List<ConvertData> enumchange;
		public List<ConvertData> getTypechange() {
			return typechange;
		}
		public void setTypechange(List<ConvertData> typechange) {
			this.typechange = typechange;
		}
		public List<ConvertData> getEnumchange() {
			return enumchange;
		}
		public void setEnumchange(List<ConvertData> enumchange) {
			this.enumchange = enumchange;
		}
		
	}
	public static class ConvertData{
		private String before;
		private String after;
		private String dbtype;
		
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
}
