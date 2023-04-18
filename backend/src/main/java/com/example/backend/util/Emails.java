/**
 * This class implements reusable email templates
 */

package com.example.backend.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Emails {
    private static final Logger LOG = LoggerFactory.getLogger(Emails.class);

    public static void sendVerificationMail(String email, String key) throws MessagingException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("Bestätigen Sie Ihre Email!");

        EmailTemplate emailVerificationTemplate = new EmailTemplate();
        emailVerificationTemplate.addTitle("Verifizieren Sie Ihre Email");
        emailVerificationTemplate.addLine();
        emailVerificationTemplate.addButton("Klicken Sie hier um Ihre Email zu verifizieren!", "http://"+IP.getServerIP()+":5173/email-verifizierung/" + email + "/"+key);
        emailVerificationTemplate.addText("Falls Sie sich nicht bei CityQuest registriert haben, ignorieren Sie diese Email.");

        message.setContent(emailVerificationTemplate.generateEmailTemplate(), "text/html");

        Transport.send(message);

        LOG.info("Email Verification sent to '" + email + "' containig key: '" + key + "'");
    }

    public static void sendPasswordResetMail(String email, String key) throws MessagingException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("Passwortänderung!");

        EmailTemplate passwordResetEmailTemplate = new EmailTemplate();
        passwordResetEmailTemplate.addTitle("Folgen Sie den Anweisungen um Ihr Passwort zu ändern");
        passwordResetEmailTemplate.addLine();
        passwordResetEmailTemplate.addButton("Klicken Sie hier um Ihr Passwort zu ändern!", "http://"+IP.getServerIP()+":5173/passwort-vergessen/" + email + "/"+key);
        passwordResetEmailTemplate.addText("Falls Sie keine Passwortänderung angefordert haben, ignorieren Sie diese Email.");

        message.setContent(passwordResetEmailTemplate.generateEmailTemplate(), "text/html");
        Transport.send(message);

        LOG.info("Email for password reset sent to '" + email + "' containig key: '" + key + "'");
    }

    private static MimeMessage createMailTemplate(String email) throws MessagingException {
        String to = email;
        String from = "cityquest.noreply@gmail.com";
        String host = "smtp.gmail.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", false);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cityquest.noreply@gmail.com", "lwykmklxlaykjufg");
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

        return message;
    }
}
