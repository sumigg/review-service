package se.example.review.services;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.example.api.core.review.Review;
import se.example.api.core.review.ReviewService;
import se.example.api.event.Event;
import se.example.api.exception.EventProcessingException;

@Configuration
public class MessageProcessorConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessorConfig.class);

    private final ReviewService reviewService;

    @Autowired
    public MessageProcessorConfig(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Bean
    Consumer<Event<Integer, Review>> messageProcessor() {
        return event -> {
            LOGGER.debug("Process event: {}", event);
            switch (event.getEventType()) {
                case CREATE:
                    Review review = event.getData();
                    LOGGER.debug("Create review with ID: {}", review.getProductId());
                    reviewService.createReview(review).block();
                    break;
                case DELETE:
                    Integer productId = event.getKey();
                    LOGGER.debug("Delete review with ID: {}", productId);
                    reviewService.deleteReviews(productId).block();
                    break;
                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType()
                            + ", expected a CREATE or DELETE event";
                    LOGGER.warn(errorMessage);
                    throw new EventProcessingException(errorMessage);
            }
            LOGGER.debug("Event processed successfully: {}", event);
        };
    }

}
