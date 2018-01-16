package de.kreth.loghousekeeper.reactions;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class DeleteFile implements Reaction {

   private Logger logger;

   public DeleteFile(Logger logger) {
      this.logger = logger;
   }

   @Override
   public void react(File f, String... cause) {
      if(logger.isInfoEnabled()) {
         logger.info("Deleting " + f.getAbsolutePath());
      }
      try {
         FileUtils.forceDelete(f);
      } catch (IOException e) {
         logger.error("Error deleting " + f.getAbsolutePath(), e);
      }
      
   }

}
