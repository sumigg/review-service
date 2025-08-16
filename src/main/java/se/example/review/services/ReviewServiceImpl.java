package se.example.review.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
  public List<Review> getReviews(int productId) {

    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    LOG.debug("Will get reviews for product with productId: {}", productId);
       List<ReviewEntity> reviewEntities = reviewRepository.findByProductId(productId);
    if (reviewEntities == null || reviewEntities.isEmpty()) {
      LOG.debug("No reviews found for productId: {}", productId);
      return new ArrayList<>();
    }   
    LOG.debug("Reviews found for productId: {}", productId);
    List<Review> reviews = reviewEntities.stream().map(ReviewMapper::mapToReview).toList();
    reviews.forEach(r -> r.setServiceAddress(serviceUtil.getServiceAddress()));
    LOG.debug("/reviews response size: {}", reviews.size());
    return reviews;
  }   


  @Override
  public Review createReview(Review body) {
    if (body.getProductId() < 1) {
      throw new InvalidInputException("Invalid productId: " + body.getProductId());
    }
    if (body.getReviewId() < 1) {
      throw new InvalidInputException("Invalid reviewId: " + body.getReviewId());
    } 

    LOG.debug("createReview: tries to create a review for product with productId: {}", body.getProductId());
    ReviewEntity entity = ReviewMapper.mapToEntity(body);
    ReviewEntity newEntity = reviewRepository.save(entity);
    LOG.debug("createReview: created a review with reviewId: {}", newEntity.getReviewId()); 
    body = ReviewMapper.mapToReview(newEntity);
    body.setServiceAddress(serviceUtil.getServiceAddress());
    LOG.debug("createReview: response body: {}", body); 
    return body;
  }

  @Override
  public void deleteReviews(int productId) {
    if (productId < 1) {
      throw new InvalidInputException("Invalid productId: " + productId);
    }
    reviewRepository.deleteAll(reviewRepository.findByProductId(productId));
    LOG.debug("deleteReviews: deleted reviews for product with productId: {}", productId);
  }

}
