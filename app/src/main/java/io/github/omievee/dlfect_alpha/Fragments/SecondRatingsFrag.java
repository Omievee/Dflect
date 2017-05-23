package io.github.omievee.dlfect_alpha.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.omievee.dlfect_alpha.R;

/**
 * Created by omievee on 5/23/17.
 */

public class SecondRatingsFrag extends Fragment {

    public SecondRatingsFrag() {
    }

    public static SecondRatingsFrag newInstance() {

        SecondRatingsFrag frag = new SecondRatingsFrag();
        Bundle ment = new Bundle();

        frag.setArguments(ment);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.secondratingsfrag, container, false);
        return view;


    }
}
