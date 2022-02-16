package ci;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

/*
* Takes a directory containing code of an github commit and the id and date of the commit. Then the
* code in the directory is built and tested using gradle. Depending on the output a PushStatus
* objects is created and returned.
 */
public class PushTester  {
    private Boolean compileSuccess = true;
    private Boolean testSuccess = true;
    private String compileMessage = "";
    private String testMessage = "";

    /**
     * Executes the testing of an github repository and create a Push status object containing the result.
     *
     * @param dir File  A file object for the github repository.
     * @param commitID String   The id of the commit containing the github repository.
     * @param commitDate String     The data of the commit.
     * @return PushStatus object containing the results of the test.
     */
    public PushStatus createPushStatus(File dir, String commitID, String commitDate){
        compileSuccess = true;
        testSuccess = true;
        compileMessage = "";
        testMessage = "";
        fileExecuter(dir);

        PushStatus pushStatus = new PushStatus( commitID, commitDate, compileSuccess,  testSuccess,  compileMessage,  testMessage);
        return pushStatus;
    }


    /**
     * Build and test the directory inputed using gradle. Sets the class variables depending on the result.
     *
     * @param dir File  A file object for the github repository.
     * @return void
     */
    private void fileExecuter(File dir){

        try {
            String path = dir.toString();
            System.out.println(path);
            Process permissionProcess = Runtime.getRuntime().exec("chmod 777 gradlew", null, dir);
            permissionProcess.waitFor();
            Process buildProcess = Runtime.getRuntime().exec("./gradlew build", null, dir);
            buildProcess.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(buildProcess.getInputStream()));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();

            while ((line = reader.readLine()) != null)
            {
                if(line.contains("compileJava FAILED")){
                    compileSuccess =false;
                    testSuccess=false;
                    testMessage="Tests not run because compile failed.";
                }
                if(line.contains("Test") && line.contains("FAILED")){
                    testSuccess=false;
                    testMessage=testMessage + "\n Failedtest: " + line;
                }

                stringBuilder.append(line);
                System.out.println(line);
            }

            if(compileSuccess){
                compileMessage="Successfully compiled";
            }else{
                compileMessage="COMPILED FAILED \n" + stringBuilder.toString();
            }


        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error when executing code.");
        }

    }

}
