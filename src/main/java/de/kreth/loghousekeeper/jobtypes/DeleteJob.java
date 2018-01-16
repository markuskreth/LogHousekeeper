package de.kreth.loghousekeeper.jobtypes;

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.dom4j.Element;

import de.kreth.loghousekeeper.reactions.DeleteFile;

public class DeleteJob extends AbstractJob {

   final File path;
	final String filePattern;
	final int maxSize;
	final int maxAgeInDays;
	
	public int getMaxAgeInDays() {
		return maxAgeInDays;
	}
	
	public DeleteJob(File path, String filePattern, int maxSize, int maxAgeInDays) {
		super();
		this.path = path;
		this.filePattern = filePattern;
		this.maxSize = maxSize;
		this.maxAgeInDays = maxAgeInDays;
		add(new DeleteFile(logger));
	}
	
	public Collection<File> listFiles() {
		return FileUtils.listFiles(path, new WildcardFileFilter(filePattern), TrueFileFilter.INSTANCE);
	}

   public static DeleteJob parse(Element e) {
      if(DeleteJob.class.getName().equals(e.getName()) == false) {
         throw new UnsupportedOperationException("Element " + e.getName() + " cannot be implemented by " + DeleteJob.class.getName()); 
      }
      
      String locPath = e.element("path").getText();
      String filePattern = e.element("filePattern").getText();
      String maxSize = e.element("maxSize").getText();
      String maxAgeInDays = e.element("maxAgeInDays").getText();
      return new DeleteJob(new File(locPath), filePattern, Integer.parseInt(maxSize), Integer.parseInt(maxAgeInDays));
   }

   @Override
   public void appendTo(Element root) {
      Element el = root.addElement(getClass().getName());
      el.addElement("path").setText(path.getPath());
      el.addElement("filePattern").setText(filePattern);
      el.addElement("maxSize").setText(Integer.toString(maxSize));
      el.addElement("maxAgeInDays").setText(Integer.toString(maxAgeInDays));
   }

   @Override
   public void run() {
   
      Collection<File> files = listFiles();
      Calendar timeLimit = new GregorianCalendar();
      timeLimit.add(Calendar.DAY_OF_MONTH, getMaxAgeInDays() * -1);
      Date max = timeLimit.getTime();
      
      for(File f : files) {
         if (FileUtils.isFileOlder(f, max)) {
            react(f);
         }
      }
   }
}
