package vn.proptech.listing.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.proptech.listing.domain.model.Listing;

@Component
@RequiredArgsConstructor
@Slf4j
public class ListingEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${listing.rabbitmq.exchange}")
    private String listingExchange;
    
    @Value("${listing.rabbitmq.routing-keys.listing-created}")
    private String listingCreatedRoutingKey;
    
    @Value("${listing.rabbitmq.routing-keys.listing-updated}")
    private String listingUpdatedRoutingKey;
    
    @Value("${listing.rabbitmq.routing-keys.listing-deleted}")
    private String listingDeletedRoutingKey;
    
    public void publishListingCreatedEvent(Listing listing) {
        log.info("Publishing listing created event for listing id: {}", listing.getId());
        rabbitTemplate.convertAndSend(listingExchange, listingCreatedRoutingKey, listing);
    }
    
    public void publishListingUpdatedEvent(Listing listing) {
        log.info("Publishing listing updated event for listing id: {}", listing.getId());
        rabbitTemplate.convertAndSend(listingExchange, listingUpdatedRoutingKey, listing);
    }
    
    public void publishListingDeletedEvent(Listing listing) {
        log.info("Publishing listing deleted event for listing id: {}", listing.getId());
        rabbitTemplate.convertAndSend(listingExchange, listingDeletedRoutingKey, listing);
    }
} 