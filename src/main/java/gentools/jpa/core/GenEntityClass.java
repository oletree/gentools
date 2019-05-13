package gentools.jpa.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gentools.jpa.core.config.JpaEntityGenProperties;
import gentools.jpa.core.gen.ClazzBody;
import gentools.jpa.core.gen.ClazzExtendsSuper;
import gentools.jpa.core.gen.PkClazzBody;
import gentools.jpa.core.info.DbTable;

@Component
public class GenEntityClass {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	JpaEntityGenProperties prop;
	
	public void write(List<DbTable> list) throws Exception {
		File basePath = Paths.get(prop.getEntity().getWritepath(), prop.getEntity().getBasepackage().replace(".", "/")).toFile(); 
		File keyPath = Paths.get(prop.getEntity().getWritepath(), prop.getEntity().getKeypackage().replace(".", "/")).toFile();
		File entPath = Paths.get(prop.getEntity().getWritepath(), prop.getEntity().getEntpackage().replace(".", "/")).toFile();
		
		if(basePath.exists() && basePath.isDirectory() && keyPath.exists() && keyPath.isDirectory() ) {
			for(DbTable t : list) {
				if(t.isMultiPk()) {
					PkClazzBody pk = new PkClazzBody(t, prop.getEntity().getKeypackage());
					File file = new File(keyPath, pk.getClassName() + ".java");
					PrintStream out = new PrintStream(new FileOutputStream(file), true, "UTF-8");
					out.print(pk.toString());
					out.flush();
					out.close();
				}
				ClazzBody clazBody = new ClazzBody(t, prop);
				File file = new File(basePath, clazBody.getClassName() + ".java");
				PrintStream out = new PrintStream( new FileOutputStream(file), true, "UTF-8");
				out.print(clazBody.toString());
				out.flush();
				out.close();
				// Superclass를 생성 할 경우 entpackage를 사용하여 단순 Entity를 생성 한다.
				if(prop.getEntity().isSuperclass()) {
					ClazzExtendsSuper extendBody = new ClazzExtendsSuper(t, prop);
					File extfile = new File(entPath, extendBody.getClassName() + ".java");
					PrintStream extOut = new PrintStream( new FileOutputStream(extfile), true, "UTF-8");
					extOut.print(extendBody.toString());
					extOut.flush();
					extOut.close();
				}
			}
		}else {
			logger.error("is not Exist or not Directory {} , {}", basePath.getAbsolutePath() , keyPath.getAbsolutePath() );
		}
	}
	
	

}
