package com.lzb.rabbitmq.start;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by luzhibin on 2019/12/18 14:42
 */
/*生产者*/
public class Producer {

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

        //4.通过channel发送数据
        /**
        * exchange:交换机名称，作用：接收消息，并根据路由键转发消息所绑定的队列
         * routtingKey
         * */
        for (int i=0;i<100;i++){
            String msg = "body：消息实体，Hello RabbitMQ";
            channel.basicPublish("","routingKey001",null,msg.getBytes());
            System.out.println("生产端发送消息："+msg);
        }

        //5.必须要关闭相关连接
        channel.close();
        connection.close();


    }
}
