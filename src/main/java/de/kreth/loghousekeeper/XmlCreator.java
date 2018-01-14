package de.kreth.loghousekeeper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlCreator {

	public static void main(String[] args) throws DocumentException, IOException {

		Job job = new Job(new File("/opt/tomcat8/logs/"), "catalina*.log", 5, 5);
		File xml = new File("housekeeper.xml");
		Document doc;
		if(xml.exists()) {

			SAXReader xmlReader = new SAXReader();
			doc = xmlReader.read(xml);
		} else {
		    DocumentFactory df = DocumentFactory.getInstance();
		    doc = df.createDocument("UTF-8");
		    doc.setRootElement(df.createElement("configurations"));
		}
		job.appendTo(doc.getRootElement());

	    OutputFormat format = OutputFormat.createPrettyPrint();
	    format.setEncoding(doc.getXMLEncoding());
	    Writer out = new FileWriter(xml);
	    
	    XMLWriter writer = new XMLWriter( out, format );
	    // XMLWriter has a bug that is avoided if we reparse the document
	    // prior to calling XMLWriter.write()
	    writer.write(DocumentHelper.parseText(doc.asXML()));
	    writer.close();
	}
}
