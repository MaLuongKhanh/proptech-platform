package vn.proptech.payment.config;

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

    @Value("${wallet.rabbitmq.exchange}")
    private String walletExchange;
    
    @Value("${wallet.rabbitmq.queues.wallet-created}")
    private String walletCreatedQueue;
    
    @Value("${wallet.rabbitmq.queues.wallet-updated}")
    private String walletUpdatedQueue;
    
    @Value("${wallet.rabbitmq.queues.wallet-deleted}")
    private String walletDeletedQueue;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-created}")
    private String walletCreatedRoutingKey;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-updated}")
    private String walletUpdatedRoutingKey;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-deleted}")
    private String walletDeletedRoutingKey;
    
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
    public TopicExchange walletExchange() {
        return new TopicExchange(walletExchange);
    }
    
    @Bean
    public Queue walletCreatedQueue() {
        return new Queue(walletCreatedQueue, true);
    }
    
    @Bean
    public Queue walletUpdatedQueue() {
        return new Queue(walletUpdatedQueue, true);
    }
    
    @Bean
    public Queue walletDeletedQueue() {
        return new Queue(walletDeletedQueue, true);
    }
    
    @Bean
    public Binding walletCreatedBinding() {
        return BindingBuilder
                .bind(walletCreatedQueue())
                .to(walletExchange())
                .with(walletCreatedRoutingKey);
    }
    
    @Bean
    public Binding walletUpdatedBinding() {
        return BindingBuilder
                .bind(walletUpdatedQueue())
                .to(walletExchange())
                .with(walletUpdatedRoutingKey);
    }
    
    @Bean
    public Binding walletDeletedBinding() {
        return BindingBuilder
                .bind(walletDeletedQueue())
                .to(walletExchange())
                .with(walletDeletedRoutingKey);
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