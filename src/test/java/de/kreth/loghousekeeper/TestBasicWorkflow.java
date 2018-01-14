package de.kreth.loghousekeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestBasicWorkflow {

	private File dir;
	private Loghousekeeper keeper;

	@Before
	public void createTestDir() throws IOException {
		dir = new File(FileUtils.getTempDirectory(), getClass().getSimpleName());
		assertFalse(dir.exists());
		FileUtils.forceMkdir(dir);

		assertTrue(dir.exists());
		assertTrue(dir.isDirectory());

		keeper = new Loghousekeeper();
	}
	
	@After
	public void deleteTestDir() throws IOException {
		FileUtils.deleteDirectory(dir);
		assertFalse(dir.exists());
	}
	
	@Test
	public void testDeleteOlder() throws IOException {
		String filename = "fileTodelete.log";
		File toDelete = new File(".", filename);
		if(toDelete.exists() == false ) {
			FileUtils.write(toDelete, "Ein Text", Charset.defaultCharset());
		}
		assertTrue(toDelete.exists());
		Calendar timeLimit = new GregorianCalendar();
		timeLimit.add(Calendar.DAY_OF_MONTH, -5);

		toDelete.setLastModified(timeLimit.getTime().getTime());
		Job job = new Job(new File("."), filename, 5, 3);
		keeper.delete(job);
		assertFalse(toDelete.exists());
	}

	@Test
	public void testclearDir() throws IOException {
		Calendar date = new GregorianCalendar();
		for (int i=0; i<10; i++) {
			File f = new File(dir, "test_" + i + ".log");
			FileUtils.write(f, "Test Text at " + date.getTime(),Charset.defaultCharset());
			f.setLastModified(date.getTimeInMillis());
			date.add(Calendar.DAY_OF_MONTH, -1);
		}
		Collection<File> created = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		assertEquals(10, created.size());

		Job job = new Job(dir, "*.log", 5, 5);
		assertEquals(10, job.listFiles().size());
		keeper.delete(job);

		Collection<File> remaining = FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		assertEquals(5, remaining.size());
	}
	
	@Test
	public void testList() {
		Job job = new Job(new File("."), "*", 5, 3);
		Collection<File> files = job.listFiles();
		assertFalse(files.isEmpty());
	}

}
