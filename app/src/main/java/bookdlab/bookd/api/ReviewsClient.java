package bookdlab.bookd.api;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.models.Review;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewsClient {

    //TODO: Mock data
    public List<Review> getMyReviews() {

        List<Review> reviewsList = new ArrayList<>();

        reviewsList.add(genReview("#4286f4"));
        return reviewsList;
    }

    private Review genReview(String color) {
        Review review = new Review();
        return review;
    }
}
