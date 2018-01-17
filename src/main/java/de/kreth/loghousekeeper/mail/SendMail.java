package de.kreth.loghousekeeper.mail;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.config.Configuration;
import de.kreth.loghousekeeper.reactions.Reaction;

public class SendMail implements Reaction {

   private static final Logger logger = LoggerFactory.getLogger(SendMail.class);
   
   private Session session;
   private Properties mailProperties;

   private String user;

   public void init(Configuration config) {
      mailProperties = config.getMailProperties();
      String password = mailProperties.getProperty("mail.password");
      user = mailProperties.getProperty("mail.user");
      mailProperties.remove("mail.password");
      mailProperties.remove("mail.user");
      MailAuthenticator passwordAuthentication = new MailAuthenticator(user, password);
      this.session = Session.getInstance(mailProperties, passwordAuthentication);
      if(logger.isTraceEnabled()) {
         enableDebug();
      }
   }
   
   private void enableDebug() {
      session.setDebug(true);
      PrintStream out = new PrintStream(new OutputStream() {
         StringWriter writer = new StringWriter();
         
         @Override
         public void write(int b) throws IOException {
            writer.write(b);
            if('\n' == (char)b) {
               flush();
            }
         }
         
         @Override
         public void flush() throws IOException {
            super.flush();
            writer.flush();
            logger.debug(writer.toString());
            writer = new StringWriter();
         }
      });
      session.setDebugOut(out);
   }

   @Override
   public void react(File f, String... cause) {

      if(logger.isDebugEnabled()) {
         logger.debug("Sending Mail for " + f.getAbsolutePath() + " because " + Arrays.toString(cause));
      }
      
      try {
         MimeMessage msg = prepareMail(cause);
         
         StringBuilder text = new StringBuilder("Warning!\n");
         text.append(f.getAbsolutePath()).append(Arrays.toString(cause));
         msg.setText(text.toString());
         
         Transport.send(msg);
      } catch (MessagingException e) {
         logger.error("unable to send message for File " + f.getAbsolutePath() + " with cause " + Arrays.toString(cause), e);
      }
   }

   private MimeMessage prepareMail(String[] cause) throws MessagingException {

      String to = mailProperties.getProperty("mail.to");
      String subject = mailProperties.getProperty("mail.subject", "File " + Arrays.toString(cause));

      MimeMessage msg = new MimeMessage(session);
      msg.setRecipients(Message.RecipientType.TO,to);
      msg.setSubject(subject);
      msg.setSentDate(new Date());
      msg.setFrom(user);
      return msg;
   }

   private class MailAuthenticator  extends Authenticator {
      private final String user;

      private final String password;

      /**
       * Der Konstruktor erzeugt ein MailAuthenticator Objekt<br>
       * aus den beiden Parametern user und passwort.
       *
       * @param user
       *            String, der Username fuer den Mailaccount.
       * @param password
       *            String, das Passwort fuer den Mailaccount.
       */
      public MailAuthenticator(String user, String password) {
          this.user = user;
          this.password = password;
      }

      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
         return new PasswordAuthentication(this.user, this.password);
      }
   }
}
