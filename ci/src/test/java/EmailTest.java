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
    public void TestEmailSuccessDefaultRecipients() {
        assertTrue(email.send("test successfull sent email for default recipients"));
    }

    @Test
    public void TestEmailSuccessThreeGivenRecipients() {
        String[] recipients = { "elisabeth.chen110@gmail.com",
                "elisabeth.chen100@gmail.com", "echen@kth.se" };
        email.changeRecipients(recipients);
        assertTrue(email.send("test successfull sent email, changed to three recipients"));
    }

    @Test
    public void TestEmailSuccessOneGivenRecipients() {
        String[] recipients = { "elisabeth.chen110@gmail.com"};
        email.changeRecipients(recipients);
        assertTrue(email.send("test successfull sent email, changed to one recipients"));
    }

    @Test
    public void TestEmailFailOneGivenRecipients() {
        String[] recipients = { "this is not an email address"};
        email.changeRecipients(recipients);
        assertFalse(email.send("test failed sent email, changed to one recipients"));
    }

    @Test
    public void TestGetContentAllSuccess() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Success", "Success");
        String actual =email.getContent(new PushStatus(commitID, true, true, "", ""));
        assertEquals(expected, actual);
    }

    @Test
    public void TestGetContentComileSuccessTestFail() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Success", "Fail");
        expected += "\n\nError message for test:\n\ttest failed";
        String actual =email.getContent(new PushStatus(commitID, true, false, "", "test failed"));
        assertEquals(expected, actual);
    }

    @Test
    public void TestGetContentComileFailTestFail() {
        String commitID = "33ds2e12saa";
        String expected = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", commitID, "Fail", "Fail");
        expected += "\n\nError message for compile:\n\tcompile failed";
        expected += "\n\nError message for test:\n\ttest failed";
        String actual =email.getContent(new PushStatus(commitID, false, false, "compile failed", "test failed"));
        // assertEquals(expected, actual);
    }
}
