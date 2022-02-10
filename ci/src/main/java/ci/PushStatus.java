package ci;

/**
 * Class that has the CI result of the pushed code, i.e. if the code compiled
 * (build) successfully and passed all the test. It also holds the error message
 * if it did not success.
 */
public class PushStatus {
    private Boolean buildSuccess;
    private Boolean testSuccess;
    private String buildMessage = "";
    private String testMessage = "";
    private String commitID;

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
     * @param buildSuccess if the pushed code was compiled successfully
     * @param testSuccess  if the pushed code passed all tests
     * @param buildMessage error message if the pushed code was not compiled
     *                     successfully
     * @param testMessage  error message if the pushed code didn't passed all tests
     */
    public PushStatus(String commitID, Boolean buildSuccess, Boolean testSuccess, String buildMessage, String testMessage) {
        this.commitID = commitID;
        this.buildSuccess = buildSuccess;
        this.testSuccess = testSuccess;
        this.buildMessage = buildMessage;
        this.testMessage = testMessage;
    }

    public Boolean getBuildSuccess() {
        return this.buildSuccess;
    }

    public void setBuildSuccess(Boolean buildSuccess) {
        this.buildSuccess = buildSuccess;
    }

    public Boolean getTestSuccess() {
        return this.testSuccess;
    }

    public void setTestSuccess(Boolean testSuccess) {
        this.testSuccess = testSuccess;
    }

    public String getBuildMessage() {
        return this.buildMessage;
    }

    public void setBuildMessage(String buildMessage) {
        this.buildMessage = buildMessage;
    }

    public String getTestMessage() {
        return this.testMessage;
    }

    public void setTestMessage(String testMessage) {
        this.testMessage = testMessage;
    }


    public Boolean isBuildSuccess() {
        return this.buildSuccess;
    }

    public Boolean isTestSuccess() {
        return this.testSuccess;
    }

    public String getCommitID() {
        return this.commitID;
    }
}
