package io.github.omievee.dlfect_alpha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.auth.ui.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import io.github.omievee.dlfect_alpha.UsersandRatings.RatingActions;
import io.github.omievee.dlfect_alpha.UsersandRatings.Rating_ViewHolder;
import io.github.omievee.dlfect_alpha.UsersandRatings.Users;

import static android.R.attr.borderlessButtonStyle;
import static android.R.attr.data;
import static android.R.attr.mode;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "found";
    public static final int USERSIGNIN = 1;

    DatabaseReference mRef;
    DatabaseReference mSearchUsers;
    String mCurrentUserID;
    RatingBar mRating;
    TextView mTexty;
    Button mSend;
    FirebaseRecyclerAdapter<Users, Rating_ViewHolder> mAdapt;
    SearchView mSEARCH;
    FirebaseAuth mAuth;
    DatabaseReference mRatingGiven;
    DatabaseReference mUsersID;
    RecyclerView mRV;
    public static final String FIREBASE_CHILD_RATING = "Rating";
    public static final String FIREBASE_CHILD_USERS = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//views
        mSend = (Button) findViewById(R.id.send);
        mTexty = (TextView) findViewById(R.id.TEXTy);
        mRating = (RatingBar) findViewById(R.id.RATING);
        mSEARCH = (SearchView) findViewById(R.id.EDITFIRE);
        mRV = (RecyclerView) findViewById(R.id.RV);
        LinearLayoutManager linear = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(linear);

        //FireBase 1
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("users");
        mRatingGiven = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_RATING);
        mUsersID = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_USERS);


        //Authenticating USer
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
        } else {
            signIN();
        }

        //Onclick of submit raitng
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTexty.setText("Rating is:" + mRating.getRating());
                saveRatingsToFirebase("Rating is:" + mRating.getRating());

                saveUsersToFirebase();
//                 mRef.push().setValue(new RatingActions(mCurrentUserID, mRating.getRating()));
//                mTexty.setText("You've been rated: " + mRating.getRating());
            }
        });
        //Perform Search on FB & bring back matching email.
        searchFB();

    }

    //Add rating to FDB
    public void saveRatingsToFirebase(String rating) {
        mRatingGiven.push().setValue(rating);
    }

    //Sign in  to GoogleAccount
    public void signIN() {
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(), USERSIGNIN);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == USERSIGNIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {

                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    return;
                }
            }


        }


    }

    //Check if users already exist in FB IF NOT add
    public void saveUsersToFirebase() {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            DatabaseReference child = database.child(currentUser.getUid());
            child.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        database.child(currentUser.getUid()).setValue(new Users(currentUser.getUid(),
                                currentUser.getDisplayName(),
                                currentUser.getEmail()));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();

                }
            });

        }
    }

    public void searchFB() {
        mSEARCH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //code to hide softkeyboard
                InputMethodManager imm = (InputMethodManager) mSEARCH.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSEARCH.getWindowToken(), 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference searchUsers = database.getReference("users");
                searchUsers.orderByChild("mEmail").equalTo(newText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            boolean exit = true;
                            int counter = 1;
                            String key = dataSnapshot.getValue().toString();
                            while (exit) {
                                if (key.charAt(counter) == '=')
                                    exit = false;
                                else
                                    counter++;
                            }
                            key = key.substring(1, counter);
                            System.out.println(key);
                            mTexty.setText(dataSnapshot.child(key).getValue(Users.class).getmEmail());

                        } else {

                            Toast.makeText(MainActivity.this, "No User Found!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                return false;
            }
        });
    }
}



