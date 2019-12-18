package com.lzb.rabbitmq.exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by luzhibin on 2019/12/18 16:43
 */
public class ProducerTopicExchange {
    public static void main(String[] args) throws IOException, TimeoutException {
        //1.创建一个connectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2.通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //3.通过connection创建一个channel
        Channel channel = connection.createChannel();
        //4.声明
        String exchangeName = "test_topic_exchange";
        String routingKey1 = "user.save";
        String routingKey2 = "user.update";
        String routingKey3 = "user.delete.abc";

        //5.发送
        String msg = "Hello RabbitMQ,Topic Exchange Message ....";
        //参数：exchange名称，routingKey路由键，属性：props，body：消息主体
        channel.basicPublish(exchangeName,routingKey1,null,msg.getBytes());
        channel.basicPublish(exchangeName,routingKey2,null,msg.getBytes());
        channel.basicPublish(exchangeName,routingKey3,null,msg.getBytes());

        //关闭相关连接
        channel.close();
        connection.close();
    }
}
