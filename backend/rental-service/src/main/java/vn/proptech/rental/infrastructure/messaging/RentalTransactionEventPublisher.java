package vn.proptech.rental.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import vn.proptech.rental.domain.model.RentalTransaction;

@Component
@RequiredArgsConstructor
@Slf4j
public class RentalTransactionEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${transaction.rabbitmq.exchange}")
    private String transactionExchange;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-created}")
    private String transactionCreatedRoutingKey;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-updated}")
    private String transactionUpdatedRoutingKey;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-deleted}")
    private String transactionDeletedRoutingKey;
    
    public void publishRentalTransactionCreatedEvent(RentalTransaction transaction) {
        log.info("Publishing transaction created event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionCreatedRoutingKey, transaction);
    }
    
    public void publishRentalTransactionUpdatedEvent(RentalTransaction transaction) {
        log.info("Publishing transaction updated event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionUpdatedRoutingKey, transaction);
    }
    
    public void publishRentalTransactionDeletedEvent(RentalTransaction transaction) {
        log.info("Publishing transaction deleted event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionDeletedRoutingKey, transaction);
    }
} 