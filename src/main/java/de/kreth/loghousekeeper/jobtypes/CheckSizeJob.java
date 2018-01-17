package de.kreth.loghousekeeper.jobtypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.dom4j.Element;

public class CheckSizeJob extends AbstractJob {

   private List<File> files;
   private long warnSize;
   
   public CheckSizeJob(List<File> files, long warnSize) {
      super();
      this.files = files;
      this.warnSize = warnSize;
   }

   public static CheckSizeJob parse(Element e) {

      if(CheckSizeJob.class.getName().equals(e.getName()) == false) {
         throw new UnsupportedOperationException("Element " + e.getName() + " cannot be implemented by " + CheckSizeJob.class.getName()); 
      }
      long warnSize = Long.parseUnsignedLong(e.element("warnSize").getTextTrim());
      List<File> files = new ArrayList<>();
      for(Element el: e.elements("file")) {
         File f = new File(el.getText());
         files.add(f);
      }
      CheckSizeJob job = new CheckSizeJob(files, warnSize);
      return job;
   }
   
   @Override
   public void run() {
      if(logger.isDebugEnabled()) {
         logger.debug("Checking " + files.size() + " files for size " + warnSize + " bytes: " + files);
      }
      for(File f: files) {
         if(FileUtils.sizeOf(f) > warnSize) {
            react(f, " has size " + FileUtils.sizeOf(f) + " bytes", "allowed are " + warnSize);
         }
      }
   }

   @Override
   protected Element createDetailedElementIn(Element root) {
      Element el = root.addElement(getClass().getName());
      el.addElement("warnSize").setText(Long.toString(warnSize));
      for(File f: files) {
         el.addElement("file").setText(f.getAbsolutePath());
      }
      return el;
   }

}
