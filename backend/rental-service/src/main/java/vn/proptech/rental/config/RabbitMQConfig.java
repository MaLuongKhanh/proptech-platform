package vn.proptech.rental.config;

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

    @Value("${contract.rabbitmq.exchange}")
    private String contractExchange;
    
    @Value("${contract.rabbitmq.queues.contract-created}")
    private String contractCreatedQueue;
    
    @Value("${contract.rabbitmq.queues.contract-updated}")
    private String contractUpdatedQueue;
    
    @Value("${contract.rabbitmq.queues.contract-deleted}")
    private String contractDeletedQueue;
    
    @Value("${contract.rabbitmq.routing-keys.contract-created}")
    private String contractCreatedRoutingKey;
    
    @Value("${contract.rabbitmq.routing-keys.contract-updated}")
    private String contractUpdatedRoutingKey;
    
    @Value("${contract.rabbitmq.routing-keys.contract-deleted}")
    private String contractDeletedRoutingKey;
    
    @Value("${transaction.rabbitmq.exchange}")
    private String transactionExchange;

    @Value("${transaction.rabbitmq.queues.transaction-created}")
    private String transactionCreatedQueue;

    @Value("${transaction.rabbitmq.queues.transaction-updated}")
    private String transactionUpdatedQueue;

    @Value("${transaction.rabbitmq.routing-keys.transaction-created}")
    private String transactionCreatedRoutingKey;

    @Value("${transaction.rabbitmq.routing-keys.transaction-updated}")
    private String transactionUpdatedRoutingKey;

    @Value("${transaction.rabbitmq.routing-keys.transaction-deleted}")
    private String transactionDeletedRoutingKey;

    @Value("${transaction.rabbitmq.queues.transaction-deleted}")
    private String transactionDeletedQueue;

    @Bean
    public TopicExchange contractExchange() {
        return new TopicExchange(contractExchange);
    }
    
    @Bean
    public Queue contractCreatedQueue() {
        return new Queue(contractCreatedQueue, true);
    }
    
    @Bean
    public Queue contractUpdatedQueue() {
        return new Queue(contractUpdatedQueue, true);
    }
    
    @Bean
    public Queue contractDeletedQueue() {
        return new Queue(contractDeletedQueue, true);
    }
    
    @Bean
    public Binding contractCreatedBinding() {
        return BindingBuilder
                .bind(contractCreatedQueue())
                .to(contractExchange())
                .with(contractCreatedRoutingKey);
    }
    
    @Bean
    public Binding contractUpdatedBinding() {
        return BindingBuilder
                .bind(contractUpdatedQueue())
                .to(contractExchange())
                .with(contractUpdatedRoutingKey);
    }
    
    @Bean
    public Binding contractDeletedBinding() {
        return BindingBuilder
                .bind(contractDeletedQueue())
                .to(contractExchange())
                .with(contractDeletedRoutingKey);
    }

    @Bean
    public TopicExchange transactionExchange() {
        return new TopicExchange(transactionExchange);
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(transactionCreatedQueue, true);
    }

    @Bean
    public Queue transactionUpdatedQueue() {
        return new Queue(transactionUpdatedQueue, true);
    }

    @Bean
    public Queue transactionDeletedQueue() {
        return new Queue(transactionDeletedQueue, true);
    }

    @Bean
    public Binding transactionCreatedBinding() {
        return BindingBuilder
                .bind(transactionCreatedQueue())
                .to(transactionExchange())
                .with(transactionCreatedRoutingKey);
    }

    @Bean
    public Binding transactionUpdatedBinding() {
        return BindingBuilder
                .bind(transactionUpdatedQueue())
                .to(transactionExchange())
                .with(transactionUpdatedRoutingKey);
    }

    @Bean
    public Binding transactionDeletedBinding() {
        return BindingBuilder
                .bind(transactionDeletedQueue())
                .to(transactionExchange())
                .with(transactionDeletedRoutingKey);
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