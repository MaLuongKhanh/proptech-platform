package vn.proptech.sale.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.proptech.sale.domain.model.Contract;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${contract.rabbitmq.exchange}")
    private String contractExchange;
    
    @Value("${contract.rabbitmq.routing-keys.contract-created}")
    private String contractCreatedRoutingKey;
    
    @Value("${contract.rabbitmq.routing-keys.contract-updated}")
    private String contractUpdatedRoutingKey;
    
    @Value("${contract.rabbitmq.routing-keys.contract-deleted}")
    private String contractDeletedRoutingKey;
    
    public void publishContractCreatedEvent(Contract contract) {
        log.info("Publishing contract created event for contract id: {}", contract.getId());
        rabbitTemplate.convertAndSend(contractExchange, contractCreatedRoutingKey, contract);
    }
    
    public void publishContractUpdatedEvent(Contract contract) {
        log.info("Publishing contract updated event for contract id: {}", contract.getId());
        rabbitTemplate.convertAndSend(contractExchange, contractUpdatedRoutingKey, contract);
    }
    
    public void publishContractDeletedEvent(Contract contract) {
        log.info("Publishing contract deleted event for contract id: {}", contract.getId());
        rabbitTemplate.convertAndSend(contractExchange, contractDeletedRoutingKey, contract);
    }
} 