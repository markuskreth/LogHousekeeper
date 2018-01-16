package de.kreth.loghousekeeper;

import java.io.*;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import de.kreth.loghousekeeper.jobtypes.DeleteJob;

public class XmlCreator {

   public static void main(String[] args) throws DocumentException, IOException {

      DeleteJob job = new DeleteJob(new File("/opt/tomcat8/logs/"), "catalina*.log", 5, 5);
      
      File xml = new File("housekeeper.xml");
      Document doc = getDocument(xml);

      job.appendTo(doc.getRootElement());

      Element root = doc.getRootElement();

      Properties mailProperties = new Properties();
      mailProperties.put("mail.smtp.host", "smtp.web.de");
      mailProperties.put("mail.smtp.port", "587");
      mailProperties.put("mail.smtp.auth", "true");
      mailProperties.put("mail.smtp.starttls.enable", "true");
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      mailProperties.storeToXML(out, null);
      ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
      SAXReader reader = new SAXReader();
      root.add(reader.read(in).getRootElement());
      writeDoc(xml, doc);
   }

   private static void writeDoc(File xml, Document doc) throws IOException, DocumentException {
      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setEncoding(doc.getXMLEncoding());
      Writer out = new FileWriter(xml);

      XMLWriter writer = new XMLWriter(out, format);
      // XMLWriter has a bug that is avoided if we reparse the document
      // prior to calling XMLWriter.write()
      writer.write(DocumentHelper.parseText(doc.asXML()));
      writer.close();
   }

   private static Document getDocument(File xml) throws DocumentException {
      Document doc;
      if (xml.exists()) {
         SAXReader xmlReader = new SAXReader();
         doc = xmlReader.read(xml);
      } else {
         DocumentFactory df = DocumentFactory.getInstance();
         doc = df.createDocument("UTF-8");
//         doc.setRootElement(df.createElement("configurations"));
      }
      return doc;
   }

}
