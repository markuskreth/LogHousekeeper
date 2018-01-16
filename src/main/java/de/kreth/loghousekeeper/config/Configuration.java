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
	
	public Collection<AbstractJob> getJobs() {
		return jobs;
	}

	public static Configuration from(Document xmlConfig) {
		Element root = xmlConfig.getRootElement();
		List<Element> list = root.elements();
		Configuration config = new Configuration();
		
		for(Element e: list) {		   
			config.jobs.add(AbstractJob.parse(e));
		}
		if(logger.isInfoEnabled()) {
		   logger.info("Found " + config.jobs.size() + " jobs in configuration file");
		}
		return config;
	}
	
	public Properties getMailProperties() {
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.web.de");
       props.put("mail.smtp.port", "587");
       
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    
	    return props;
	}
}
