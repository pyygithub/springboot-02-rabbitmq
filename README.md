## 　&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 一、概述
![](https://upload-images.jianshu.io/upload_images/11464886-7110ab2ccbe538c5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-8c4179e09f7bdad4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-98af3642885041fd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-8ab58dd7c3174ca5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-ed24be3e08126cd0.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-c1e0af888901ee56.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-26468a218caf3895.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-f005f9069ebe24bf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/11464886-7c885b2c07706935.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/11464886-10f3b69500b77bc5.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-e1ee9523c072b8bd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/11464886-6bdc5a31eccdf51a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; 四、SpringBoot+RabbitMQ整合

1. 使用docker安装RabbitMQ
```shell
1）、使用docker pull 下载rabbitmq
[root@localhost ~]# docker pull registry.docker-cn.com/library/rabbitmq:3-management
3-management: Pulling from library/rabbitmq
683abbb4ea60: Already exists 
30a58d97bcb5: Pull complete 
...
```
2. 启动rabbitMQ
```
[root@localhost ~]# docker images
REPOSITORY                                TAG                 IMAGE ID            CREATED             SIZE
registry.docker-cn.com/library/redis      latest              71a81cb279e3        9 days ago          83.4MB
registry.docker-cn.com/library/rabbitmq   3-management        500d74765467        9 days ago          149MB
mysql                                     5.7                 66bc0f66b7af        9 days ago          372MB
[root@localhost ~]# docker run -d -p 5672:5672 -p 15672:15672 --name myrabbitmq 500d74765467
7599303175cb42287d0f58c0b9d0db67070199670cad4f680f6348e41d6e2240
```
3. 在浏览器输入：http://主机地址:15672 进入rabbitMQ登录页面
  ![](https://upload-images.jianshu.io/upload_images/11464886-288dd77779406529.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

4. 输入默认用户名：guest/guest 进入管理界面
  ![](https://upload-images.jianshu.io/upload_images/11464886-0ce16e54768f0345.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![](https://upload-images.jianshu.io/upload_images/11464886-a5c02724ad22b150.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-2df2af2ddb717f80.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![](https://upload-images.jianshu.io/upload_images/11464886-21ddf3b67ce28384.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

5. 搭建springboot+rabbit工程
```
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```
引入spring-boot-starter-amqp包后springboot帮我们自动配置：
1. 执行RabbitAutoConfiguration
2. 自动配置类会帮我们自动配置连接工厂、RabbitTemplate（给RabbitMQ发送和接受消息）、AmqpAdmin（RabbitMQ系统管理功能组件）
```java
 @Configuration
    @ConditionalOnMissingBean({ConnectionFactory.class})
    protected static class RabbitConnectionFactoryCreator {
        protected RabbitConnectionFactoryCreator() {
        }

        @Bean
        public CachingConnectionFactory rabbitConnectionFactory(RabbitProperties config) throws Exception {
            RabbitConnectionFactoryBean factory = new RabbitConnectionFactoryBean();
            if(config.determineHost() != null) {
                factory.setHost(config.determineHost());
            }

            factory.setPort(config.determinePort());
            if(config.determineUsername() != null) {
                factory.setUsername(config.determineUsername());
            }

            if(config.determinePassword() != null) {
                factory.setPassword(config.determinePassword());
            }

            if(config.determineVirtualHost() != null) {
                factory.setVirtualHost(config.determineVirtualHost());
            }

            if(config.getRequestedHeartbeat() != null) {
                factory.setRequestedHeartbeat(config.getRequestedHeartbeat().intValue());
            }

            Ssl ssl = config.getSsl();
            if(ssl.isEnabled()) {
                factory.setUseSSL(true);
                if(ssl.getAlgorithm() != null) {
                    factory.setSslAlgorithm(ssl.getAlgorithm());
                }

                factory.setKeyStore(ssl.getKeyStore());
                factory.setKeyStorePassphrase(ssl.getKeyStorePassword());
                factory.setTrustStore(ssl.getTrustStore());
                factory.setTrustStorePassphrase(ssl.getTrustStorePassword());
            }

            if(config.getConnectionTimeout() != null) {
                factory.setConnectionTimeout(config.getConnectionTimeout().intValue());
            }

            factory.afterPropertiesSet();
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory((com.rabbitmq.client.ConnectionFactory)factory.getObject());
            connectionFactory.setAddresses(config.determineAddresses());
            connectionFactory.setPublisherConfirms(config.isPublisherConfirms());
            connectionFactory.setPublisherReturns(config.isPublisherReturns());
            if(config.getCache().getChannel().getSize() != null) {
                connectionFactory.setChannelCacheSize(config.getCache().getChannel().getSize().intValue());
            }

            if(config.getCache().getConnection().getMode() != null) {
                connectionFactory.setCacheMode(config.getCache().getConnection().getMode());
            }

            if(config.getCache().getConnection().getSize() != null) {
                connectionFactory.setConnectionCacheSize(config.getCache().getConnection().getSize().intValue());
            }

            if(config.getCache().getChannel().getCheckoutTimeout() != null) {
                connectionFactory.setChannelCheckoutTimeout(config.getCache().getChannel().getCheckoutTimeout().longValue());
            }

            return connectionFactory;
        }
    }
-------------------------------------------------------------------------------------
 @Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        @ConditionalOnMissingBean({RabbitTemplate.class})
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
            MessageConverter messageConverter = (MessageConverter)this.messageConverter.getIfUnique();
            if(messageConverter != null) {
                rabbitTemplate.setMessageConverter(messageConverter);
            }

            rabbitTemplate.setMandatory(this.determineMandatoryFlag());
            Template templateProperties = this.properties.getTemplate();
            Retry retryProperties = templateProperties.getRetry();
            if(retryProperties.isEnabled()) {
                rabbitTemplate.setRetryTemplate(this.createRetryTemplate(retryProperties));
            }

            if(templateProperties.getReceiveTimeout() != null) {
                rabbitTemplate.setReceiveTimeout(templateProperties.getReceiveTimeout().longValue());
            }

            if(templateProperties.getReplyTimeout() != null) {
                rabbitTemplate.setReplyTimeout(templateProperties.getReplyTimeout().longValue());
            }

            return rabbitTemplate;
        }
------------------------------------------------------------------------------------------------
@Bean
        @ConditionalOnSingleCandidate(ConnectionFactory.class)
        @ConditionalOnProperty(
            prefix = "spring.rabbitmq",
            name = {"dynamic"},
            matchIfMissing = true
        )
        @ConditionalOnMissingBean({AmqpAdmin.class})
        public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
            return new RabbitAdmin(connectionFactory);
        }

```
3. RabbitProperties 封装了RabbitMQ的所有配置
```java
@ConfigurationProperties(
    prefix = "spring.rabbitmq"
)
public class RabbitProperties {
    private String host = "localhost";
    private int port = 5672;
    private String username;
    private String password;
    private final RabbitProperties.Ssl ssl = new RabbitProperties.Ssl();
    private String virtualHost;
    private String addresses;
    private Integer requestedHeartbeat;
    private boolean publisherConfirms;
    private boolean publisherReturns;
    private Integer connectionTimeout;
    private final RabbitProperties.Cache cache = new RabbitProperties.Cache();
    private final RabbitProperties.Listener listener = new RabbitProperties.Listener();
    private final RabbitProperties.Template template = new RabbitProperties.Template();
    private List<RabbitProperties.Address> parsedAddresses;
...
```
4. application.properties配置
```properties
spring.rabbitmq.host=192.168.43.53
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```
5. 编写测试类
- 自定义MessageConverter
```java
package com.pyy.rabbitmq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Administrator on 2018/7/7 0007.
 */
@Configuration
public class MyAMQPConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

```
- Exchange.direct：点对点（单播模式）

发送消息：
```java
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
```
![](https://upload-images.jianshu.io/upload_images/11464886-5c23e04150bcce9d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
只有Queue：pyy.news 该消息队列接收到消息，其他队列不能接收到该消息。
消息内容如下：
![](https://upload-images.jianshu.io/upload_images/11464886-a9bafd4ddff748bf.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
接受消息：
```java
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
```
结果：
```
  class com.pyy.rabbitmq.User
  com.pyy.rabbitmq.User@4c27d39d
  张三
  12312321
```
- Exchange.fanout：广播模式
```
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
```
**注意：fanout模式路由键不用指定，所有绑定到这个交换机的消息队列都能接受到该消息**
![](https://upload-images.jianshu.io/upload_images/11464886-6952efbc838a740f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-e6befa5fdeab7c66.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- Exchange.topic 主题模式
```java
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
```
![](https://upload-images.jianshu.io/upload_images/11464886-acb99499b90ec107.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-4abf61e37868d95d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
--------------------
####SpringBoot高级-消息-@RabbitListener&@EnableRabbit监听
1. 开启RabbitListener注解支持
```
@EnableRabbit // 开启基于注解的RabbitMQ
@SpringBootApplication
public class Springboot02RabbitmqApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot02RabbitmqApplication.class, args);
	}
}
```
2. 使用@RabbitListener注解完成消息接收
```java
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
```
系统会自动监听指定名称的消息队列，只有有消息自动消费。

-------
AmqpAdmin 管理rabbitmq相关操作：
```
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
```
![](https://upload-images.jianshu.io/upload_images/11464886-20a628362c3f99d2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)


![](https://upload-images.jianshu.io/upload_images/11464886-09a2012e3c7690e2.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![](https://upload-images.jianshu.io/upload_images/11464886-612e7916ac84fa29.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)