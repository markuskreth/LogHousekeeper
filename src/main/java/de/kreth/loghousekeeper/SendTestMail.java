package de.kreth.loghousekeeper;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import de.kreth.loghousekeeper.config.Configuration;
import de.kreth.loghousekeeper.mail.SendMail;

public class SendTestMail {

   public static void main(String[] args) throws Exception {

      SAXReader saxBuilder = new SAXReader();
      Document xmlConfig = saxBuilder.read(new File("housekeeper.xml"));
      Configuration conf = Configuration.from(xmlConfig);
      
      SendMail m = new SendMail();
      m.init(conf);
      m.react(new File("."), "Testmail");

   }

}
