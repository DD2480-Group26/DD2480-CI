package ci;

/**
 * Class that has the CI result of the pushed code, i.e. if the code compiled
 * successfully and passed all the test. It also holds the error message
 * if it did not success.
 */
public class PushStatus {
    private Boolean compileSuccess;
    private Boolean testSuccess;
    private String compileMessage = "";
    private String testMessage = "";
    private String commitID;
    private String commitDate;

    /**
     * Constructor with one parameters
     * @param commitID the ID for a commit
     */
    public PushStatus(String commitID) {
        this.commitID = commitID;
    }

    /**
     * Constructor
     * 
     * @param commitID the ID for a commit
     * @param compileSuccess if the pushed code was compiled successfully
     * @param testSuccess  if the pushed code passed all tests
     * @param compileMessage error message if the pushed code was not compiled
     *                     successfully
     * @param testMessage  error message if the pushed code didn't passed all tests
     */
    public PushStatus(String commitID, Boolean compileSuccess, Boolean testSuccess, String compileMessage, String testMessage) {
        this.commitID = commitID;
        this.compileSuccess = compileSuccess;
        this.testSuccess = testSuccess;
        this.compileMessage = compileMessage;
        this.testMessage = testMessage;
    }

    public Boolean getCompileSuccess() {
        return this.compileSuccess;
    }

    public Boolean getTestSuccess() {
        return this.testSuccess;
    }

    public String getCompileMessage() {
        return this.compileMessage;
    }

    public String getTestMessage() {
        return this.testMessage;
    }

    public String getCommitID() {
        return this.commitID;
    }

    public String getCommitDate() {
        return this.commitDate;
    }
}
