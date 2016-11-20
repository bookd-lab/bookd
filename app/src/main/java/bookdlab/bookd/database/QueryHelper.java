package bookdlab.bookd.database;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import bookdlab.bookd.interfaces.UserCheckCallback;

/**
 * Created by pranavkonduru on 11/19/16.
 */

public class QueryHelper {

    private static final String TAG = "QueryHelper";

    public static void isUserPresentInDatabase(String uid, UserCheckCallback callback){
        Queries queries = new Queries();
        queries.getUserFromId(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(!children.iterator().hasNext()){
                    Log.d(TAG, "User not present in database");
                    callback.userIsNotPresent();
                } else {
                    callback.userIsPresent();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
