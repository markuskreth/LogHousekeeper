package de.kreth.loghousekeeper;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.config.Configuration;

public class Loghousekeeper {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	public static void main(String[] args) throws DocumentException, IOException {
		SAXReader saxBuilder = new SAXReader();
		Document xmlConfig = saxBuilder.read(new File(args[0]));
		Configuration conf = Configuration.from(xmlConfig);
		Loghousekeeper keeper = new Loghousekeeper();
		if(keeper.logger.isInfoEnabled()) {
			keeper.logger.info("Running " + keeper.getClass().getSimpleName() 
					+ " with " + conf.getJobs().size() + " jobs");
		}
		for (Job j: conf.getJobs()) {
			keeper.delete(j);
		}
	}

	public void delete(Job job) throws IOException {
		Collection<File> files = job.listFiles();
		Calendar timeLimit = new GregorianCalendar();
		timeLimit.add(Calendar.DAY_OF_MONTH, job.getMaxAgeInDays() * -1);
		Date max = timeLimit.getTime();
		
		for(File f : files) {
			if (FileUtils.isFileOlder(f, max)) {
				if(logger.isInfoEnabled()) {
					logger.info("Deleting " + f.getAbsolutePath());
				}
				FileUtils.forceDelete(f);
			}
		}
	}

	
}
