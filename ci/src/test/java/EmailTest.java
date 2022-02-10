import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import ci.Email;

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
}
