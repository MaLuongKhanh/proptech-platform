package vn.proptech.security.infrastructure.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.proptech.security.domain.model.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    
    @Value("${user.rabbitmq.exchange}")
    private String exchange;
    
    @Value("${user.rabbitmq.routing-keys.user-created}")
    private String userCreatedRoutingKey;
    
    @Value("${user.rabbitmq.routing-keys.user-updated}")
    private String userUpdatedRoutingKey;
    
    @Value("${user.rabbitmq.routing-keys.user-deleted}")
    private String userDeletedRoutingKey;
    
    public void publishUserCreatedEvent(User user) {
        Map<String, Object> message = createUserMessage(user);
        message.put("eventType", "USER_CREATED");
        message.put("timestamp", Instant.now().toString());
        
        log.info("Publishing user created event for user: {}", user.getUsername());
        rabbitTemplate.convertAndSend(exchange, userCreatedRoutingKey, message);
    }
    
    public void publishUserUpdatedEvent(User user) {
        Map<String, Object> message = createUserMessage(user);
        message.put("eventType", "USER_UPDATED");
        message.put("timestamp", Instant.now().toString());
        
        log.info("Publishing user updated event for user: {}", user.getUsername());
        rabbitTemplate.convertAndSend(exchange, userUpdatedRoutingKey, message);
    }
    
    public void publishUserDeletedEvent(User user) {
        Map<String, Object> message = createUserMessage(user);
        message.put("eventType", "USER_DELETED");
        message.put("timestamp", Instant.now().toString());
        
        log.info("Publishing user deleted event for user: {}", user.getUsername());
        rabbitTemplate.convertAndSend(exchange, userDeletedRoutingKey, message);
    }
    
    private Map<String, Object> createUserMessage(User user) {
        Map<String, Object> message = new HashMap<>();
        message.put("id", user.getId());
        message.put("username", user.getUsername());
        message.put("email", user.getEmail());
        message.put("fullName", user.getFullName());
        message.put("roles", user.getRoles());
        message.put("enabled", user.isEnabled());
        return message;
    }
}