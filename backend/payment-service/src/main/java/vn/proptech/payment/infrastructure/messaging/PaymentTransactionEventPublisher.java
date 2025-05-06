package vn.proptech.payment.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import vn.proptech.payment.domain.model.PaymentTransaction;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentTransactionEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${transaction.rabbitmq.exchange}")
    private String transactionExchange;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-created}")
    private String transactionCreatedRoutingKey;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-updated}")
    private String transactionUpdatedRoutingKey;
    
    @Value("${transaction.rabbitmq.routing-keys.transaction-deleted}")
    private String transactionDeletedRoutingKey;
    
    public void publishPaymentTransactionCreatedEvent(PaymentTransaction transaction) {
        log.info("Publishing transaction created event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionCreatedRoutingKey, transaction);
    }
    
    public void publishPaymentTransactionUpdatedEvent(PaymentTransaction transaction) {
        log.info("Publishing transaction updated event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionUpdatedRoutingKey, transaction);
    }
    
    public void publishPaymentTransactionDeletedEvent(PaymentTransaction transaction) {
        log.info("Publishing transaction deleted event for transaction id: {}", transaction.getId());
        rabbitTemplate.convertAndSend(transactionExchange, transactionDeletedRoutingKey, transaction);
    }
} 