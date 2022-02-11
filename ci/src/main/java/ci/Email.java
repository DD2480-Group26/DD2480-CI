package ci;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * Send a email to every participant in the group project (i.e. the recipients)
 * for every push. In the email, it includes the results of the compile and test
 * of the pushed code and the corresponding error message if either compile or
 * test fails.
 */
public class Email {
    private Properties prop;
    private Session session;
    private final String sender = "group26CI@gmail.com";
    private final String pwd = "pas123&pas";
    private String[] recipients = { "elisabeth.chen010@gmail.com",
            "carl.engelhardt@gmail.com",
            "juliavasastan@gmail.com", "hemena@kth.se", "victor.massy@grenoble-inp.org"
    };

    /**
     * Get the parameters in pushStatus to a String
     * 
     * @param pushStatus has parameters about the compile and test result of the
     *                   pushed code
     * @return email String
     */
    public String getContent(PushStatus pushStatus) {
        String compileSuccessTxt = pushStatus.getCompileSuccess() ? "Success" : "Fail";
        String testSuccessTxt = pushStatus.getTestSuccess() ? "Success" : "Fail";
        String content = String.format("Status for commit %s:\n \tCompile: %s\n\tTest: %s ", pushStatus.getCommitID(),
                compileSuccessTxt, testSuccessTxt);
        if (!pushStatus.getCompileSuccess()) {
            content += "\n\nError message for compile:\n\t" + pushStatus.getCompileMessage();
        }
        if (!pushStatus.getTestSuccess()) {
            content += "\n\nError message for test:\n\t" + pushStatus.getTestMessage();
        }
        return content;
    }

    /**
     * send an email to all recipients.
     * 
     * @param PushStatus has parameters about the compile and test result of the
     *                   pushed code. Used to get the content of the email
     * @return boolean, if the email was successfully sent
     */
    public boolean send(PushStatus pushStatus) {
        try {
            initEmail();
            authenticateSender();
            createMessage(getContent(pushStatus));
            System.out.println("Email sent");
            return true;
        } catch (Exception e) {
            System.out.println("Failed to send the email");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * initialize the properties for email
     */
    private void initEmail() {
        prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }

    /**
     * authenticate the sender
     */
    private void authenticateSender() {
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, pwd);
            }
        });
    }

    /**
     * create the massage
     * 
     * @param content the content of the message
     * @throws AddressException
     * @throws MessagingException
     */
    private void createMessage(String content) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.addRecipients(Message.RecipientType.TO, stringToAddress(recipients));
        message.setSubject("CI Server Group 26 - Notification");
        message.setText(content);

        Transport.send(message);
    }

    /**
     * change the string addresses to InternetAddress-objects
     * 
     * @param strAddresses string addresses
     * @return InternetAddress-objects of strAddresses
     * @throws AddressException
     */
    private InternetAddress[] stringToAddress(String[] strAddresses) throws AddressException {
        InternetAddress[] addresses = new InternetAddress[strAddresses.length];
        for (int i = 0; i < strAddresses.length; i++) {
            addresses[i] = new InternetAddress(strAddresses[i]);
        }
        return addresses;
    }

    /**
     * change the recipients
     * 
     * @param recip addresses it changes to
     */
    public void changeRecipients(String[] recip) {
        recipients = recip;
    }

    /**
     * return recipients
     * 
     * @return recipients
     */
    public String[] getRecipients() {
        return recipients;
    }
}
