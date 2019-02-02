package gentools.liquibase;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import liquibase.change.Change;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.parser.core.xml.XMLChangeLogSAXParser;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.serializer.core.xml.XMLChangeLogSerializer;

@SpringBootApplication
public class LiquibaseXmlReset implements CommandLineRunner {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	Environment env;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(LiquibaseXmlReset.class);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.error(env.getProperty("liquibase.target"));
		logger.error(env.getProperty("liquibase.target"));
		List<ChangeSet> list = readXml();
		ChangeSet root = null;
		for (ChangeSet c : list) {
			if (root == null) {
				root = new ChangeSet(c.getId(), c.getAuthor(), false, false, null, null, null, null, null);
			}
			for (Change cc : c.getChanges()) {
				if(cc instanceof AddPrimaryKeyChange) {
					AddPrimaryKeyChange addPrimary = (AddPrimaryKeyChange) cc;
					addPrimary.setConstraintName("pk_" + addPrimary.getTableName());
					
				}
				root.addChange(cc);
			}
		}
		List<ChangeSet> outlist = new ArrayList<>();
		outlist.add(root);
		XMLChangeLogSerializer xmlSerial = new XMLChangeLogSerializer();

		FileOutputStream stream = new FileOutputStream(env.getProperty("liquibase.output"));
		PrintStream out = new PrintStream(stream, true, "UTF-8");
		xmlSerial.write(outlist, out);
		out.flush();
		stream.close();
	}

	private List<ChangeSet> readXml() throws Exception {
		FileSystemResourceAccessor fsOpener = new FileSystemResourceAccessor();
		CompositeResourceAccessor fileOpener = new CompositeResourceAccessor(fsOpener);

		XMLChangeLogSAXParser xmlParser = new XMLChangeLogSAXParser();
		DatabaseChangeLog datlog = xmlParser.parse(env.getProperty("liquibase.target"), null, fileOpener);

		return datlog.getChangeSets();
	}
}
