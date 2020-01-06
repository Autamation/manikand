package com.dalrada.dashboard.file.upload;

import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendAttachmentInEmail {
        
        public static void sendEmail(String fromemail, String toemail, String subject, String body, String filepath) {
                   MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
               mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
               mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
               mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
               mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
               mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
               
               // Additional elements to make DSN work
               mc.addMailcap("multipart/report;;  x-java-content-handler=com.sun.mail.dsn.multipart_report");
               mc.addMailcap("message/delivery-status;; x-java-content-handler=com.sun.mail.dsn.message_deliverystatus");
               mc.addMailcap("message/disposition-notification;; x-java-content-handler=com.sun.mail.dsn.message_dispositionnotification");
               mc.addMailcap("text/rfc822-headers;;   x-java-content-handler=com.sun.mail.dsn.text_rfc822headers");
               
              // Recipient's email ID needs to be mentioned.
              String to = toemail;

              // Sender's email ID needs to be mentioned
              String from = fromemail;

              final String username = fromemail;//change accordingly
              //final String username = "crowdfundasia@gmail.com";
              final String password = "Prakat123";//change accordingly 
              
              //String text = "<p>You have recieved this mail, Because you have Subscrided to QuickDaan NEWSLETTER click here to</p> <a href=\"http://127.0.0.1:8080/web/bindu/news-letter-unsubscribe?email= "+to+" > UNSUBSCRIBE</a>";

              // Assuming you are sending email through relay.jangosmtp.net
              String host = "smtp.gmail.com";

              Properties props = new Properties();
              props.put("mail.smtp.auth", "true");
              props.put("mail.smtp.starttls.enable", "true");
              props.put("mail.smtp.host", host);
              props.put("mail.smtp.port", "587");

              // Get the Session object.
              Session session = Session.getInstance(props,
                 new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                       return new PasswordAuthentication(username, password);
                    }
                 });

              try {
                 // Create a default MimeMessage object.
                 Message message = new MimeMessage(session);

                 // Set From: header field of the header.
                 message.setFrom(new InternetAddress(from));

                 // Set To: header field of the header.
                 message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

                 // Set Subject: header field
                 message.setSubject(subject);
                 
                 

                 // Create the message part
                 BodyPart messageBodyPart = new MimeBodyPart();

                 // Now set the actual message
                 messageBodyPart.setText(body);

                 // Create a multipar message
                 Multipart multipart = new MimeMultipart();

                 // Set text message part
                 multipart.addBodyPart(messageBodyPart);

                 // Part two is attachment
                 messageBodyPart = new MimeBodyPart();
                 String filename = filepath;
                 //String filename = "C://Users//Prakat-Intern//Desktop//button css.txt";
                 DataSource source = new FileDataSource(filename);
                 messageBodyPart.setDataHandler(new DataHandler(source));
                 messageBodyPart.setFileName("ReconcileReport.xlsx");
                 multipart.addBodyPart(messageBodyPart);
                 
                 

                 // Send the complete message parts
                 message.setContent(multipart);

                 // Send message
                 Transport.send(message);

                 System.out.println("Sent message successfully....");
          
              } catch (MessagingException e) {
                // throw new RuntimeException(e);
            	  e.printStackTrace();
              }
           }

        
}
