package bookdlab.bookd.api;

import java.util.List;

import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.Favorite.Favorite;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("/businessByIds")
    Call<List<Business>> getBusinessesByIds(
            @Query("ids") String ids
    );

    @GET("/event")
    Call<List<Event>> getEvents(
            @Query("creator") String creator,
            @Query("page") Integer page,
            @Query("limit") Integer limit
    );

    @POST("/event")
    Call<Event> saveEvent(@Body Event event);

    @PUT("/event")
    Call<Event> updateEvent(@Body Event event);

    @GET("/favorite")
    Call<List<Favorite>> getFavorite(@Query("creator") String creator);

    @POST("/favorite")
    Call<Favorite> addFavorite(@Body Favorite event);

    @DELETE("/favorite")
    Call<Favorite> removeFavorite(@Body Favorite event);
}
