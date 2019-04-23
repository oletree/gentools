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
		
		private boolean hasSuper;
		private String prefix;
		private String writepath;
		private String entPackage;
		private String keyPackage;
		private String superPackage;
		
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
		public String getEntPackage() {
			return entPackage;
		}
		public void setEntPackage(String entPackage) {
			this.entPackage = entPackage;
		}
		public String getKeyPackage() {
			return keyPackage;
		}
		public void setKeyPackage(String keyPackage) {
			this.keyPackage = keyPackage;
		}
		public boolean isHasSuper() {
			return hasSuper;
		}
		public void setHasSuper(boolean hasSuper) {
			this.hasSuper = hasSuper;
		}
		public String getSuperPackage() {
			return superPackage;
		}
		public void setSuperPackage(String superPackage) {
			this.superPackage = superPackage;
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
		
	}
}
