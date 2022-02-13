package ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import org.json.*;

/**
 * The CI server handles compiling and testing the pushed code. The author of
 * the commit (pushed code) will be notified about the results with an email.
 * If the author is not authorized (i.e. is not a member of the team
 * DD2480-Group26), the code will not be compiled and tested and the author will
 * receive an email about his/her push was unauthorized.
 * 
 * See README for how to set up the CI server.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    public void handle(String target,
            Request baseRequest,
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String githubEvent = request.getHeader("X-Github-Event");
        System.out.println(githubEvent);
        switch (githubEvent) {
            case "push":
                // get informations from the request
                JSONObject payload = new JSONObject(request.getParameter("payload"));
                JSONObject headCommit = (JSONObject) payload.get("head_commit");
                JSONObject author = (JSONObject) headCommit.get("author");

                String branchName = (String) payload.get("ref");
                branchName = branchName.replaceAll("refs/heads/", "");
                String id = (String) headCommit.get("id");
                String timestamp = (String) headCommit.get("timestamp");
                String email = (String) author.get("email");

                // clone repository
                File localDirectory = new File("GitPull/");
                Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git", localDirectory);
                GitConnector.gitPull(localDirectory, branchName);
                GitConnector.checkoutToBranch(localDirectory, "origin/" + branchName);

                // create email object that handel notification
                Email emailObj = new Email();
                if (emailObj.isAuthorizedAuthor(email)) {
                    // compile and test the code if the commit author is authorized
                    PushTester pt = new PushTester();
                    PushStatus pushStatus = pt.getPushStatus(localDirectory, id, timestamp);

                    // notify the author about the CI result
                    emailObj.send(pushStatus, email);
                } else {
                    // notify the unauthorized author
                    emailObj.send("You are not authorized to push to this project", email);
                }

                response.getWriter().println("CI job Done");

                // Delete the directory
                git.getRepository().close();
                GitConnector.deleteDirectory(localDirectory);
                localDirectory.delete();
                break;
            case "issues":
                // DO issues action
                System.out.println("Issues");
            default:
                // DO default actions
                System.out.println("No event match for " + githubEvent);
                break;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

    }

    /**
     * Parse request data and create a string containing the payload.
     *
     * @param request A HttpServletRequest recieved by handler.
     * @return String Payload of the request.
     */
    public String getRequestPayload(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    // Append charvuffer to String builder with given offsets.
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            System.out.println("Error when parsing payload");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

        return stringBuilder.toString();

    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        server.setHandler(new ContinuousIntegrationServer());

        server.start();
        server.join();

    }
}
