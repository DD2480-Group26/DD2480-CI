package ci;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

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
     * send an email. 
     * @param content content of the email
     * @return if the email was successfully sent
     */
    public boolean send(String content) {
        try {
            initEmail();
            authenticateSender();
            createMessage(content);
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
     * change the recipients to recip
     * @param recip addresses in string array
     */
    public void changeRecipients(String[] recip) {
        recipients = recip;
    }

    /**
     * return recipients
     * @return recipients
     */
    public String[] getRecipients() {
        return recipients;
    }
}
