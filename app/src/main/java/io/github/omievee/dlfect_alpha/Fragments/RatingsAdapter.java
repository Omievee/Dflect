package io.github.omievee.dlfect_alpha.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by omievee on 5/23/17.
 */

public class RatingsAdapter extends FragmentPagerAdapter {

    public RatingsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MyRatingsFrag.newInstance();
            case 1:
                return StatsFrag.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Send Rating";
            case 1:
                return "My Info";
            default:
                return null;
        }
    }

}
