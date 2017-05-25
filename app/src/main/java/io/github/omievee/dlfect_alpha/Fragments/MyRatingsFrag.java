package io.github.omievee.dlfect_alpha.Fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.github.omievee.dlfect_alpha.R;
import io.github.omievee.dlfect_alpha.UsersandRatings.MyUsers;

/**
 * Created by omievee on 5/23/17.
 */

public class MyRatingsFrag extends Fragment {
    public static final String TAG = "found";

    RatingBar mRating;
    TextView mTexty;
    FloatingActionButton mSend;
    SearchView mSEARCH;
    Spinner mSpinny;
    ArrayAdapter<String> mAdapt;
    String scores[] = {"Were They...", "Engaged", "Friendly", "Polite", "Presentable", "Professional"};
    DatabaseReference mRef;
    MediaPlayer mPositiveratingSound, mNegativeratingSound;


    public MyRatingsFrag() {
    }

    public static MyRatingsFrag newInstance() {
        MyRatingsFrag frag = new MyRatingsFrag();
        Bundle ment = new Bundle();

        frag.setArguments(ment);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myratingsfrag, container, false);

        mSend = (FloatingActionButton) view.findViewById(R.id.send);
        mTexty = (TextView) view.findViewById(R.id.TEXTy);
        mRating = (RatingBar) view.findViewById(R.id.RATING);
        mSEARCH = (SearchView) view.findViewById(R.id.EDITFIRE);
        mSpinny = (Spinner) view.findViewById(R.id.spinner);

        mAdapt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, scores);
        mSpinny.setAdapter(mAdapt);

        //FireBase 1
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference("users");

        mSend.setHapticFeedbackEnabled(true);

        //Onclick of submit raitng
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Find Someone to Rate!", Toast.LENGTH_SHORT).show();
            }
        });

        mTexty.setText("Rate A Friend!");
        searchView();

        mPositiveratingSound = MediaPlayer.create(view.getContext(), R.raw.nosedive_4_stars);
        mNegativeratingSound = MediaPlayer.create(view.getContext(), R.raw.nosedive_1_star);

        return view;

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

                            mTexty.setText(dataSnapshot.child(key).getValue(MyUsers.class).getmLastName());


                            //once found set on click to rate this person & update their selected category.
                            final String newKey = key;
                            mSend.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final MyUsers foundUser = dataSnapshot.child(newKey).getValue(MyUsers.class);
                                    String categorySelected = mSpinny.getSelectedItem().toString().toLowerCase();

                                    switch (categorySelected) {
                                        //case & child matching case were later hardcoded in because titles were changed in the spinner...
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
                                        case "engaged":
                                            foundUser.getmRatings().setEngCOUNT(foundUser.getmRatings().getEngCOUNT() + 1);
                                            foundUser.getmRatings().setEngSUM(foundUser.getmRatings().getEngSUM() + mRating.getRating());

                                            double engSUM = foundUser.getmRatings().getEngSUM();
                                            double engCOUNT = foundUser.getmRatings().getEngCOUNT();
                                            double updateOverall2 = engSUM / engCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("engCOUNT").setValue(engCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("engSUM").setValue(engSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updateOverall2);
                                            searchUsers.child(newKey).child("mRatings").child("engagement").setValue(engSUM / engCOUNT);

                                            break;
                                        case "professional":
                                            foundUser.getmRatings().setProCOUNT(foundUser.getmRatings().getProCOUNT() + 1);
                                            foundUser.getmRatings().setProSUM(foundUser.getmRatings().getProSUM() + mRating.getRating());

                                            double proSUM = foundUser.getmRatings().getProSUM();
                                            double proCOUNT = foundUser.getmRatings().getProCOUNT();
                                            double updatedOverall3 = proSUM / proCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("proCOUNT").setValue(proCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("proSUM").setValue(proSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall3);
                                            searchUsers.child(newKey).child("mRatings").child("professionalism").setValue(proSUM / proCOUNT);
                                            break;
                                        case "presentable":
                                            foundUser.getmRatings().setPreCOUNT(foundUser.getmRatings().getPreCOUNT() + 1);
                                            foundUser.getmRatings().setPreSUM(foundUser.getmRatings().getPreSUM() + mRating.getRating());

                                            double preSUM = foundUser.getmRatings().getPreSUM();
                                            double preCOUNT = foundUser.getmRatings().getPreCOUNT();
                                            double updatedOverall4 = preSUM / preCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("preCOUNT").setValue(preCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("preSUM").setValue(preSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall4);
                                            searchUsers.child(newKey).child("mRatings").child("presentation").setValue(preSUM / preCOUNT);
                                            break;
                                        case "polite":
                                            foundUser.getmRatings().setMannersCOUNT(foundUser.getmRatings().getMannersCOUNT() + 1);
                                            foundUser.getmRatings().setMannersSUM(foundUser.getmRatings().getMannersCOUNT() + mRating.getRating());

                                            double manSUM = foundUser.getmRatings().getMannersSUM();
                                            double manCOUNT = foundUser.getmRatings().getMannersCOUNT();
                                            double updatedOverall5 = manSUM / manCOUNT / 5 + 2;

                                            searchUsers.child(newKey).child("mRatings").child("mannersCOUNT").setValue(manCOUNT);
                                            searchUsers.child(newKey).child("mRatings").child("mannersSUM").setValue(manSUM);
                                            searchUsers.child(newKey).child("mRatings").child("overAll").setValue(updatedOverall5);
                                            searchUsers.child(newKey).child("mRatings").child("manners").setValue(manSUM / manCOUNT);
                                            break;
                                        default:
                                            Toast.makeText(getContext(), "Select a Category", Toast.LENGTH_SHORT).show();
                                            break;


                                    }
                                    Toast.makeText(getContext(), "Rating Sent!", Toast.LENGTH_SHORT).show();
                                    if (mRating.getRating() >= 2.5) {
                                        mPositiveratingSound.start();
                                    } else {
                                        mNegativeratingSound.start();
                                    }
                                }
                            });
                            //reset textview
                        } else if (newText.equals("")) {

                            mTexty.setText("Rate A Friend!");
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
