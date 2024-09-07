package org.jlab.epsci.dpux.core.process;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * A utility class to send email alerts.
 *
 * @author Vardan Gyurjyan
 */
public class EmailAlert {

    private static final String username = "your_email@example.com";
    private static final String password = "your_password";

    /**
     * This function `sendAlert` is responsible for sending email alerts.
     * It creates a SMTP session with the required properties and authentication, then
     * builds the email message with the provided content and sends it.
     *
     * @param subject       The subject of the email.
     * @param messageContent The content to be included in the body of the email.
     */
    public void sendAlert(String subject, String messageContent) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.example.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from_email@example.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("to_email@example.com"));
            message.setSubject(subject);
            message.setText(messageContent);

            Transport.send(message);
            System.out.println("Alert email sent successfully!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
