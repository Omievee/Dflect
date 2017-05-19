package io.github.omievee.projectfinal.UsersandRatings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import io.github.omievee.projectfinal.R;

/**
 * Created by omievee on 5/18/17.
 */

public class Rating_Viewholder extends RecyclerView.ViewHolder {
    public Button mTestButton;
    public RatingBar mRating;
    public TextView mTexty, mTexty2;
    View mView;


    public Rating_Viewholder(View itemView) {
        super(itemView);

        mView = itemView;
        mTestButton = (Button) itemView.findViewById(R.id.send);
        mRating = (RatingBar) itemView.findViewById(R.id.RATING);
        mTexty = (TextView) itemView.findViewById(R.id.TEXTy);
        mTexty2 = (TextView) itemView.findViewById(android.R.id.text1);

    }


}

