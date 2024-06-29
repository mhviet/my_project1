package com.viet.bookmanagement.ibm.mq;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XMLBuilder {
    public static void main(String[] args) {
        generateXML("message content1", "updated");
    }

    public static String generateXML(String messageContent, String result) {
//        String messageContent = "message content1";
//        String result = "updated";

        String xmlString ="";

        try {
            // Create a new DocumentBuilderFactory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            // Root element
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            // Add namespace declaration
            rootElement.setAttribute("xmlns", "http://www.example.com/xmlns");

            // Create type element
            Element type = doc.createElement("type");
            type.appendChild(doc.createTextNode("TypeValue"));
            rootElement.appendChild(type);

            // Create message element
            Element message = doc.createElement("message");
            message.appendChild(doc.createTextNode(messageContent));
            rootElement.appendChild(message);

            // Create result element
            Element resultElement = doc.createElement("result");
            resultElement.appendChild(doc.createTextNode(result));
            rootElement.appendChild(resultElement);

            // Create dateSent element
            Element dateSent = doc.createElement("dateSent");
            // Get current date
            LocalDate currentDate = LocalDate.now();
            // Format date as desired
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            dateSent.appendChild(doc.createTextNode(formatter.format(currentDate)));
            rootElement.appendChild(dateSent);

            // Convert the XML document to a string
            StringWriter writer = new StringWriter();
            TransformerFactory tfactory = TransformerFactory.newInstance();
            Transformer transformer = tfactory.newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            xmlString = writer.getBuffer().toString();

            // Output XML to console
            System.out.println("Generated XML:");
            System.out.println(xmlString);




        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlString;

    }
}
