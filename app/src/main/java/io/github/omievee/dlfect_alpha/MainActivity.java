package io.github.omievee.dlfect_alpha;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import io.github.omievee.dlfect_alpha.UsersandRatings.RatingActions;
import io.github.omievee.dlfect_alpha.UsersandRatings.Rating_ViewHolder;
import io.github.omievee.dlfect_alpha.UsersandRatings.Users;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "found";
    public static final int USERSIGNIN = 1;

    DatabaseReference mRef;
    String mCurrentUserID;
    RatingBar mRating;
    TextView mTexty;
    Button mSend;
    FirebaseRecyclerAdapter<RatingActions, Rating_ViewHolder> mAdapt;
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

        // Write a message to the database

        mSend = (Button) findViewById(R.id.send);
        mTexty = (TextView) findViewById(R.id.TEXTy);
        mRating = (RatingBar) findViewById(R.id.RATING);
        mSEARCH = (SearchView) findViewById(R.id.EDITFIRE);
        mRV = (RecyclerView) findViewById(R.id.RV);

        LinearLayoutManager linear = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRV.setLayoutManager(linear);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
//        mRef.child("Users").setValue("mUser");

        mRatingGiven = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_RATING);
        mUsersID = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_USERS);

//        mAdapt = new FirebaseRecyclerAdapter<RatingActions, Rating_ViewHolder>(RatingActions.class, android.R.layout.activity_list_item, Rating_ViewHolder.class, mRateRef) {
//            @Override
//            protected void populateViewHolder(final Rating_ViewHolder viewHolder, final RatingActions model, int position) {
//                Log.d(TAG, "populateViewHolder: ");
//                mRatingGiven = database.getReference();
//                mSEARCH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        InputMethodManager imm = (InputMethodManager) mSEARCH.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(mSEARCH.getWindowToken(), 0);
//
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//
//                        DatabaseReference mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//                        Query query = mFirebaseDatabaseReference.child("UserRating").orderByChild("mUser").equalTo("omar");
//
//                        query.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                dataSnapshot.child("UserRating").child("mUser").toString();
//
//                                Log.d(TAG, "lololololololololol: " + dataSnapshot.child("UserRating").child("mUser").getValue());
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                        return false;
//                    }
//                });
//            }
//        };
//
//        mRV.setAdapter(mAdapt);


//        mSEARCH.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                InputMethodManager imm = (InputMethodManager) mSEARCH.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(mSEARCH.getWindowToken(), 0);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.d(TAG, "THIS IS A LOG THIS IS A LOG THIS IS A LOG " + newText);
//
//                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                mRatingGiven = database.getReference();
//                mRatingGiven.child("UserRating").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(final DataSnapshot dataSnapshot) {
//                        mAdapt = new FirebaseRecyclerAdapter<RatingActions, Rating_ViewHolder>(RatingActions.class, android.R.layout.simple_list_item_2, Rating_ViewHolder.class, mRateRef) {
//                            @Override
//                            protected void populateViewHolder(Rating_ViewHolder viewHolder, RatingActions model, int position) {
//                                Log.d(TAG, "FUCKFUCK: " + model.getmUser());
//
//                                final Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                                for (DataSnapshot user : children) {
//                                    final RatingActions value = user.getValue(RatingActions.class);
//                                    Log.d(TAG, "THIS IS A LOG THIS IS A LOG THIS IS A LOG " +dataSnapshot.getChildren());
//                                    viewHolder.mTexty2.setText(model.getmUser().toString());
//
//
//                                    mRV.setAdapter(mAdapt);
//
//                                    Log.d(TAG, "FUCKFUCK: " + value.getmUser());
//                                }
//
//
//                        };
//                    }
//
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//                return true;
//            }
//        });


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
        } else {
            Toast.makeText(this, "mmm toast", Toast.LENGTH_SHORT).show();
            signIN();
        }

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
    }

    public void saveRatingsToFirebase(String rating) {
        mRatingGiven.push().setValue(rating);
    }


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

//    public void searchFIRE() {
//        final DatabaseReference user = database.getReference("UserID");
//        user.orderByChild('_searchLastName')
//                .startAt(queryText)
//                .endAt(queryText+"\uf8ff")
//                .once("value")
//    }
    }
}
