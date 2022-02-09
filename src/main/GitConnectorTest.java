import static org.junit.Assert.*;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.junit.Test;

public class GitConnectorTest extends GitConnector {

	@Test
	public void testGitPull() {
		//Testing the pull function
		File localDirectory = new File("TestGitPull\\");
    	Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git",localDirectory);
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
    	Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git",localDirectory);
    	GitConnector.gitPull(localDirectory, "issue2");
    	assertTrue(GitConnector.checkoutToBranch(localDirectory, "origin/issue2"));
        
        //Delete the directory
    	git.getRepository().close();
    	GitConnector.deleteDirectory(localDirectory);
    	localDirectory.delete();
	}
}
