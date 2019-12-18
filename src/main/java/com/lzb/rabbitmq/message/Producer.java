package com.lzb.rabbitmq.message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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

        //声明
        String exchangeName = "test_message_exchange";
        String routingKey = "routingKey1";


        Map<String,Object> headers = new HashMap<>();
        headers.put("my1","111");
        headers.put("my2","222");
        //发送一个带有附加属性的消息
        AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                .deliveryMode(2)    //常用的模式
                .contentEncoding("UTF-8")
                .expiration("10000")    //过期时间
                .headers(headers)       //自定义属性
                .build();
        //4.通过channel发送数据
        /**
        * exchange:交换机名称，作用：接收消息，并根据路由键转发消息所绑定的队列
         * routtingKey
         * */
        for (int i=0;i<100000;i++){
            String msg = "body：消息实体，Hello RabbitMQ";
            channel.basicPublish(exchangeName,routingKey,properties,msg.getBytes());
            System.out.println("生产端发送消息："+msg);
        }

        //5.必须要关闭相关连接
        channel.close();
        connection.close();
    }
}
