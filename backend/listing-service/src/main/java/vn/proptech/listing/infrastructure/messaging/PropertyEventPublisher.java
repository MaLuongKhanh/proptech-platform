package vn.proptech.listing.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.proptech.listing.domain.model.Property;

@Component
@RequiredArgsConstructor
@Slf4j
public class PropertyEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${property.rabbitmq.exchange}")
    private String propertyExchange;

    @Value("${property.rabbitmq.routing-keys.property-created}")
    private String propertyCreatedRoutingKey;

    @Value("${property.rabbitmq.routing-keys.property-updated}")
    private String propertyUpdatedRoutingKey;

    @Value("${property.rabbitmq.routing-keys.property-deleted}")
    private String propertyDeletedRoutingKey;

    public void publishPropertyCreatedEvent(Property property) {
        log.info("Publishing property created event for property id: {}", property.getId());
        rabbitTemplate.convertAndSend(propertyExchange, propertyCreatedRoutingKey, property);
    }

    public void publishPropertyUpdatedEvent(Property property) {
        log.info("Publishing property updated event for property id: {}", property.getId());
        rabbitTemplate.convertAndSend(propertyExchange, propertyUpdatedRoutingKey, property);
    }

    public void publishPropertyDeletedEvent(Property property) {
        log.info("Publishing property deleted event for property id: {}", property.getId());
        rabbitTemplate.convertAndSend(propertyExchange, propertyDeletedRoutingKey, property);
    }
}
