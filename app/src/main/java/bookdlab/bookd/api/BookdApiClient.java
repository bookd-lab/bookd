package bookdlab.bookd.api;

import java.util.List;

import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by akhmedovi on 11/26/16.
 * Copyright - 2016
 */

public interface BookdApiClient {

    @GET("/business")
    Call<List<Business>> getBusinesses(
            @Query("q") String searchKey,
            @Query("price") int priceMax,
            @Query("rating") double ratingMin,
            @Query("page") int page,
            @Query("limit") int limit,
            @Query("sort") String sortBy
    );

    @GET("/event")
    Call<List<Event>> getEvents(
            @Query("creator") String creator,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @POST("/event")
    Call<Event> saveEvent(@Body Event event);
}
