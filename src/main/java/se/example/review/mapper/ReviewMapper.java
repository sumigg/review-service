package se.example.review.mapper;

import se.example.api.core.review.Review;
import se.example.review.persistence.ReviewEntity;

public class ReviewMapper {

    public static Review mapToReview(ReviewEntity entity) {

        if (entity == null) {
            return null;
        }
        return new Review(
                entity.getReviewId(),
                entity.getProductId(),
                entity.getAuthor(),
                entity.getContent(),
                entity.getSubject(),
                null);
    }

    public static ReviewEntity mapToEntity(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewEntity(0,
                0,
                review.getReviewId(),
                review.getProductId(),
                review.getAuthor(),
                review.getContent(),
                review.getSubject());
    }

}
