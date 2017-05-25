package io.github.omievee.dlfect_alpha.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.omievee.dlfect_alpha.R;

/**
 * Created by omievee on 5/24/17.
 */

public class HistoryFrag extends Fragment {

    public HistoryFrag() {
    }


    public static HistoryFrag newInstance() {
        HistoryFrag frag = new HistoryFrag();
        Bundle ment = new Bundle();

        frag.setArguments(ment);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historyfrag, container, false);


        /**TODO - add history fragment if time allows
        *user can see history of who they have rated & who has rated them
         * possibly add location of rate?
         */
        return view;

    }
}
