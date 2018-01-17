package de.kreth.loghousekeeper.config;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.kreth.loghousekeeper.jobtypes.AbstractJob;
import de.kreth.loghousekeeper.mail.Encrytion;
import de.kreth.loghousekeeper.mail.SendMail;

public class Configuration {

   private final static Logger logger = LoggerFactory.getLogger(Configuration.class);

   private final List<AbstractJob> jobs = new ArrayList<>();

   private Properties mailProperties;

   public Collection<AbstractJob> getJobs() {
      return jobs;
   }

   public static Configuration from(Document xmlConfig) {
      
      Element root = xmlConfig.getRootElement();
      List<Element> list = root.elements();
      Configuration config = new Configuration();

      config.mailProperties = new Properties();

      SendMail m = new SendMail();
      
      for (Element e : list) {
         if (e.getName().equals("mail_properties")) {
            parseEntriesToProps(e, config.mailProperties);
         } else {
            AbstractJob job = AbstractJob.parse(e);
            for(Element react: e.element("reactions").elements("reaction")) {
               if(SendMail.class.getName().equals(react.getText())) {
                  job.add(m);
               }
            }
            config.jobs.add(job);
         }
      }

      m.init(config);
      
      if (logger.isInfoEnabled()) {
         logger.info("Found " + config.jobs.size() + " jobs in configuration file");
      }
      return config;
   }

   private static void parseEntriesToProps(Element e, Properties mailProperties2) {
      for (Element entry : e.elements("entry")) {

         String key = entry.attribute("key").getText();
         String value = entry.getText();
         try {
            if (key.contains("password")) {
               value = Encrytion.decrypt(value);
            }
            mailProperties2.put(key, value);
         } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e1) {
            logger.warn("Unable to decrypt password for mail settings", e);
         }
      }
   }

   public Properties getMailProperties() {
      return mailProperties;
   }
}
