package bookdlab.bookd.models;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class Review {

    private String reviewerName;
    private String reviewerImgUrl; //change later to User.imageUrl?
    private String reviewBody;
    private String reviewDate;
    private int starRating;

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
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

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }


}
