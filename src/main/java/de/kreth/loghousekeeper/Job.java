package de.kreth.loghousekeeper;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.dom4j.Element;

public class Job {
	private final File path;
	private final String filePattern;
	private final int maxSize;
	private final int maxAgeInDays;
	
	public File getPath() {
		return path;
	}

	public String getFilePattern() {
		return filePattern;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getMaxAgeInDays() {
		return maxAgeInDays;
	}
	
	public Job(File path, String filePattern, int maxSize, int maxAgeInDays) {
		super();
		this.path = path;
		this.filePattern = filePattern;
		this.maxSize = maxSize;
		this.maxAgeInDays = maxAgeInDays;
	}
	
	public Collection<File> listFiles() {
		return FileUtils.listFiles(path, new WildcardFileFilter(filePattern), TrueFileFilter.INSTANCE);
	}

	public static Job parse(Element e) {
		String locPath = e.element("path").getText();
		String filePattern = e.element("filePattern").getText();
		String maxSize = e.element("maxSize").getText();
		String maxAgeInDays = e.element("maxAgeInDays").getText();
		return new Job(new File(locPath), filePattern, Integer.parseInt(maxSize), Integer.parseInt(maxAgeInDays));
	}
	
	public void appendTo(Element root) {
		Element el = root.addElement(getClass().getSimpleName());
		el.addElement("path").setText(path.getPath());
		el.addElement("filePattern").setText(filePattern);
		el.addElement("maxSize").setText(Integer.toString(maxSize));
		el.addElement("maxAgeInDays").setText(Integer.toString(maxAgeInDays));
	}
}
