package io.github.omievee.dlfect_alpha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
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

import io.github.omievee.dlfect_alpha.UsersandRatings.MyUsers;

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
    Spinner mSpinny;
    ArrayAdapter<String> mAdapt;
    String scores[] = {"Select A Category", "Friendly", "Professionalism", "Manners", "Engagement", "Presentation"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views
        mSend = (Button) findViewById(R.id.send);
        mTexty = (TextView) findViewById(R.id.TEXTy);
        mRating = (RatingBar) findViewById(R.id.RATING);
        mSEARCH = (SearchView) findViewById(R.id.EDITFIRE);
        mSpinny = (Spinner) findViewById(R.id.spinner);


        mAdapt = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, scores);
        mSpinny.setAdapter(mAdapt);

        //FireBase 1
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("users");


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
        searchView();

        //Onclick of submit raitng
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Find Someone to Rate!", Toast.LENGTH_SHORT).show();


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
                //if autheticated, save user to database w/ default values
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

    //commence search in searchview..
    public void searchView() {
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
                searchView_FireBaseSearch(newText);
                return true;
            }

            //go through Firebase & search for this user.. once found, Set onclick to that specific user & adjust base score
            public void searchView_FireBaseSearch(final String newText) {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference searchUsers = database.getReference("users");
                searchUsers.orderByChild("mEmail").equalTo(newText).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
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

                            mTexty.setText(dataSnapshot.child(key).getValue(MyUsers.class).getmEmail());


                            //once found set on click to rate this person & update their selected category.
                            final String newKey = key;
                            mSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final MyUsers foundUser = dataSnapshot.child(newKey).getValue(MyUsers.class);
                                    String categorySelected = mSpinny.getSelectedItem().toString().toLowerCase();

                                    switch (categorySelected) {
                                        //always add to the counter when Rated .. add to sum... and do math for average of selected category + update overall average
                                        case "friendly":
                                            foundUser.getmRatings().setFriendlyCOUNT(foundUser.getmRatings().getFriendlyCOUNT() + 1);
                                            foundUser.getmRatings().setFriendlySUM(foundUser.getmRatings().getFriendlySUM() + mRating.getRating());

                                            double friendSUM = foundUser.getmRatings().getFriendlySUM();
                                            double friendCOUNT = foundUser.getmRatings().getFriendlyCOUNT();
                                            double updateOverall1 = friendSUM / friendCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("friendlyCOUNT").setValue(friendCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("friendlySUM").setValue(friendSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updateOverall1);
                                            searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString().toLowerCase()).setValue(friendSUM / friendCOUNT);
                                            break;
                                        case "engagement":
                                            foundUser.getmRatings().setEngCOUNT(foundUser.getmRatings().getEngCOUNT() + 1);
                                            foundUser.getmRatings().setEngSUM(foundUser.getmRatings().getEngSUM() + mRating.getRating());

                                            double engSUM = foundUser.getmRatings().getEngSUM();
                                            double engCOUNT = foundUser.getmRatings().getEngCOUNT();
                                            double updateOverall2 = engSUM / engCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("engCOUNT").setValue(engCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("engSUM").setValue(engSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updateOverall2);
                                            searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString().toLowerCase()).setValue(engSUM / engCOUNT);

                                            break;
                                        case "professionalism":
                                            foundUser.getmRatings().setProCOUNT(foundUser.getmRatings().getProCOUNT() + 1);
                                            foundUser.getmRatings().setProSUM(foundUser.getmRatings().getProSUM() + mRating.getRating());

                                            double proSUM = foundUser.getmRatings().getProSUM();
                                            double proCOUNT = foundUser.getmRatings().getProCOUNT();
                                            double updatedOverall3 = proSUM / proCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("proCOUNT").setValue(proCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("proSUM").setValue(proSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall3);
                                            searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString().toLowerCase()).setValue(proSUM / proCOUNT);
                                            break;
                                        case "presentation":
                                            foundUser.getmRatings().setPreCOUNT(foundUser.getmRatings().getPreCOUNT() + 1);
                                            foundUser.getmRatings().setPreSUM(foundUser.getmRatings().getPreSUM() + mRating.getRating());

                                            double preSUM = foundUser.getmRatings().getPreSUM();
                                            double preCOUNT = foundUser.getmRatings().getPreCOUNT();
                                            double updatedOverall4 = preSUM / preCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("preCOUNT").setValue(preCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("preSUM").setValue(preSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall4);
                                            searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString().toLowerCase()).setValue(preSUM / preCOUNT);
                                            break;
                                        case "manners":
                                            foundUser.getmRatings().setMannersCOUNT(foundUser.getmRatings().getMannersCOUNT() + 1);
                                            foundUser.getmRatings().setMannersSUM(foundUser.getmRatings().getMannersCOUNT() + mRating.getRating());

                                            double manSUM = foundUser.getmRatings().getMannersSUM();
                                            double manCOUNT = foundUser.getmRatings().getMannersCOUNT();
                                            double updatedOverall5 = manSUM / manCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("mannersCOUNT").setValue(manCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("mannersSUM").setValue(manSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall5);
                                            searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString().toLowerCase()).setValue(manSUM / manCOUNT);
                                            break;
                                        default:
                                            Toast.makeText(MainActivity.this, "Select a Category", Toast.LENGTH_SHORT).show();
                                            break;

                                    }
                                    //set value to coresponding spinner / user selection.
//                                    searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString()).setValue(1.5);
                                }
                            });

                            //reset textview
                        } else if (newText.equals("")) {

                            mTexty.setText("Search Users");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

}

