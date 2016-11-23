package cn.wenqi.rabbitmq.main;

import cn.wenqi.rabbitmq.util.MessageUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * @author wenqi
 */
public class Send {
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        /**
         * In the Java client, when we supply no parameters to queueDeclare() we create a non-durable,
         * exclusive, autodelete queue with a generated name:
         */
        String randomQueueName=channel.queueDeclare().getQueue();//

        channel.exchangeDeclare("logs","fanout");
        channel.queueBind(randomQueueName,"logs","");
        String message = MessageUtil.getMessage(new String[]{"Hello world","A","111","vvvvvv","ccccccc"});
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        channel.close();
        connection.close();
    }



}
