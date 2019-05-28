package gentools.jpa;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import gentools.jpa.core.GenEntityClass;
import gentools.jpa.core.HandlerUtil;
import gentools.jpa.core.config.JpaEntityGenConstants;
import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.config.JpaEntityGenProperties.ConvertData;
import gentools.jpa.core.gen.DefaultClassMap;
import gentools.jpa.core.info.DbColumn;
import gentools.jpa.core.info.DbTable;

@SpringBootApplication
@EnableConfigurationProperties({ JpaEntityGenProperties.class })
public class JpaEntityGen implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Environment env;
	@Autowired
	JpaEntityGenProperties jpaEntityGenProperties;
	@Autowired
	GenEntityClass genEntityClass;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JpaEntityGen.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		for( ConvertData t: jpaEntityGenProperties.getChangetype().getTypechange()) {
			DefaultClassMap.addCustMap(t.getBefore(), t.getAfter());
		}
		for( ConvertData t: jpaEntityGenProperties.getChangetype().getEnumchange()) {
			DefaultClassMap.addEnumMap(t.getBefore(), t);
		}
		Class.forName(jpaEntityGenProperties.getDatabase().getJdbcDriver());
		Connection conn = DriverManager.getConnection(jpaEntityGenProperties.getDatabase().getUrl(),
				jpaEntityGenProperties.getDatabase().getUsername(), jpaEntityGenProperties.getDatabase().getPassword());

		DatabaseMetaData md = conn.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);

		//showColumnData(rs);
		ArrayList<DbTable> list = new ArrayList<>();
		while (rs.next()) {
			String tableName = rs.getString(JpaEntityGenConstants.TABLE_META_TABLE_NAME);
			String remarks = rs.getString(JpaEntityGenConstants.META_REMARKS);
			if (!HandlerUtil.tableSelector(tableName, jpaEntityGenProperties))
				continue;

			DbTable table = new DbTable();
			list.add(table);
			table.setTableName(tableName);
			table.setRemarks(remarks);
			table.setClassName(
					HandlerUtil.capitailizeWord(tableName.toLowerCase().replaceAll("_", " ")).replaceAll(" ", ""));
			TreeSet<String> pk = getPrimaryKeys(md, tableName);
			TreeMap<String, String> colJavaType = getColType(conn, tableName);
			table.setColumns(getTableColums(md, tableName, pk, colJavaType));
			if(pk.size() > 1) table.setMultiPk(true);
		}
		genEntityClass.write(list);
		conn.close();
	}

	private TreeMap<String, String> getColType(Connection conn, String tableName) throws SQLException {
		TreeMap<String, String> treeMap = new TreeMap<>();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery("select * from " + tableName + " " + jpaEntityGenProperties.getDatabase().getLimitQuery());
	      ResultSetMetaData metadata = rs.getMetaData();
	      int colcnt = metadata.getColumnCount();
	      for (int i = 1; i <= colcnt; i++) {
	    	  treeMap.put(metadata.getColumnName(i),
	    			  DefaultClassMap.getJavaClass(metadata.getColumnClassName(i)));
			}
		return treeMap;
	}

	private TreeSet<String> getPrimaryKeys(DatabaseMetaData md, String tableName) throws SQLException {
		ResultSet primaryRs = md.getPrimaryKeys(null, null, tableName);
		TreeSet<String> ret = new TreeSet<>();
		while(primaryRs.next()) {
			ret.add(primaryRs.getString(JpaEntityGenConstants.COLUMN_META_COLUMN_NAME));
		}
		primaryRs.close();
		return ret;
	}

	private List<DbColumn> getTableColums(DatabaseMetaData md, String tableName, TreeSet<String> pk, TreeMap<String, String> colJavaType) throws SQLException {
		List<DbColumn> ret = new ArrayList<>();
		ResultSet rs = md.getColumns(null, null, tableName, null);
		showColumnData(rs);
		while (rs.next()) {
			DbColumn col = new DbColumn();
			ret.add(col);
			col.setColumnName(rs.getString(JpaEntityGenConstants.COLUMN_META_COLUMN_NAME));
			col.setTypeName(rs.getString(JpaEntityGenConstants.COLUMN_META_TYPE_NAME));
			col.setColumnSize(rs.getInt(JpaEntityGenConstants.COLUMN_META_COLUMN_SIZE));
			col.setRemarks(rs.getString(JpaEntityGenConstants.META_REMARKS));
			col.setIsNullAble(rs.getString(JpaEntityGenConstants.COLUMN_META_IS_NULLABLE));
			col.setIsAutoIncrement(rs.getString(JpaEntityGenConstants.COLUMN_META_IS_AUTOINCREMENT));
			String javaClassName = DefaultClassMap.getColumnJavaCalss(
					colJavaType.get(col.getColumnName()), col.getTypeName(), col.getColumnName()
					);
			col.setJavaClassName(javaClassName);
			if( pk.contains(col.getColumnName()) ) {
				col.setPkColumn(true);
			}
		}
		rs.close();
		return ret;
	}

	private void showColumnData(ResultSet rs) throws SQLException {
		int colcnt = rs.getMetaData().getColumnCount();
		for (int i = 1; i <= colcnt; i++) {
			System.out.println(rs.getMetaData().getColumnTypeName(i) + " " + rs.getMetaData().getColumnClassName(i)
					+ " " + rs.getMetaData().getColumnName(i)

			);
		}
	}
}
