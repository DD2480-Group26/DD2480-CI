import static org.junit.Assert.assertTrue;

import java.io.File;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.merge.MergeStrategy;

public class GitConnector {

	public static void deleteDirectory(File file)
    {
        for (File subfile : file.listFiles()) {

            if (subfile.isDirectory()) {
                deleteDirectory(subfile);
            }
  
            subfile.delete();
        }
    }
	
	public static Git cloneRepo(String remotePath, File localPath) {
		Git git; 
		try {
			 CloneCommand cloneCommand = new CloneCommand();
			 cloneCommand.setURI(remotePath);
			 cloneCommand.setDirectory(localPath);
			 git = cloneCommand.call();
			 return git;
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
		 return null;
	}
	
	public static boolean gitPull(File localRepoPath, String branch) {
		 boolean pullSuccessful;
		 
		 try (Git git = Git.open(localRepoPath)) {
		 
			 PullCommand pull = git.pull();
			 pull.setRemote("origin");
			 pull.setRemoteBranchName(branch); 
//			 pull.setStrategy(MergeStrategy.RECURSIVE);
			 pull.setRebase(true);
			 PullResult result = pull.call();
			 pullSuccessful = result.isSuccessful();
		 
		 } catch (Exception e) {
			 pullSuccessful = false; 
		 }
		 return pullSuccessful;
	}
	
	public static boolean checkoutToBranch(File repositoryLocalPath, String branchName) {

		  boolean actionCompleted = false;

		  try (Git git = Git.open(repositoryLocalPath)) {
		    git.checkout()
		      .setName(branchName)
		      .setStartPoint("origin/" + branchName)
		      .call();
		    
		    actionCompleted = true;
		  } catch (Exception e) {
		    e.printStackTrace();
		  }
		  return actionCompleted;
		}

	
}

