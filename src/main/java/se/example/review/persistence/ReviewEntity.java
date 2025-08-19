
package se.example.review.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;


@Table(name = "reviews" )
public class ReviewEntity {

    @Id 
    private int id;
    @Version
    private int version;
    private int reviewId;
    private int productId;

    private String author;
    private String content;
    private String subject;

    public ReviewEntity() {
    }

    public ReviewEntity(int reviewId, int productId, String author, String content,
            String subject) {
   
        this.reviewId = reviewId;
        this.productId = productId;
        this.author = author;
        this.content = content;
        this.subject = subject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
