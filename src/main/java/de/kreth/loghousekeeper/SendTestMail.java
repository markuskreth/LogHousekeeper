package de.kreth.loghousekeeper;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;

import de.kreth.loghousekeeper.config.Configuration;
import de.kreth.loghousekeeper.mail.SendMail;

public class SendTestMail {

   public static void main(String[] args) {

      DocumentFactory df = DocumentFactory.getInstance();
      Document doc = df.createDocument("UTF-8");
      doc.setRootElement(df.createElement("configurations"));
      Configuration config = Configuration.from(doc);
      SendMail m = new SendMail(config);
      m.react(new File("."), "Testmail");

   }

}
