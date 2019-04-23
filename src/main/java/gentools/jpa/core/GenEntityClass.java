package gentools.jpa.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.gen.ClazzBody;
import gentools.jpa.core.gen.PkClazzBody;
import gentools.jpa.core.info.DbTable;

@Component
public class GenEntityClass {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JpaEntityGenProperties prop;
	
	public void write(List<DbTable> list) throws Exception {
		
		File path = new File(prop.getEntity().getWritepath());
		if(path.exists() && path.isDirectory()) {
			for(DbTable t : list) {
				
				PkClazzBody pk = null;
				if(t.isMultiPk()) {
					pk = new PkClazzBody(t, prop.getEntity().getKeyPackage());
					File file = new File(path, pk.getClassName() + ".java");
					PrintStream out = new PrintStream(new FileOutputStream(file), true, "UTF-8");
					out.print(pk.toString());
					out.flush();
					out.close();
				}
				File file = new File(path, t.getClassName() + ".java");
				PrintStream out = new PrintStream( new FileOutputStream(file), true, "UTF-8");
				ClazzBody clazBody = new ClazzBody(t, prop.getEntity().getEntPackage(), pk);
				out.print(clazBody.toString());
				out.flush();
				out.close();
			}
		}else {
			logger.error("is not Exist or not Directory {}", path.getAbsolutePath() );
		}
	}
	
	

}
