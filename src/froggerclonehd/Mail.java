 
package froggerclonehd;

/**
 *
 * @author Kevin Berg, Jan Olschewski, Niels Oldenburg
 */

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
    
    /**
     * Konstruktor
     * @param name
     * @param receiver
     * @param points 
     */
   public Mail(String name, String receiver, int points){
      String to = receiver; //Empfänger der E-Mail

      String from = "Froggerclone_HD"; //Absendername
      
      //Logindaten
      final String username = "froggerclonehd@gmail.com"; 
      final String password = "frogger1234";

      /**
       * G-mailserverconfig
       */
      String host = "smtp.gmail.com";
      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password); //Benutzerauthetifizierung.
         }
      });

      try {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(from)); //Absender
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));
         message.setSubject(name + "'s erreichte Punktzahl bei Froggerclone HD"); //Betreff
         message.setText("Mein neuer Rekord bei Froggerclone HD ist " + points + ". Schaffst du das auch?"); //Inhalt
         Transport.send(message); //Nachricht abschicken
      } catch (MessagingException e) {
            System.err.println("Die E-Mail konnte dem Empfänger nicht mitgeteilt werden.");
            throw new RuntimeException(e);
      }
   }
}