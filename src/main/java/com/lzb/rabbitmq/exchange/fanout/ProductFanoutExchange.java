package com.lzb.rabbitmq.exchange.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by luzhibin on 2019/12/18 18:40
 */
public class ProductFanoutExchange {
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
        String exchangeName = "test_fanout_exchange";

        for (int i=0;i<10;i++){
            //5.发送
            String msg = "Hello RabbitMQ,FANOUT Exchange Message ....";
            channel.basicPublish(exchangeName,"",null,msg.getBytes());
        }

        channel.close();
        connection.close();
    }
}
