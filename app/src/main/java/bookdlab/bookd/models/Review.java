package bookdlab.bookd.models;

import org.parceler.Parcel;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

@Parcel
public class Review {

    String id;                      // Unique ID
    String businessId;              // Which business is this created for
    String reviewerId;              // User ID who created this. We can fetch user using this
    String reviewerImgUrl;          // change later to User.imageUrl?
    String reviewBody;              // Review itself
    String reviewDate;              // When the review was created
    Double starRating;              // Rating for the review

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(String reviewerId) {
        this.reviewerId = reviewerId;
    }

    public String getReviewerImgUrl() {
        return reviewerImgUrl;
    }

    public void setReviewerImgUrl(String reviewerImgUrl) {
        this.reviewerImgUrl = reviewerImgUrl;
    }

    public String getReviewBody() {
        return reviewBody;
    }

    public void setReviewBody(String reviewBody) {
        this.reviewBody = reviewBody;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Double getStarRating() {
        return starRating;
    }

    public void setStarRating(Double starRating) {
        this.starRating = starRating;
    }
}
