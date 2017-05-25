package io.github.omievee.dlfect_alpha.UsersandRatings;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import io.github.omievee.dlfect_alpha.R;

/**
 * Created by omievee on 5/16/17.
 */

public class Rating_ViewHolder extends RecyclerView.ViewHolder {
    public Button mTestButton;
    public RatingBar mRating;
    public TextView mTexty, mTexty2;
    View mView;

    public Rating_ViewHolder(View itemView) {
        super(itemView);

        mView = itemView;
        mTestButton = (Button) itemView.findViewById(R.id.send);
        mRating = (RatingBar) itemView.findViewById(R.id.RATING);
        mTexty = (TextView) itemView.findViewById(R.id.TEXTy);
        mTexty2 = (TextView) itemView.findViewById(android.R.id.text1);


    }


}

