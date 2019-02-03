package gentools.jpa.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.gen.ClazzImport;
import gentools.jpa.core.info.DbTable;

@Component
public class GenEntityClass {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JpaEntityGenProperties prop;
	
	public void write(List<DbTable> list) {
		for(DbTable t : list) {
			ClazzImport cimport = new ClazzImport(t, prop.getEntity().getPakage());
			logger.info(cimport.toString());
			
			logger.info(t.toString());
		}

	}
	
	

}
