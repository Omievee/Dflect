package io.github.omievee.dlfect_alpha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.github.omievee.dlfect_alpha.UsersandRatings.MyUsers;

import static android.os.Build.VERSION_CODES.M;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "found";
    public static final int USERSIGNIN = 1;

    DatabaseReference mRef;
    String mCurrentUserID;
    FirebaseAuth mRateSomeone;
    RatingBar mRating;
    TextView mTexty;
    Button mSend;
    SearchView mSEARCH;
    FirebaseAuth mAuth;
    DatabaseReference mRatingGiven;
    DatabaseReference mUsersID;
    public static final String FIREBASE_CHILD_RATING = "Rating";
    public static final String SELECTED_USER = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views
        mSend = (Button) findViewById(R.id.send);
        mTexty = (TextView) findViewById(R.id.TEXTy);
        mRating = (RatingBar) findViewById(R.id.RATING);
        mSEARCH = (SearchView) findViewById(R.id.EDITFIRE);


        //FireBase 1
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("users");

        mRatingGiven = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_RATING);


        //Authenticating USer
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
        } else {
            signIN();

        }
        mTexty.setText("Search Users");

        //METHODS TO EXECUTE:
        //Perform Search on FB & bring back matching email.
        searchFB();

        //Onclick of submit raitng
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference peerRate = database.getReference().child("Activity");
////                peerRate.push().setValue(mCurrentUserID + "  " + mRating.getRating() + "  " );
////                Log.d(TAG, "onClick: " + mCurrentUserID);
////                peerRate.child()
                FirebaseUser myID = mRateSomeone.getCurrentUser();
//                    myID = new MyUsers

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == USERSIGNIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
                saveUsersToFirebase();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Check Network", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {

                    return;
                }
            }


        }
    }

    //Add rating to FDB
    public void peer2PeerRate(String rating) {
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
                        database.child(currentUser.getUid()).setValue(new MyUsers(currentUser.getUid(),
                                currentUser.getDisplayName(),
                                currentUser.getEmail(), 2.5));
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
            public boolean onQueryTextChange(final String newText) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference searchUsers = database.getReference("users");
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
//                            System.out.println(key);

                            mTexty.setText(dataSnapshot.child(key).getValue(MyUsers.class).getmEmail());
                            //check if field is empty & clear TextView

                            final MyUsers myUser = dataSnapshot.child(key).getValue(MyUsers.class);

                            String usertest = dataSnapshot.child(key).getValue(MyUsers.class).getmEmail();
                            final Double test = dataSnapshot.child(key).getValue(MyUsers.class).getmRating();

                            //once found set on click to rate this person!
                            final String newKey = key;
                            mSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference database = searchUsers.child(newKey).child("mRating");
                                    database.setValue(mRating.getRating());

                                }
                            });
                        } else if (newText.equals("")) {
                            mTexty.setText("Search Users");
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                });

                return false;
            }
        });
    }
}



