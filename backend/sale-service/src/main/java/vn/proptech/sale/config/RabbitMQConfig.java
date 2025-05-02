package vn.proptech.sale.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange names
    public static final String SALE_EXCHANGE = "sale.exchange";
    public static final String LISTING_EXCHANGE = "listing.exchange";
    
    // Queue names
    public static final String TRANSACTION_CREATED_QUEUE = "transaction.created.queue";
    public static final String TRANSACTION_UPDATED_QUEUE = "transaction.updated.queue";
    public static final String LISTING_UPDATED_QUEUE = "listing.updated.queue";
    
    // Routing keys
    public static final String TRANSACTION_CREATED_KEY = "transaction.created";
    public static final String TRANSACTION_UPDATED_KEY = "transaction.updated";
    public static final String LISTING_UPDATED_KEY = "listing.updated";

    @Bean
    public TopicExchange saleExchange() {
        return new TopicExchange(SALE_EXCHANGE);
    }

    @Bean
    public TopicExchange listingExchange() {
        return new TopicExchange(LISTING_EXCHANGE);
    }

    @Bean
    public Queue transactionCreatedQueue() {
        return new Queue(TRANSACTION_CREATED_QUEUE, true);
    }

    @Bean
    public Queue transactionUpdatedQueue() {
        return new Queue(TRANSACTION_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue listingUpdatedQueue() {
        return new Queue(LISTING_UPDATED_QUEUE, true);
    }

    @Bean
    public Binding transactionCreatedBinding() {
        return BindingBuilder
                .bind(transactionCreatedQueue())
                .to(saleExchange())
                .with(TRANSACTION_CREATED_KEY);
    }

    @Bean
    public Binding transactionUpdatedBinding() {
        return BindingBuilder
                .bind(transactionUpdatedQueue())
                .to(saleExchange())
                .with(TRANSACTION_UPDATED_KEY);
    }

    @Bean
    public Binding listingUpdatedBinding() {
        return BindingBuilder
                .bind(listingUpdatedQueue())
                .to(listingExchange())
                .with(LISTING_UPDATED_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}