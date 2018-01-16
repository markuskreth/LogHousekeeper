package de.kreth.loghousekeeper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

public class AbstractFilesystemTests {

   protected File dir;
   protected TestReaction testReaction;

   @Before
   public void createTestDir() throws IOException {
   	dir = new File(FileUtils.getTempDirectory(), getClass().getSimpleName());
   	assertFalse(dir.exists());
   	FileUtils.forceMkdir(dir);
   
   	assertTrue(dir.exists());
   	assertTrue(dir.isDirectory());
   	testReaction = new TestReaction();
   }

   @After
   public void deleteTestDir() throws IOException {
   	FileUtils.deleteDirectory(dir);
   	assertFalse(dir.exists());
   }

}
