package io.github.omievee.dlfect_alpha;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import java.util.Map;

import io.github.omievee.dlfect_alpha.UsersandRatings.MyUsers;

import static android.R.attr.data;
import static android.R.attr.debuggable;
import static android.R.attr.defaultHeight;
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
    public static final String SELECTED_USER = "";
    Spinner mSpinny;
    ArrayAdapter<String> mAdapt;
    String scores[] = {"Select A Category", "Friendly", "Professionalism", "Manners", "Engagement", "Presentation"};
    public static final int Friendly = 0;
    public static final int Professionalism = 1;
    public static final int Manners = 2;
    public static final int ENgagement = 3;
    public static final int Presentation = 4;
    private String mFoundUser;
    double mSelectedStars;


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

        mSpinny.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //METHODS TO EXECUTE:
        //Perform Search on FB & bring back matching email.
        searchView();

        //Onclick of submit raitng
//        mSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });


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
                            Log.d(TAG, "FIRST DATA CHANGE: " + dataSnapshot);

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

                            //get value associted w/ user & whatever the spinner is on..
                            final MyUsers foundUser = dataSnapshot.child(key).getValue(MyUsers.class);
                            String categorySelected = mSpinny.getSelectedItem().toString();
                            switch (categorySelected){
                                case "Engagement":
                                    foundUser.getmRatings().getEngagement();
                                    break;
                                case "Friendly":
                                    foundUser.getmRatings().getFriendly();
                                    break;
                                case "Professionalism":
                                    foundUser.getmRatings().getProfessionalism();
                                    break;
                                case "Presentation":
                                    foundUser.getmRatings().getPresentation();
                                case "Manners":
                                    foundUser.getmRatings().getManners();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Select a Rating", Toast.LENGTH_SHORT).show();

                                    Log.d(TAG, "SPINSPINSPIN: " + categorySelected);
                            }

                            //once found set on click to rate this person & update their base rate.
                            final String newKey = key;
                            mSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    mFoundUser = dataSnapshot.child(newKey).getValue(MyUsers.class).getmLastName();

                                    //set value to coresponding spinner / user selection.
                                    searchUsers.child(newKey).child("mRatings").child(mSpinny.getSelectedItem().toString()).setValue(1.5);


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

