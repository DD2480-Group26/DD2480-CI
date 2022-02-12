package ci;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.ProcessBuilder;
import java.nio.Buffer;
import java.util.Enumeration;
import org.json.*;

import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Skeleton of a ContinuousIntegrationServer which acts as webhook
 * See the Jetty documentation for API documentation of those classes. ok
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    static int port = 8080;

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {

        String url = ((HttpServletRequest)request).getRequestURL().toString();
        System.out.println(url);
        
        // use regex to see if any specific build is accessed
        Pattern singleBuildPattern = Pattern.compile("http://localhost:" + port + "/builds/.");
        Matcher matcher = singleBuildPattern.matcher(url);
        // true if URL matches regex of singleBuildPattern
        boolean singleBuild = matcher.find();
        
        // show list of all stored builds
        if (url.equals("http://localhost:" + port + "/builds")) {
            response.setContentType("text/html");

            // get all build filenames in buildHistory directory
            File dir = new File("../buildHistory");
            String[] buildFileNames = dir.list();
            // start of simple html file
            String output = "<!DOCTYPE html>\n<html>\n<body>\n<h1>Build list</h1>";
            
            // iterate through each build
            for (String build : buildFileNames) {
                String buildURL = url + "/" + build;
                // add link to the build in the html
                output += "<a href=\"" + buildURL + "\">" + build + "</a> <br>";
            }

            output += "</body>\n</html>"; // end of simple html file
            response.getWriter().print(output);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }
        else if (singleBuild) {
            String output = "";

            // get the name of the build from the URL
            int indexOfBuilds = url.indexOf("builds/");
            String buildName = url.substring(indexOfBuilds + 7, url.length());

            try {
                File build = new File("../buildHistory/" + buildName);
                BufferedReader br = new BufferedReader(new FileReader(build));
                String line;
                while ((line = br.readLine()) != null) {
                    output += line + "\n";
                }
            }
            catch (Exception e) {
                output += "error: no such build exists";
            }

            response.getWriter().print(output);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }
        else {
            String githubEvent = request.getHeader("X-Github-Event");
            System.out.println(githubEvent);
            switch(githubEvent){
                case "push":
                    
                    JSONObject payload = new JSONObject(request.getParameter("payload"));
                    JSONObject headCommit = (JSONObject) payload.get("head_commit");
                    JSONObject author = (JSONObject) headCommit.get("author");
                    
                    String branchName = (String) payload.get("ref");
                    branchName = branchName.replaceAll("refs/heads/", "");
                    String id = (String) headCommit.get("id");
                    String email = (String) author.get("email");
                    
                    // 1st clone your repository
                    PushTester pushTester = new PushTester();
                    File localDirectory = new File("GitPull/");
    
                    Git git = GitConnector.cloneRepo("https://github.com/DD2480-Group26/DD2480-CI.git", localDirectory);
                    GitConnector.gitPull(localDirectory, branchName);
                    GitConnector.checkoutToBranch(localDirectory, "origin/" + branchName);
    
                    PushTester pt = new PushTester();
                    PushStatus pushStatus = pt.getPushStatus(localDirectory);
    
                    // 2nd compile the code
                    pushTester.fileExecuter(localDirectory);
    
                    response.getWriter().println("CI job Done");
    
                    //Delete the directory
                    git.getRepository().close();
                    GitConnector.deleteDirectory(localDirectory);
                    localDirectory.delete();
                    break;
                case "issues":
                    // DO issues action
                    System.out.println("Issues");
                default:
                    // DO default actions
                    System.out.println("No event match for " + githubEvent );
                    break;
            }
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
        }
    }

    /**
     * Parse request data and create a string containing the payload.
     *
     * @param request A HttpServletRequest recieved by handler.
     * @return String Payload of the request.
     */
    public String getRequestPayload( HttpServletRequest request){
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
        Server server = new Server(port);
        server.setHandler(new ContinuousIntegrationServer());

        server.start();
        server.join();


    }
}
