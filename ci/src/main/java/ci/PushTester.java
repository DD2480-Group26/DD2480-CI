package ci;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.lang.ProcessBuilder;
import java.io.File;
import java.io.IOException;

public class PushTester  {
    private Boolean compileSuccess = true;
    private Boolean testSuccess = true;
    private String compileMessage = "";
    private String testMessage = "";

    /**
     * Sets the Push status variables
     *
     * @param Local folder
     * @return void
     */

    public PushStatus getPushStatus(File dir, String commitID, String commitDate){
        compileSuccess = true;
        testSuccess = true;
        compileMessage = "";
        testMessage = "";
        fileExecuter(dir);

        PushStatus pushStatus = new PushStatus( commitID, commitDate, compileSuccess,  testSuccess,  compileMessage,  testMessage);
        return pushStatus;
    }


    /**
     * Build the directory inputed using gradle
     *
     * @param Local folder
     * @return void
     */
    public void fileExecuter(File dir){

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
                if(line.contains("BUILD FAILED")){
                    compileSuccess =false;
                    testSuccess=false;
                    testMessage="Tests not run because compile failed.";
                }
                if(line.contains("Test") && line.contains("FAILED")){
                    testSuccess=false;
                    testMessage=testMessage + "\n Failedtest: " + testMessage;
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