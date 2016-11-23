package cn.wenqi.rabbitmq.main;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * @author wenqi
 */
public class RPCClient {

    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private QueueingConsumer consumer;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        replyQueueName = channel.queueDeclare().getQueue();

        consumer = new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName, true, consumer);
    }

    public String call(String message) throws Exception {
        String response = null;
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        channel.basicPublish("", requestQueueName, props, message.getBytes());

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws Exception {
        connection.close();
    }

    public static void main(String[] args) throws Exception {
        RPCClient fibonacciRpc = new RPCClient();

        System.out.print(new Date()+ "：");
        Scanner scanner=new Scanner(System.in);
        String message = scanner.next();
        System.out.println("发送消息："+message);
        fibonacciRpc.call(message);
        fibonacciRpc.close();
    }
}
