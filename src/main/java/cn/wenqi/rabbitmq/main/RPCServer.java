package cn.wenqi.rabbitmq.main;

import com.rabbitmq.client.*;

import static cn.wenqi.rabbitmq.util.MessageUtil.fib;

/**
 * @author wenqi
 */
public class RPCServer {
    private static final String RPC_QUEUE_NAME = "rpc_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection=connectionFactory.newConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(RPC_QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(RPC_QUEUE_NAME, false, consumer);
        System.out.println(" [x] Awaiting RPC requests");

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();

            AMQP.BasicProperties props = delivery.getProperties();
            AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(props.getCorrelationId())
                    .build();

            String message = new String(delivery.getBody());
            int n = Integer.parseInt(message);

            System.out.println(" [.] fib(" + message + ")");
            String response = "" + fib(n);

            channel.basicPublish( "", props.getReplyTo(), replyProps, response.getBytes());

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }
    }
}
