package bookdlab.bookd.database;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    public static void saveUser(User user, DatabaseReference.CompletionListener listener) {
        Queries queries = new Queries();
        queries.getUserReference().push().setValue(user, listener);
    }

    public static void isUserPresentInDatabase(String email, UserCheckCallback callback) {
        Queries queries = new Queries();
        queries.getUserFromEmail(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if (!children.iterator().hasNext()) {
                    Log.d(TAG, "User not present in database");
                    callback.userIsNotPresent();
                } else {
                    User signedInUser = new User();
                    for (DataSnapshot child : children) {
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

    private static void getBusinessNearByFromId(ArrayList<String> ids, NearbyBusinessCallback callback) {
        Queries queries = new Queries();
        ArrayList<Business> businesses = new ArrayList<>();
        for (String id : ids) {
            queries.getBusinessById(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.d(TAG, "onDataChange: adding business");
                        Business business = child.getValue(Business.class);
                        businesses.add(business);

                        // After adding all businesses, callback
                        if (businesses.size() == ids.size()) {
                            callback.onNearbyBusinessesFound(businesses);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static void removeEventWithId(String eventId, OnSuccessListener<Void> listener) {
        Queries queries = new Queries();
        queries.getBookedBusinessesForEvent(eventId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().setValue(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("events").child(eventId).removeValue().addOnSuccessListener(listener);
    }
}
