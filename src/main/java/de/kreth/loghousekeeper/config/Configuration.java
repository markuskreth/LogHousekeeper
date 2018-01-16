package de.kreth.loghousekeeper.config;

import java.util.*;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.jobtypes.AbstractJob;

public class Configuration {
	private final static Logger logger = LoggerFactory.getLogger(Configuration.class);
	
	private final List<AbstractJob> jobs = new ArrayList<>();

   private Properties mailProperties;
	
	public Collection<AbstractJob> getJobs() {
		return jobs;
	}

	public static Configuration from(Document xmlConfig) {
		Element root = xmlConfig.getRootElement();
		List<Element> list = root.elements();
		Configuration config = new Configuration();

		config.mailProperties = new Properties();
		
		for(Element e: list) {
		   if(e.getName().equals("mail_properties")) {
		      parseEntriesToProps(e, config.mailProperties);
		   } else {
		      config.jobs.add(AbstractJob.parse(e));
		   }
		}
		
		if(logger.isInfoEnabled()) {
		   logger.info("Found " + config.jobs.size() + " jobs in configuration file");
		}
		return config;
	}
	
	private static void parseEntriesToProps(Element e, Properties mailProperties2) {
	   for (Element entry: e.elements("entry")) {
	      
            String key = entry.attribute("key").getText();
            if(key.contains("password")) {
               mailProperties2.put(key, entry.getText());
            } else {
               mailProperties2.put(key, entry.getText());
            }
         
	   }
   }

   public Properties getMailProperties() {
	    return mailProperties;
	}
}
