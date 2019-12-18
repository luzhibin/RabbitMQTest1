package com.lzb.rabbitmq.message;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by luzhibin on 2019/12/18 14:42
 */
/*消费者*/
public class Consumer {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        //1.创建一个connectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3.通过connection创建一个channel
        Channel channel = connection.createChannel();

        //声明
        String exchangeName = "test_message_exchange";
        String exchangeType = "direct";
        String queueName = "test_message_queue";
        String routingKey = "routingKey1";


        //声明交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        //4.声明（创建）一个队列
        /**
        queue:队列名称，durable：是否持久化（如果为true，服务器即使重启，队列也不会消失）
        * exclusive：是否独占队列有两个作用，一：当连接关闭时connection.close()该队列是否会自动删除；二：该队列是否是私有的private，如果不是排外的，可以使用两个消费者都访问同一个队列，没有任何问题，如果是排外的，会对当前队列加锁，其他通道channel是不能访问的，如果强制访问会报异常
        * autoDelete：是否自动删除队列，当最后一个消费者断开连接之后队列是否自动被删除
        * args：扩展参数，目前一般为null
        * */
        channel.queueDeclare(queueName,true,false,false,null);
        //建立交换机和队列的绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);
        //5.创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        //6.设置channel
        /**
         * queue:队列名称
         * autoAck 是否自动确认消息
         * callBack：具体的消费者对象
         */
        channel.basicConsume(queueName,true,queueingConsumer);

        while (true){
            //7.获取消息
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();   //收不到消息会一直阻塞，可以设置timeout超时时长
            String msg = new String(delivery.getBody());
            Map<String,Object> headers = delivery.getProperties().getHeaders();
            System.out.println("get message properties headers"+headers.get("my1"));
            System.out.println("消费端："+msg);
            //Envelope envelope = delivery.getEnvelope();
        }

    }
}
