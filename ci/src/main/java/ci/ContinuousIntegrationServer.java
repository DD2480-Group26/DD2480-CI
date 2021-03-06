package ci;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;

import java.io.BufferedReader;


import org.json.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The CI server handles compiling and testing the pushed code. The author of
 * the commit (pushed code) will be notified about the results with an email.
 * If the author is not authorized (i.e. is not a member of the team
 * DD2480-Group26), the code will not be compiled and tested and the author will
 * receive an email about his/her push was unauthorized.
 * See README for how to set up the CI server.
 */
public class ContinuousIntegrationServer extends AbstractHandler {

    public ContinuousIntegrationServer() {
    }

    static int port = 8080;

    /**
     * Handle all CI functions, including compile, tests and notification. 
     * @param target the target in string
     * @param baseRequest the base request
     * @param request A HttpServletRequest recieved by handler.
     * @param response A HttpServletResponse recieved by handler.
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException, ServletException {

        if (!buildInUrl(request.getRequestURL().toString(), baseRequest, response)) {
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
                        PushStatus pushStatus = pt.createPushStatus(localDirectory, id, timestamp);
                        generateBuildLog(pushStatus);

                        // notify the author about the CI result
                        emailObj.send(pushStatus, email);
                    } else {
                        // notify the unauthorized author
                        emailObj.send("You are not authorized to push to this project", email);
                    }

                    response.getWriter().println("CI job Done");

                    // Delete the directory
                    if (git != null) {
                        git.getRepository().close();
                    }
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


    }

    /**
     * Generates a log file in the buildHistory directory.
     *
     * @param ps PushStatus object, to get information about build
     */
    private void generateBuildLog(PushStatus ps) {
        try {
            // create new log file
            System.out.println("Generating log file");
            String date = ps.getCommitDate();
            date = date.substring(0, 19);
            date = date.replace(':', '-');
            date = date.replace(' ', 'T');
            String buildLogName = date + ".txt";
            FileWriter logWriter = new FileWriter("../buildHistory/" + buildLogName);

            // write the log information to file
            logWriter.write("Build date: " + ps.getCommitDate() + "\n");
            logWriter.write(Email.getContent(ps));

            logWriter.close();
        } catch (Exception e) {
            // Prints this throwable and its backtrace to the specified print stream.
            e.printStackTrace();
        }
    }

    /**
     *  checks if the incoming request is about build.
     *  If it is about build, then it saves the build log in the folder "buildHistory"
     *
     * @param url         Requested URL
     * @param baseRequest A request instance is created for each HttpConnection accepted by the server
     * @param response    ServletResponse
     * @return Boolean true if URL matches, otherwise false
     * @throws IOException general class of exceptions
     */

    private boolean buildInUrl(String url, Request baseRequest, HttpServletResponse response) throws IOException, NullPointerException {

        String subDirectory = "";
        boolean singleBuild = false;
        int ifSubDirectoryURL = url.indexOf('/', 8);
        // true if URL matches http(s)://<host>/*, false if matches http(s)://<host>
        if (ifSubDirectoryURL != -1) {
            subDirectory = url.substring(ifSubDirectoryURL);
            // use regex to see if any specific build is accessed
            Pattern singleBuildPattern = Pattern.compile("/builds/.");
            Matcher matcher = singleBuildPattern.matcher(subDirectory);
            // true if URL matches regex of singleBuildPattern
            singleBuild = matcher.find();
        }

        // show list of all stored builds if URL is http(s)://<host>/builds
        if (subDirectory.equals("/builds")) {
            response.setContentType("text/html");

            // get all build filenames in buildHistory directory
            File dir = new File("../buildHistory");
            String[] buildFileNames = dir.list();
            // start of simple html file
            StringBuilder output = new StringBuilder("<!DOCTYPE html>\n<html>\n<body>\n<h1>Build list</h1>");

            // iterate through each build
            if (buildFileNames != null) {
                for (String build : buildFileNames) {
                    String buildURL = url + "/" + build;
                    // add link to the build in the html
                    output.append("<a href=\"").append(buildURL).append("\">").append(build).append("</a> <br>");
                }
            }

            output.append("</body>\n</html>"); // end of simple html file
            response.getWriter().print(output);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);
            return true;
        } else return isSingleBuild(url, baseRequest, response, singleBuild);
    }

    /**
     * show single build information if URL is http(s)://<host>/builds/<singleBuildFileName>
     *
     * @param url         Requested URL
     * @param baseRequest A request instance is created for each HttpConnection accepted by the server
     * @param response    ServletResponse
     * @param singleBuild boolean value for URL matches regex of singleBuildPattern
     * @return Boolean true if successfully find a build, otherwise false
     */
    private boolean isSingleBuild(String url, Request baseRequest, HttpServletResponse response, boolean singleBuild) throws IOException {
        if (singleBuild) {
            StringBuilder output = new StringBuilder();

            // get the name of the build from the URL
            int indexOfBuilds = url.indexOf("builds/");
            String buildName = url.substring(indexOfBuilds + 7, url.length());

            try {
                File build = new File("../buildHistory/" + buildName);
                BufferedReader br = new BufferedReader(new FileReader(build));
                String line;
                while ((line = br.readLine()) != null) {
                    output.append(line).append("\n");
                }
                br.close();
            } catch (Exception e) {
                output.append("error: no such build exists");
            }

            response.getWriter().print(output);
            response.setStatus(HttpServletResponse.SC_OK);
            baseRequest.setHandled(true);

            return true;
        }
        return false;
    }

    /**
     * used to start the CI server in command line
     */
    public static void main(String[] args) throws Exception {
        Server server = new Server(port);
        server.setHandler(new ContinuousIntegrationServer());

        server.start();
        server.join();

    }
}