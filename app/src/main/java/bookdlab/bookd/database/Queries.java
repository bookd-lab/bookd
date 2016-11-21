package bookdlab.bookd.database;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by pranavkonduru on 11/19/16.
 */

public class Queries {

    private FirebaseDatabase database;

    public Queries() {
        database = FirebaseDatabase.getInstance();
    }

    public Query getBusinessInArea(String area) {
        DatabaseReference reference = database.getReference().child("business");
        return reference.orderByChild("city").equalTo(area).limitToFirst(100);
    }

    public Query getReviewsForBusiness(String businessId) {
        DatabaseReference reference = database.getReference().child("reviews");
        Query q = reference.orderByChild("businessId").equalTo(businessId).limitToFirst(100);
        return q;
    }

    public Query getFeaturedReviewForBusiness(String businessId) {
        DatabaseReference reference = database.getReference().child("reviews");
        Query q = reference.orderByChild("businessId").equalTo(businessId).limitToFirst(1);
        return q;
    }

    public Query getUserFromId(String userId) {
        DatabaseReference reference = database.getReference().child("users");
        return reference.orderByChild("id").equalTo(userId).limitToFirst(1);
    }

    public Query getUserFromEmail(String email) {
        DatabaseReference reference = database.getReference().child("users");
        return reference.orderByChild("email").equalTo(email).limitToFirst(1);
    }

    public Query getEventsOfUser(String userId) {
        DatabaseReference reference = database.getReference().child("events");
        return reference.orderByChild("creator").equalTo(userId);
    }

    public Query getBusinessById(String id) {
        DatabaseReference reference = database.getReference().child("business");
        return reference.orderByChild("id").equalTo(id).limitToFirst(1);
    }

    public GeoQuery getNearbyBusiness(Double lat, Double lng, Double radius) {
        DatabaseReference reference = database.getReference().child("geoFire");
        GeoFire geoFire = new GeoFire(reference);
        return geoFire.queryAtLocation(new GeoLocation(lat, lng), radius);
    }

    public Query getBookedBusinessesForEvent(String eventId){
        DatabaseReference reference = database.getReference().child("event_booked_business");
        return reference.orderByChild("eventId").equalTo(eventId);
    }

}
