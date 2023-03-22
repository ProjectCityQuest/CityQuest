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

    public static void sendMail(String email, String key) throws MessagingException {
        String ip;
        try(final DatagramSocket socket = new DatagramSocket()){
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }

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
        message.setSubject("Bestätigen Sie Ihre Email!");
        message.setContent("<h1>Verifizieren Sie Ihre Email</h1><hr><a href='http://"+ip+":5173//email-verifizierung/" + to + "/"+key+"'><button style='border:1px solid black;padding: 20px; color:black;'>Klicken Sie hier um Ihre Email zu verifizieren!</button></a><p>Falls Sie sich nicht bei CityQuest registriert haben, ignorieren Sie diese Email.</p>", "text/html");
        Transport.send(message);
    }
}
