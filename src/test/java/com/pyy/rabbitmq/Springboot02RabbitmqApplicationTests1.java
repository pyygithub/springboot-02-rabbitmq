package com.pyy.rabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Springboot02RabbitmqApplicationTests1 {

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	AmqpAdmin amqpAdmin;

	/**
	 * 创建交换机：exchange
	 */
	@Test
	public void createExchange() {
		amqpAdmin.declareExchange(new DirectExchange("amqpadmin.DirectExchange"));

		amqpAdmin.declareExchange(new FanoutExchange("amqpadmin.FanoutExchange"));

		amqpAdmin.declareExchange(new TopicExchange("amqpadmin.TopicExchange"));

		System.out.println("创建完毕");
	}

	/**
	 * 创建队列：queye
	 */
	@Test
	public void createQueue() {
		amqpAdmin.declareQueue(new Queue("amqpadmin.queue", true));

		System.out.println("创建完毕");
	}

	/**
	 * 创建绑定规则：binding
	 */
	@Test
	public void createBinding() {
		/**
		 String destination: 目的地
		 String exchange： 交换机
		 String routingKey： 路由键
		 Map<String, Object> arguments：
		 Binding.DestinationType destinationType:目的地类型
		 */
		amqpAdmin.declareBinding(new Binding("amqpadmin.queue", Binding.DestinationType.QUEUE, "amqpadmin.DirectExchange", "amqp.haha", null));

		System.out.println("创建完毕");
	}



}
