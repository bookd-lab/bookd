package bookdlab.bookd.models;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class Review {

    private String reviewTitle;
    private String reviewerImgUrl; //change later to User.imageUrl?

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
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

    private String reviewBody;
    private int starRating;

}
