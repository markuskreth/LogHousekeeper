package de.kreth.loghousekeeper.jobtypes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

   @Override
   public final void react(File f, String...strings ) {
      for (Reaction r: reactions) {
         r.react(f, strings);
      }
   }
   
   public abstract void appendTo(Element root);
   
   public static AbstractJob parse(Element e) {
      String jobname = e.getName();
      if(jobname.equals(DeleteJob.class.getName())) {
         return DeleteJob.parse(e);
      }
      if(jobname.equals(CheckSizeJob.class.getName())) {
         return CheckSizeJob.parse(e);
      }
      
      throw new UnsupportedOperationException("Unable to find Job instance for " + jobname);
   }
}
