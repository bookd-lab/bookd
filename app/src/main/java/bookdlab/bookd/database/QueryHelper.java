package bookdlab.bookd.database;

import android.util.Log;

import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import bookdlab.bookd.interfaces.NearbyBusinessCallback;
import bookdlab.bookd.interfaces.UserCheckCallback;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.User;

/**
 * Created by pranavkonduru on 11/19/16.
 */

public class QueryHelper {

    private static final String TAG = "QueryHelper";

    public static void isUserPresentInDatabase(String email, UserCheckCallback callback){
        Queries queries = new Queries();
        queries.getUserFromEmail(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(!children.iterator().hasNext()){
                    Log.d(TAG, "User not present in database");
                    callback.userIsNotPresent();
                } else {
                    User signedInUser = new User();
                    for(DataSnapshot child : children){
                        signedInUser = child.getValue(User.class);
                    }
                    callback.userIsPresent(signedInUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void getBusinessesInArea(Double lat, Double lng, Double radius, NearbyBusinessCallback callback){
        Queries queries = new Queries();
        ArrayList<String> businessIds = new ArrayList<>();

        queries.getNearbyBusiness(lat, lng, radius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                Log.d(TAG, "onKeyEntered: "+key);
                businessIds.add(key);
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                Log.d(TAG, "onGeoQueryReady: all queries complete");
                getBusinessNearByFromId(businessIds, callback);
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private static void getBusinessNearByFromId(ArrayList<String> ids, NearbyBusinessCallback callback){
        Queries queries = new Queries();
        ArrayList<Business> businesses = new ArrayList<>();
        for(String id: ids) {
            queries.getBusinessById(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot child: dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: adding business");
                        Business business = child.getValue(Business.class);
                        businesses.add(business);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
