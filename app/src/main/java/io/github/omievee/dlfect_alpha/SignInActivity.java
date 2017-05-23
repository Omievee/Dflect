package io.github.omievee.dlfect_alpha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import io.github.omievee.dlfect_alpha.Fragments.MyRatingsFrag;
import io.github.omievee.dlfect_alpha.Fragments.RatingsAdapter;
import io.github.omievee.dlfect_alpha.UsersandRatings.MyUsers;

import static com.google.android.gms.common.SignInButton.SIZE_ICON_ONLY;

public class SignInActivity extends AppCompatActivity {
    public static final int USERSIGNIN = 1;
    FirebaseAuth mAuth;
    String mCurrentUserID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        SignInButton googlesignin = (SignInButton) findViewById(R.id.GoogleSignIN);
        googlesignin.setSize(SIZE_ICON_ONLY);


        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        } else {
            googlesignin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Sign in  to GoogleAccount
                       signIN();

                }

            });


        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == USERSIGNIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                mCurrentUserID = mAuth.getCurrentUser().getDisplayName();
                //if autheticated, save user to database w/ default values & take to main activity
                saveUsersToFirebase();
                Intent intent = new Intent(getApplicationContext(), RatingsAdapter.class);
                startActivity(intent);
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

    public void signIN() {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);

        startActivityForResult(// Get an instance of AuthUI based on the default app
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(), USERSIGNIN);


    }
}
