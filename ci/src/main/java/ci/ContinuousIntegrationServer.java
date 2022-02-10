package ci;

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
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes.
 */
public class ContinuousIntegrationServer extends AbstractHandler {


    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        System.out.println(target);


        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        File localDirectory = new File("GitPull\\");
        String branchName = "";
        Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git", localDirectory);
        GitConnector.gitPull(localDirectory, branchName);
        GitConnector.checkoutToBranch(localDirectory, "origin/" + branchName);
        // 2nd compile the code

        response.getWriter().println("CI job Done");

        //Delete the directory
        git.getRepository().close();
        GitConnector.deleteDirectory(localDirectory);
        localDirectory.delete();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }
}
