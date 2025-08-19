package se.example.review.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.example.api.core.review.Review;
import se.example.api.core.review.ReviewService;
import se.example.api.exception.InvalidInputException;
import se.example.review.mapper.ReviewMapper;
import se.example.review.persistence.ReviewEntity;
import se.example.review.persistence.ReviewRepository;
import se.example.util.http.ServiceUtil;

@RestController
public class ReviewServiceImpl implements ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ReviewServiceImpl.class);

  private final ReviewRepository reviewRepository;

  private final ServiceUtil serviceUtil;

  @Autowired
  public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
    this.serviceUtil = serviceUtil;
  }

  @Override
  public Flux<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    LOG.debug("Will get reviews for product with productId: {}", productId);
    Flux<ReviewEntity> reviewEntities = reviewRepository.findByProductId(productId);
    Flux<Review> reviews = reviewEntities.map(ReviewMapper::mapToReview)
        .map(this::setServiceAddress);
    return reviews;
  }

  @Override
  public Mono<Review> createReview(Review body) {
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    if (body.getReviewId() < 1) {
      throw new InvalidInputException("Invalid reviewId: " + body.getReviewId());
    }
    ReviewEntity entity = ReviewMapper.mapToEntity(body);
    Mono<ReviewEntity> newEntity = reviewRepository.save(entity);
    LOG.debug("createReview: created a review entity: {}/{}", body.getProductId(), body.getReviewId());
    return newEntity.map(ReviewMapper::mapToReview)
        .map(this::setServiceAddress)
        .onErrorMap(DuplicateKeyException.class, ex -> new InvalidInputException(
            "Duplicate key, Product Id: " + body.getProductId() + ", Review Id:" + body.getReviewId()));
  }

  @Override
  public Mono<Void> deleteReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    LOG.debug("deleteReviews: tries to delete reviews for product with productId: {}", productId);
    return reviewRepository.deleteAll(reviewRepository.findByProductId(productId));

  }

  private Review setServiceAddress(Review e) {
    e.setServiceAddress(serviceUtil.getServiceAddress());
    return e;
  }

}
