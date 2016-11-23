package cn.wenqi.rabbitmq.main;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author wenqi
 */
public class EmitLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv)
            throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        String routingKey="kern.a";

        String message = "This is Message";// ,"";

        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
        System.out.println(" [x] Sent '" + routingKey + "':'" + message + "'");

        connection.close();
    }
}
