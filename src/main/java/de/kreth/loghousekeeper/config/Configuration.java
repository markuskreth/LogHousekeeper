package de.kreth.loghousekeeper.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import de.kreth.loghousekeeper.Job;

public class Configuration {
	
	private final List<Job> jobs = new ArrayList<Job>();
	
	public Collection<Job> getJobs() {
		return jobs;
	}

	public static Configuration from(Document xmlConfig) {
		Element root = xmlConfig.getRootElement();
		List<Element> list = root.elements();
		Configuration config = new Configuration();
		
		for(Element e: list) {
			config.jobs.add(Job.parse(e));
		}
		
		return config;
	}
}
