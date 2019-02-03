package gentools.jpa.core.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jpaentity", ignoreUnknownFields = false)
public class JpaEntityGenProperties {

	private final Selector selector = new Selector();
	private final Database database = new Database();
	private final Entity entity = new Entity();

	
	public Selector getSelector() {
		return selector;
	}

	public Database getDatabase() {
		return database;
	}
	
	public Entity getEntity() {
		return entity;
	}


	public static class Entity{
		private String prefix;
		private String writepath;
		private String pakage;
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
		public String getPakage() {
			return pakage;
		}
		public void setPakage(String pakage) {
			this.pakage = pakage;
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
}
