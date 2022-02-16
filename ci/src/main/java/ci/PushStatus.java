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
     * Constructor
     * 
     * @param commitID       the ID for a commit
     * @param commitDate     the date the commit was done ()
     * @param compileSuccess if the pushed code was compiled successfully
     * @param testSuccess    if the pushed code passed all tests
     * @param compileMessage error message if the pushed code was not compiled
     *                       successfully
     * @param testMessage    error message if the pushed code didn't passed all
     *                       tests
     */
    public PushStatus(String commitID, String commitDate, Boolean compileSuccess, Boolean testSuccess,
            String compileMessage, String testMessage) {
        this.commitID = commitID;
        this.commitDate = formatCommitDate(commitDate);
        this.compileSuccess = compileSuccess;
        this.testSuccess = testSuccess;
        this.compileMessage = compileMessage;
        this.testMessage = testMessage;
    }

    /**
     * format the commitDate (e.g. 2022-02-12T21:30:52+01:00) that is the default
     * json format into e.g. 2022-02-12 21:30:52 +01:00.
     * 
     * @param commitDate the time commit was done in json default format
     * @return reformat commitDate
     */
    private String formatCommitDate(String commitDate) {
        commitDate = commitDate.replace("T", " ");
        commitDate = commitDate.substring(0, 19) + " " + commitDate.substring(19, commitDate.length());
        return commitDate;

    }

    /**
     * return compileSuccess
     * @return compileSuccess
     */
    public Boolean getCompileSuccess() {
        return this.compileSuccess;
    }

    /**
     * return testSuccess
     * @return testSuccess
     */
    public Boolean getTestSuccess() {
        return this.testSuccess;
    }

    /**
     * return compileMessage
     * @return compileMessage
     */
    public String getCompileMessage() {
        return this.compileMessage;
    }

    /**
     * return testMessage
     * @return testMessage
     */
    public String getTestMessage() {
        return this.testMessage;
    }

    /**
     * return commitID
     * @return commitID
     */
    public String getCommitID() {
        return this.commitID;
    }

    /**
     * return commitDate
     * @return commitDate
     */
    public String getCommitDate() {
        return this.commitDate;
    }
}
