import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ci.Email;
import ci.PushStatus;

/**
 * try the Email class
 */
public class EmailTest {
    private Email email;

    @Before
    public void createEmailObject() {
        email = new Email();
    }

    @Test
    public void TestSendStringOneRecipientSuccess() {
        assertTrue(email.send("test success sending one recipient", "elisabeth.chen110@gmail.com"));
    }

    @Test
    public void TestSendStringOneRecipientFail() {
        assertFalse(email.send("test fail sending one recipient", "this is not an email"));
    }

    @Test
    public void TestSendStringTwoRecipientSuccess() {
        String[] recepients = { "elisabeth.chen110@gmail.com", "elisabeth.chen100@gmail.com" };
        assertTrue(email.send("test success sending two recipient", recepients));
    }

    @Test
    public void TestSendStringTwoRecipientFail() {
        String[] recepients = { "elisabeth.chen110@gmail.com", "this is not an email" };
        assertFalse(email.send("test fail sending two recipient", recepients));
    }

    @Test
    public void TestSendSuccessPushStatusDefaultRecipients() {
        assertTrue(email.send(new PushStatus("gf12d44g43c1fb", true, false, "",
                "test successfull sent email for default recipients")));
    }

    @Test
    public void TestSendSuccessPushStatusChangedThreeRecipients() {
        String[] recipients = { "elisabeth.chen110@gmail.com",
                "elisabeth.chen100@gmail.com", "echen@kth.se" };
        email.changeRecipients(recipients);
        assertTrue(email.send(new PushStatus("gf12d44g43c1fb", true, false, "",
                "test successfull sent email, changed to three recipients")));
    }

    @Test
    public void TestSendSuccessPushStatusOneGivenRecipients() {
        assertTrue(email.send(new PushStatus("gf12d44g43c1fb", true, false, "",
                "test successfull sent email, changed to one recipients"), "elisabeth.chen010@gmail.com"));
    }

    @Test
    public void TestSendPushStatusFailChangedOneRecipients() {
        String[] recipients = { "this is not an email address" };
        email.changeRecipients(recipients);
        assertFalse(email.send(new PushStatus("gf12d44g43c1fb", true, false, "",
                "test failed sent email, changed to one recipients")));
    }

    @Test
    public void TestSendPushStatusFailOneGivenRecipients() {
        assertFalse(email.send(new PushStatus("gf12d44g43c1fb", true, false, "",
                "test failed sent email, changed to one recipients"), "this is not an email address"));
    }

    @Test
    public void TestGetContentAllSuccess() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Success",
                "Success");
        String actual = email.getContent(new PushStatus(commitID, true, true, "", ""));
        assertEquals(expected, actual);
    }

    @Test
    public void TestGetContentCompileSuccessTestFail() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Success",
                "Fail");
        expected += "\n\nError message for test:\n\ttest failed";
        String actual = email.getContent(new PushStatus(commitID, true, false, "", "test failed"));
        assertEquals(expected, actual);
    }

    @Test
    public void TestGetContentCompileFailTestFail() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Fail", "Fail");
        expected += "\n\nError message for compile:\n\tcompile failed";
        expected += "\n\nError message for test:\n\ttest failed";
        String actual = email.getContent(new PushStatus(commitID, false, false, "compile failed", "test failed"));
        assertEquals(expected, actual);
    }
}
