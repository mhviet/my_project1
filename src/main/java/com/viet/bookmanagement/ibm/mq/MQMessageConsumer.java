package com.viet.bookmanagement.ibm.mq;
import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import com.viet.bookmanagement.entities.QueueAuditLog;
import org.thymeleaf.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.viet.bookmanagement.ibm.mq.MQSender.sendToQueue;

public class MQMessageConsumer {
    public static void main(String[] args) {
        initListener("DEV.QUEUE.1", "QM1");
    }

    public static QueueAuditLog initListener(String queueName, String queueManagerName) {
//        String queueName = "DEV.QUEUE.1";
        MQQueueManager queueManager = null;
        MQQueue queue = null;
        QueueAuditLog queueAuditLog = null;

        try {
            // Connect to the queue manager
            queueManager = new MQQueueManager(queueManagerName);

            // Access the queue for getting messages
            // MQConstants.MQOO_INPUT_AS_Q_DEF : open for input operation like reading message from queue
            queue = queueManager.accessQueue(queueName, MQConstants.MQOO_INPUT_AS_Q_DEF);

//            // Create a new message object
//            MQMessage message = new MQMessage();

            // Set options for getting the message
            MQGetMessageOptions getMessageOptions = new MQGetMessageOptions();
            // MQConstants.MQGMO_WAIT: application should wait for a message to become available if no message is immediately available on the queue
            // MQConstants.MQGMO_FAIL_IF_QUIESCING: should fail if the queue manager is in quiescing state (a state in which it's shutting down).
            getMessageOptions.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
            getMessageOptions.waitInterval = MQConstants.MQWI_UNLIMITED;

            // Loop to read all messages from the queue
            while (true) {
                // Create a new message object
                MQMessage message = new MQMessage();

                // Get the message from the queue
                try {
                    queue.get(message, getMessageOptions);
                } catch (MQException mqe) {
                    // Break out of the loop if there are no more messages or if an error occurs
                    if (mqe.reasonCode == MQConstants.MQRC_NO_MSG_AVAILABLE) {
                        System.out.println("No more messages in the queue.");
                        break;
                    } else {
                        throw mqe;
                    }
                }

                // Read the message content
                int length = message.getDataLength();
                String text = message.readString(length);

                System.out.println("Received message: " + text);

                if (!StringUtils.isEmpty(text)) {
                    queueAuditLog = mapFromXmlString(text);

                    // If processing is successful, acknowledge the message
                    if (queueAuditLog != null) {
                        // processing logic here
                        System.out.println("Message processed and acknowledged: "+ text);
                        // Acknowledge the message
                        queueManager.commit();
                    }
                }

            }

//            // Get the message from the queue
//            queue.get(message, getMessageOptions);
//
//            // Read the message content, have to write the exact length of the string that we sent earlier
//            int length = message.getDataLength();
//            String text = message.readString(length);
////            String text = message.readUTF();
//            System.out.println("Received message: " + text);


        } catch (MQException mqe) {
            mqe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the queue and disconnect from the queue manager
            try {
                if (queue != null) {
                    queue.close();
                }
                if (queueManager != null) {
                    queueManager.disconnect();
                }
            } catch (MQException mqe) {
                mqe.printStackTrace();
            }
        }
        return queueAuditLog;

    }

    public static QueueAuditLog mapFromXmlString(String xmlString) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // Enable namespace awareness
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlString.getBytes()));

            Element rootElement = doc.getDocumentElement();
            String type = getTextContent(rootElement, "type"); // Adjust namespace prefix
            String message = getTextContent(rootElement, "message"); // Adjust namespace prefix
            String result = getTextContent(rootElement, "result"); // Adjust namespace prefix
            // DateTimeFormatter.ISO_LOCAL_DATE : format "yyyy-MM-dd"
            LocalDate dateSent = LocalDate.parse(getTextContent(rootElement, "dateSent"), DateTimeFormatter.ISO_LOCAL_DATE); // Adjust namespace prefix

            return new QueueAuditLog(type, message, result, dateSent.atStartOfDay());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getTextContent(Element rootElement, String tagName) {
        Node node = rootElement.getElementsByTagName(tagName).item(0);
        return node != null ? node.getTextContent() : null;
    }
}
