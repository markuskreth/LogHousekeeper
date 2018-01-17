package de.kreth.loghousekeeper;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.config.Configuration;

public class Loghousekeeper {

	private static final Logger logger = LoggerFactory.getLogger(Loghousekeeper.class);
	
	public static void main(String[] args) throws DocumentException {
		SAXReader saxBuilder = new SAXReader();
		Document xmlConfig = saxBuilder.read(new File(args[0]));
		
		Configuration conf = Configuration.from(xmlConfig);
		
		if(logger.isInfoEnabled()) {
			logger.info("Running " + Loghousekeeper.class.getSimpleName() 
					+ " with " + conf.getJobs().size() + " jobs");
		}
		for (Runnable j: conf.getJobs()) {
		   j.run();
		}
	}

}
