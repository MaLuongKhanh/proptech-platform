package vn.proptech.payment.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.proptech.payment.domain.model.Wallet;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class WalletEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${wallet.rabbitmq.exchange}")
    private String walletExchange;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-created}")
    private String walletCreatedRoutingKey;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-updated}")
    private String walletUpdatedRoutingKey;
    
    @Value("${wallet.rabbitmq.routing-keys.wallet-deleted}")
    private String walletDeletedRoutingKey;
    
    public void publishWalletCreatedEvent(Wallet wallet) {
        log.info("Publishing wallet created event for wallet id: {}", wallet.getId());
        rabbitTemplate.convertAndSend(walletExchange, walletCreatedRoutingKey, wallet);
    }
    
    public void publishWalletUpdatedEvent(Wallet wallet) {
        log.info("Publishing wallet updated event for wallet id: {}", wallet.getId());
        rabbitTemplate.convertAndSend(walletExchange, walletUpdatedRoutingKey, wallet);
    }
    
    public void publishWalletDeletedEvent(Wallet wallet) {
        log.info("Publishing wallet deleted event for wallet id: {}", wallet.getId());
        rabbitTemplate.convertAndSend(walletExchange, walletDeletedRoutingKey, wallet);
    }
}