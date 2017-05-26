package io.github.omievee.dlfect_alpha.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

public class StatsFrag extends Fragment {
    TextView mPersonalRate, mOverAllScore, mFriend, mPro, mPre, mEng, mPOLITE;
    TextView mFtext, mProText, mPreText, mEngText, mPOL;
    FirebaseAuth mAuth;
    SwipeRefreshLayout mREFRESHING;

    public StatsFrag() {
    }

    public static StatsFrag newInstance() {

        StatsFrag frag = new StatsFrag();
        Bundle ment = new Bundle();

        frag.setArguments(ment);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondratingsfrag, container, false);

        mPersonalRate = (TextView) view.findViewById(R.id.personalRating);
        mOverAllScore = (TextView) view.findViewById(R.id.OVERALLSCORE);

        mFriend = (TextView) view.findViewById(R.id.friendlyDISPLAY);
        mPOL = (TextView) view.findViewById(R.id.PolDISPLAY);
        mEng = (TextView) view.findViewById(R.id.EndDISPLAY);
        mPro = (TextView) view.findViewById(R.id.ProDISPLAY);
        mPre = (TextView) view.findViewById(R.id.PreDISPLAY);

        mFtext = (TextView) view.findViewById(R.id.friendlySCORE);
        mFtext.setText(R.string.currentF);
        mPOLITE = (TextView) view.findViewById(R.id.PolSCORE);
        mPOLITE.setText(R.string.currentPOL);
        mPreText = (TextView) view.findViewById(R.id.PreSCORE);
        mPreText.setText(R.string.currentPRE);
        mProText = (TextView) view.findViewById(R.id.ProSCORE);
        mProText.setText(R.string.currentPRO);
        mEngText = (TextView) view.findViewById(R.id.EngSCORE);
        mEngText.setText(R.string.currentENG);

        //swipe to refresh display incase it doesnt auto update.
        mREFRESHING = (SwipeRefreshLayout) view.findViewById(R.id.SWIPEREFRESH);
        mREFRESHING.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayPersonalStats();
                mREFRESHING.setRefreshing(false);

            }
        });

        displayPersonalStats();



        return view;
    }

    //check current user, then go through firebase & get Ratings for the matching current user.
    public void displayPersonalStats() {
        mAuth = FirebaseAuth.getInstance();

        mPersonalRate.setText("Welcome " + mAuth.getCurrentUser().getDisplayName().toUpperCase());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference currentUSER = database.getReference("users");

        currentUSER.orderByChild("mName").equalTo(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
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

                    double overall = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getOverAll();
                    double friendly = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getFriendly();
                    double pro = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getProfessionalism();
                    double eng = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getEngagement();
                    double polite = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getManners();
                    double pre = dataSnapshot.child(key).getValue(MyUsers.class).getmRatings().getPresentation();


                    //set texts w/ proper ratings but formatted w/ 2 decimal points
                    mOverAllScore.setText(String.format("% .2f", overall));
                    mFriend.setText(String.format("% .2f", friendly));
                    mPro.setText(String.format("% .2f", pro));
                    mPre.setText(String.format("% .2f", pre));
                    mEng.setText(String.format("% .2f", eng));
                    mPOL.setText(String.format("% .2f", polite));



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}








