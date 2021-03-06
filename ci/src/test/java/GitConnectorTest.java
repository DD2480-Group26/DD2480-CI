
import static org.junit.Assert.assertTrue;

import ci.GitConnector;
import org.junit.Test;

import java.io.File;

import org.eclipse.jgit.api.Git;


public class GitConnectorTest extends GitConnector {

    @Test
    public void testGitPull() {
        //Testing the pull function
        File localDirectory = new File("TestGitPull\\");
        Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git", localDirectory);
        assertTrue(GitConnector.gitPull(localDirectory, "main"));

        //Delete the directory
        git.getRepository().close();
        GitConnector.deleteDirectory(localDirectory);
        localDirectory.delete();
    }


    @Test
    public void testGitCheckout() {
        //Testing the checkout function
        File localDirectory = new File("TestGitPull\\");
        Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git", localDirectory);
        GitConnector.gitPull(localDirectory, "test");
        assertTrue(GitConnector.checkoutToBranch(localDirectory, "origin/test"));

        //Delete the directory
        git.getRepository().close();
        GitConnector.deleteDirectory(localDirectory);
        localDirectory.delete();
    }
}
