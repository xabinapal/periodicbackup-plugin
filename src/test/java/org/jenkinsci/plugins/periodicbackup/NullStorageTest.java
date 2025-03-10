package org.jenkinsci.plugins.periodicbackup;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.File;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
/**
 * Created by IntelliJ IDEA.
 * Author: tblaszcz
 * Date: 15-04-11
 */
public class NullStorageTest  {

    private String baseFileName;
    private File tempDirectory;
    private NullStorage nullStorage;
    private File archive1;
    @Rule
    public JenkinsRule r = new JenkinsRule();
    @Before
    public void setUp() throws Exception {
        baseFileName = "baseFileName";
        nullStorage = new NullStorage();
        tempDirectory = new File(Resources.getResource("data/temp2/").getFile());
        if(tempDirectory.exists()) {
            FileUtils.deleteDirectory(tempDirectory);
        }
        assertTrue(tempDirectory.mkdir());
        assertTrue(new File(tempDirectory, "dummy").createNewFile());
        archive1 = new File(Resources.getResource("data/archive1").getFile());
    }

    @Test
    public void testBackupStop() throws Exception {
        File expectedResult = new File(tempDirectory, baseFileName + ".null");
        assertTrue(archive1.exists());
        nullStorage.backupStart(tempDirectory.getAbsolutePath(), baseFileName);
        nullStorage.backupAddFile(archive1);
        Iterable<File> archives = nullStorage.backupStop();

        assertTrue(expectedResult.exists());
        assertTrue(expectedResult.isDirectory());
        assertEquals(archives.iterator().next(), expectedResult);

    }
    @Test
    public void testUnarchiveFiles() throws Exception {
        File sourceDir = new File(Resources.getResource("data/temp/").getFile());
        File newFile = new File(sourceDir, "newFile");
        if(!newFile.exists()) {
            assertTrue(newFile.createNewFile());
        }
        File expectedResult = new File(tempDirectory, "newFile");
        if(expectedResult.exists()) {
            assertTrue(expectedResult.delete());
        }
        nullStorage.unarchiveFiles(Lists.newArrayList(sourceDir),tempDirectory);

        assertTrue(expectedResult.exists());
    }
}
