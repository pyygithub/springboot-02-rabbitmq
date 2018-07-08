package com.pyy.rabbitmq;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit // 开启基于注解的RabbitMQ
@SpringBootApplication
public class Springboot02RabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot02RabbitmqApplication.class, args);
	}
}
