package de.kreth.loghousekeeper.jobtypes;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.reactions.DeleteFile;
import de.kreth.loghousekeeper.reactions.Reaction;

public abstract class AbstractJob implements Runnable, Reaction {

   protected final Logger logger;
   private List<Reaction> reactions;
      
   public AbstractJob() {
      reactions = new ArrayList<>();
      logger = LoggerFactory.getLogger(getClass());
   }
   
   public boolean add(Reaction e) {
      return reactions.add(e);
   }
   
   public List<Reaction> getReactions() {
      return Collections.unmodifiableList(reactions);
   }
   
   @Override
   public final void react(File f, String...strings ) {
      for (Reaction r: reactions) {
         r.react(f, strings);
      }
   }

   public final void appendTo(Element root) {
      Element el = createDetailedElementIn(root);
      Element reactElements = el.addElement("reactions");
      for(Reaction r : reactions) {
         reactElements.addElement("reaction").setText(r.getClass().getName());
      }
   }

   protected abstract Element createDetailedElementIn(Element root);
   
   public static AbstractJob parse(Element e) {
      
      String jobname = e.getName();
      AbstractJob job = null;
      if(jobname.equals(DeleteJob.class.getName())) {
         job = DeleteJob.parse(e);
      }
      if(jobname.equals(CheckSizeJob.class.getName())) {
         job = CheckSizeJob.parse(e);
      }
      
      if(job != null) {
         for(Element react: e.element("reactions").elements("reaction")) {
            if(DeleteFile.class.getName().equals(react.getText())) {
               job.reactions.add(new DeleteFile(job.logger));
            }
         }
         return job;
      }
      throw new UnsupportedOperationException("Unable to find Job instance for " + jobname);
   }
}
