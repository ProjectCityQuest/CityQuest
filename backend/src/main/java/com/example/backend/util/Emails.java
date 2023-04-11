/**
 * This class implements reusable email templates
 */

package com.example.backend.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

public class Emails {

    public static void sendVerificationMail(String email, String key) throws MessagingException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("Bestätigen Sie Ihre Email!");
        message.setContent("<h1>Verifizieren Sie Ihre Email</h1><hr><a href='http://"+IP.getServerIP()+":5173/email-verifizierung/" + email + "/"+key+"'><button style='border:1px solid black;padding: 20px; color:black;'>Klicken Sie hier um Ihre Email zu verifizieren!</button></a><p>Falls Sie sich nicht bei CityQuest registriert haben, ignorieren Sie diese Email.</p>", "text/html");
        Transport.send(message);
    }

    public static void sendPasswordResetMail(String email, String key) throws MessagingException {
        MimeMessage message = createMailTemplate(email);
        message.setSubject("Passwortänderung!");
        message.setContent("<h1>Folgen Sie den Anweisungen um Ihr Passwort zu ändern</h1><hr><a href='http://"+IP.getServerIP()+":5173/passwort-vergessen/" + email + "/"+key+"'><button style='border:1px solid black;padding: 20px; color:black;'>Klicken Sie hier um Ihr Passwort zu ändern!</button></a><p>Falls Sie keine Passwortänderung angefordert haben, ignorieren Sie diese Email.</p>", "text/html");
        Transport.send(message);
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
