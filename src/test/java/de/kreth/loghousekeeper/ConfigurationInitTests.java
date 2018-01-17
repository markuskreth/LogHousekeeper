package de.kreth.loghousekeeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringReader;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import de.kreth.loghousekeeper.config.Configuration;
import de.kreth.loghousekeeper.jobtypes.AbstractJob;

public class ConfigurationInitTests {

   @Test
   public void test() throws DocumentException {

      SAXReader saxBuilder = new SAXReader();
      Document xmlConfig = saxBuilder.read(new StringReader(xml));
      Configuration conf = Configuration.from(xmlConfig);
      assertEquals(5, conf.getJobs().size());
      for(AbstractJob j: conf.getJobs()) {
         assertTrue("No reaction configured in " + j.getClass().getName() + ", " + j, j.getReactions().size()>0);
      }
   }
   
   String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
         "\n" + 
         "<configurations>\n" + 
         "   <de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "      <path>/opt/tomcat8/logs</path>\n" + 
         "      <filePattern>catalina*.log</filePattern>\n" + 
         "      <maxSize>5</maxSize>\n" + 
         "      <maxAgeInDays>5</maxAgeInDays>\n" + 
         "      <reactions>\n" + 
         "         <reaction>de.kreth.loghousekeeper.reactions.DeleteFile</reaction>\n" + 
         "      </reactions>\n" + 
         "   </de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "   <de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "      <path>/opt/tomcat8/logs</path>\n" + 
         "      <filePattern>MeineLogDatei.log*</filePattern>\n" + 
         "      <maxSize>5</maxSize>\n" + 
         "      <maxAgeInDays>10</maxAgeInDays>\n" + 
         "      <reactions>\n" + 
         "         <reaction>de.kreth.loghousekeeper.reactions.DeleteFile</reaction>\n" + 
         "      </reactions>\n" + 
         "   </de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "   <de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "      <path>/opt/tomcat8/logs</path>\n" + 
         "      <filePattern>manager.*.log</filePattern>\n" + 
         "      <maxSize>5</maxSize>\n" + 
         "      <maxAgeInDays>10</maxAgeInDays>\n" + 
         "      <reactions>\n" + 
         "         <reaction>de.kreth.loghousekeeper.reactions.DeleteFile</reaction>\n" + 
         "      </reactions>\n" + 
         "   </de.kreth.loghousekeeper.jobtypes.DeleteJob>\n" + 
         "   <de.kreth.loghousekeeper.jobtypes.CheckSizeJob>\n" + 
         "      <warnSize>52428800</warnSize>\n" + 
         "      <file>catalina.out</file>\n" + 
         "      <reactions>\n" + 
         "         <reaction>de.kreth.loghousekeeper.mail.SendMail</reaction>\n" + 
         "      </reactions>\n" + 
         "   </de.kreth.loghousekeeper.jobtypes.CheckSizeJob>\n" + 
         "   <de.kreth.loghousekeeper.jobtypes.CheckSizeJob>\n" + 
         "      <warnSize>314572800</warnSize>\n" + 
         "      <file>catalina.out</file>\n" + 
         "      <reactions>\n" + 
         "         <reaction>de.kreth.loghousekeeper.mail.SendMail</reaction>\n" + 
         "      </reactions>\n" + 
         "   </de.kreth.loghousekeeper.jobtypes.CheckSizeJob>\n" + 
         "   <mail_properties>\n" + 
         "      <entry key=\"mail.smtp.starttls.enable\">true</entry>\n" + 
         "      <entry key=\"mail.smtp.port\">587</entry>\n" + 
         "      <entry key=\"mail.password\">encryptedPassword</entry>\n" + 
         "      <entry key=\"mail.to\">test@test.de</entry>\n" + 
         "      <entry key=\"mail.user\">test@test.de</entry>\n" + 
         "      <entry key=\"mail.smtp.auth\">true</entry>\n" + 
         "      <entry key=\"mail.smtp.host\">smtp.web.de</entry>\n" + 
         "   </mail_properties>\n" + 
         "</configurations>\n" + 
         "";
}
