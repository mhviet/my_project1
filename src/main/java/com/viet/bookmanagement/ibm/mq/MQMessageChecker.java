package com.viet.bookmanagement.ibm.mq;
import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
public class MQMessageChecker {
    public static void main(String[] args) {
        String queueName = "DEV.QUEUE.1";
        MQQueueManager queueManager = null;
        MQQueue queue = null;

        try {
            // Connect to the queue manager
            queueManager = new MQQueueManager("QM1");

            // Access the queue for browsing
            queue = queueManager.accessQueue(queueName, MQConstants.MQOO_BROWSE | MQConstants.MQOO_INQUIRE);

            // Check if there are messages in the queue
            int depth = queue.getCurrentDepth();
            if (depth > 0) {
                System.out.println("Messages exist in the queue.");
            } else {
                System.out.println("No messages in the queue.");
            }
        } catch (MQException mqe) {
            mqe.printStackTrace();
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
