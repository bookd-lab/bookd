package bookdlab.bookd.api;

import java.util.List;

import bookdlab.bookd.models.Business;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by akhmedovi on 11/26/16.
 * Copyright - 2016
 */

public interface BookdApiClient {

    @GET("/business")
    Call<List<Business>> getBusinesses(
            @Query("price") int priceMax,
            @Query("rating") double ratingMin,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sort") String sortBy
    );
}
