import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;
 
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.merge.MergeStrategy;

/** 
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
	

	public static void deleteFolder(File folder) {
	    File[] files = folder.listFiles();
	    if(files!=null) { 
	        for(File f: files) {
	            if(f.isDirectory()) {
	                deleteFolder(f);
	            } else {
	                f.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
	public static void cloneRepo(String remotePath, File localPath) {
		 try {
			 CloneCommand cloneCommand = new CloneCommand();
			 cloneCommand.setURI(remotePath);
			 cloneCommand.setDirectory(localPath);
			 cloneCommand.call();

		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	}
	
	public static boolean gitPull(File localRepoPath, String branch) {
		 boolean pullSuccessful;
		 
		 try (Git git = Git.open(localRepoPath)) {
		 
			 PullCommand pull = git.pull();
			 pull.setRemote("origin");
			 pull.setRemoteBranchName(branch); 
			 pull.setStrategy(MergeStrategy.RECURSIVE);
			 pull.setRebase(true);
			 PullResult result = pull.call();
			 pullSuccessful = result.isSuccessful();
		 
		 } catch (Exception e) {
			 pullSuccessful = false; 
		 }
		 System.out.println("Pull");
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

	
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);
        


        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        File localDirectory = new File("TestGitPull\\");
    	String localPath = localDirectory.getPath();
    	cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git",localDirectory);
    	gitPull(localDirectory, "test");
    	checkoutToBranch(localDirectory, "origin/test");
        // 2nd compile the code

        response.getWriter().println("I'm testing");
    }
 
    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer()); 
        server.start();
        server.join();
    }
}
