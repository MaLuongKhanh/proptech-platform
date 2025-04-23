package vn.proptech.listing.config;

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

    @Value("${listing.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${listing.rabbitmq.queues.listing-created}")
    private String listingCreatedQueue;
    
    @Value("${listing.rabbitmq.queues.listing-updated}")
    private String listingUpdatedQueue;
    
    @Value("${listing.rabbitmq.queues.listing-deleted}")
    private String listingDeletedQueue;
    
    @Value("${listing.rabbitmq.routing-keys.listing-created}")
    private String listingCreatedRoutingKey;
    
    @Value("${listing.rabbitmq.routing-keys.listing-updated}")
    private String listingUpdatedRoutingKey;
    
    @Value("${listing.rabbitmq.routing-keys.listing-deleted}")
    private String listingDeletedRoutingKey;
    
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }
    
    @Bean
    public Queue listingCreatedQueue() {
        return new Queue(listingCreatedQueue, true);
    }
    
    @Bean
    public Queue listingUpdatedQueue() {
        return new Queue(listingUpdatedQueue, true);
    }
    
    @Bean
    public Queue listingDeletedQueue() {
        return new Queue(listingDeletedQueue, true);
    }
    
    @Bean
    public Binding listingCreatedBinding() {
        return BindingBuilder
                .bind(listingCreatedQueue())
                .to(exchange())
                .with(listingCreatedRoutingKey);
    }
    
    @Bean
    public Binding listingUpdatedBinding() {
        return BindingBuilder
                .bind(listingUpdatedQueue())
                .to(exchange())
                .with(listingUpdatedRoutingKey);
    }
    
    @Bean
    public Binding listingDeletedBinding() {
        return BindingBuilder
                .bind(listingDeletedQueue())
                .to(exchange())
                .with(listingDeletedRoutingKey);
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