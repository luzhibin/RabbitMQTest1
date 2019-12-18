package com.lzb.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by luzhibin on 2019/12/18 16:30
 */
public class ConsumerTopicExchange {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        connectionFactory.setAutomaticRecoveryEnabled(true);    //是否自动重连
        connectionFactory.setNetworkRecoveryInterval(300);      //重连时间
        //2.通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3.通过connection创建一个channel
        Channel channel = connection.createChannel();

        //4.声明
        String exchangeName = "test_topic_exchange";
        String exchangeType = "topic";
        String queueName = "test_topic_queue";
        String routingKey = "user.*";

        //声明交换机
        channel.exchangeDeclare(exchangeName,exchangeType,true,false,false,null);
        //声明队列
        channel.queueDeclare(queueName,false,false,false,null);
        //建立交换机和队列的绑定关系
        channel.queueBind(queueName,exchangeName,routingKey);
        //new一个消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //参数：队列名称，是否自动ACK，消费者名称
        channel.basicConsume(queueName,true,queueingConsumer);

        //循环获取消息
        while (true){
            //获取消息，如果没有消息，将会一直阻塞
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("收到消息："+msg);
        }
    }
}
