package de.kreth.loghousekeeper;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import de.kreth.loghousekeeper.jobtypes.CheckSizeJob;


public class CheckSizeJobTests extends AbstractFilesystemTests {

   private List<File> files;
   
   @Before
   public void createFiles() throws IOException {
      files = new ArrayList<>();
      for (int i=0; i<10; i++) {
         File f = new File(dir, "test_" + i + ".log");
         StringBuilder text = new StringBuilder("Text content with increasing size depending on name.");
         for(int j=0; j<i;j++) {
            text.append("\nText content with increasing size depending on name.");
         }
         FileUtils.write(f, text, Charset.defaultCharset(), false);
         System.out.println(FileUtils.sizeOf(f) + " bytes in " + f.getAbsolutePath());
         files.add(f);
      }
   }
   
   @Test
   public void testXmlCreation() {
      long warnSize = 300L;
      CheckSizeJob job = new CheckSizeJob(files, warnSize);
      job.add(testReaction);
      job.run();
      assertEquals(5, testReaction.reactedFiles.size());
   }

}
