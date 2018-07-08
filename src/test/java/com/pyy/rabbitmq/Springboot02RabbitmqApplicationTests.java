package com.pyy.rabbitmq;

import com.pyy.rabbitmq.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02RabbitmqApplicationTests {

	@Autowired
	RabbitTemplate rabbitTemplate;


	/**
	 * 1、单播（点对点）
	 */
	@Test
	public void direct() {
		//Message需要自定构造一个:定义消息体内容和消息头：
		//rabbitTemplate.send(exchange, routeKey, message);

		// object默认当成消息体，只需要传入需要发送的内容，自动序列化发送给rabbitmq
		// rabbitTemplate.convertAndSend(exchange, routeKey, object);
		User user = new User();
		user.setUsername("张三");
		user.setPassword("12312321");

		// msg 对象被默认（SimpleMessageConverter -- byte[]）序列化后发送出去

		// 通过自定义：MessageConverter 实现JSON序列化
		rabbitTemplate.convertAndSend("exchange.direct", "pyy.news", user);
	}

	/**
	 * 2、广播
	 */
	@Test
	public void fanout() {
		//Message需要自定构造一个:定义消息体内容和消息头：
		//rabbitTemplate.send(exchange, routeKey, message);

		// object默认当成消息体，只需要传入需要发送的内容，自动序列化发送给rabbitmq
		// rabbitTemplate.convertAndSend(exchange, routeKey, object);
		User user = new User();
		user.setUsername("李四");
		user.setPassword("123456");

		// fanout模式路由键不用指定，所有绑定到这个交换机的消息队列都能接受到该消息
		rabbitTemplate.convertAndSend("exchange.fanout", "", user);
	}

	/**
	 * 3、topic
	 */
	@Test
	public void topic() {
		//Message需要自定构造一个:定义消息体内容和消息头：
		//rabbitTemplate.send(exchange, routeKey, message);

		// object默认当成消息体，只需要传入需要发送的内容，自动序列化发送给rabbitmq
		// rabbitTemplate.convertAndSend(exchange, routeKey, object);
		User user = new User();
		user.setUsername("王五");
		user.setPassword("123456");

		// topic模式路由键只有和exchange绑定的路由键规则匹配，对应的消息队列就能收到消息
		rabbitTemplate.convertAndSend("exchange.topic", "*.news", user);
	}

	/**
	 * 接受数据
	 */
	@Test
	public void receve() {
		Object obj = rabbitTemplate.receiveAndConvert("pyy.news");
		System.out.println(obj.getClass());
		System.out.println(obj);

		if(obj instanceof  User){
			User u = (User) obj;
			System.out.println(u.getUsername());
			System.out.println(u.getPassword());
		}

	}

}
