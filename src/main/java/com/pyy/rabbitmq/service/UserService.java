package com.pyy.rabbitmq.service;

import com.pyy.rabbitmq.model.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2018/7/8 0008.
 */
@Service
public class UserService {

    /**
     * 自动监听消息队列：pyy.news
     * @param user
     */
    @RabbitListener(queues = {"pyy.news"})
    public void receive(User user) {
        System.out.println("收到消息："+user);
    }
    //结果： 收到消息：User{username='张三', password='12312321'}

    /**
     * 自动监听消息队列：pyy.news
     * @param message
     */
    @RabbitListener(queues = {"pyy"})
    public void receive(Message message) {
        System.out.println("消息头："+message.getMessageProperties());
        System.out.println("消息体："+message.getBody());
    }

}
