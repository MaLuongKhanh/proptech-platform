package vn.proptech.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${user.rabbitmq.exchange}")
    private String exchange;

    @Value("${user.rabbitmq.queues.user-created}")
    private String userCreatedQueue;

    @Value("${user.rabbitmq.queues.user-updated}")
    private String userUpdatedQueue;

    @Value("${user.rabbitmq.queues.user-deleted}")
    private String userDeletedQueue;

    @Value("${user.rabbitmq.routing-keys.user-created}")
    private String userCreatedRoutingKey;

    @Value("${user.rabbitmq.routing-keys.user-updated}")
    private String userUpdatedRoutingKey;

    @Value("${user.rabbitmq.routing-keys.user-deleted}")
    private String userDeletedRoutingKey;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue userCreatedQueue() {
        return new Queue(userCreatedQueue, true);
    }

    @Bean
    public Queue userUpdatedQueue() {
        return new Queue(userUpdatedQueue, true);
    }

    @Bean
    public Queue userDeletedQueue() {
        return new Queue(userDeletedQueue, true);
    }

    @Bean
    public Binding userCreatedBinding() {
        return BindingBuilder
                .bind(userCreatedQueue())
                .to(exchange())
                .with(userCreatedRoutingKey);
    }

    @Bean
    public Binding userUpdatedBinding() {
        return BindingBuilder
                .bind(userUpdatedQueue())
                .to(exchange())
                .with(userUpdatedRoutingKey);
    }

    @Bean
    public Binding userDeletedBinding() {
        return BindingBuilder
                .bind(userDeletedQueue())
                .to(exchange())
                .with(userDeletedRoutingKey);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }
}