package bookdlab.bookd.api;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.models.Review;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewsClient {

    //TODO: Mock data
    public List<Review> getReviews() {

        List<Review> reviewsList = new ArrayList<>();

        reviewsList.add(genReview());
        reviewsList.add(genReview());
        reviewsList.add(genReview());
        reviewsList.add(genReview());
        reviewsList.add(genReview());

        return reviewsList;
    }

    private Review genReview() {
        Review review = new Review();
        review.setReviewBody("These guys are the best DJs. I would hire them again in a heartbeat!");
        review.setReviewDate("July 2016");
        review.setReviewerImgUrl("http://a1.files.biography.com/image/upload/c_fit,cs_srgb,dpr_1.0,h_1200,q_80,w_1200/MTE4MDAzNDEwNzQzMTY2NDc4.jpg");
        review.setReviewerId("Will Smith");
        review.setStarRating(4.0);
        return review;
    }
}
