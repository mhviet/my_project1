package com.viet.bookmanagement.ibm.mq;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MQSender {
//    public static void main(String[] args) throws MQException, IOException {
//        MQQueueManager queueManager = new MQQueueManager("QM1");
//        MQQueue queue = queueManager.accessQueue("DEV.QUEUE.1", MQConstants.MQOO_OUTPUT);
//        MQMessage message = new MQMessage();
//        message.writeString("Hello, IBM MQ!");
//        queue.put(message);
//        queue.close();
//        queueManager.disconnect();
//    }

    public static void main(String[] args) {
        String message = "message2";
        String xmlMessageRes = XMLBuilder.generateXML(message, "updated");
        sendToQueue("DEV.QUEUE.2", "QM1", xmlMessageRes);
    }

    public static void sendToQueue(String queueName, String queueManagerName, String messageContent) {
//        String queueName = "DEV.QUEUE.1";
        MQQueueManager queueManager = null;
        MQQueue queue = null;

        try {
            // Connect to the queue manager
            queueManager = new MQQueueManager(queueManagerName);

            // Access the queue for putting messages
            //MQConstants.MQOO_OUTPUT : it typically means that the application has the intention to put messages into the queue
            queue = queueManager.accessQueue(queueName, MQConstants.MQOO_OUTPUT);

            // Create a new message
            MQMessage message = new MQMessage();
            message.writeString(messageContent);

            // Put the message in the queue
            queue.put(message);

//            // Create a new message
//            MQMessage message1 = new MQMessage();
//            message1.writeString("Hello, IBM MQ1!");
//            queue.put(message1);

            log.info("Message successfully put in the queue.");
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
    }
}
