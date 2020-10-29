package com.reality.datingapp.CustomAdatpter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.reality.datingapp.ui.Fragments.ChatsFragment;
import com.reality.datingapp.ui.Fragments.FeedsFragment;
import com.reality.datingapp.ui.Fragments.ProfileFragment;
import com.reality.datingapp.ui.Fragments.SwipeFragment;
import com.reality.datingapp.ui.Fragments.UsersFragment;


public class MainAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public MainAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProfileFragment tab0 = new ProfileFragment();

                return tab0;
            case 1:

                UsersFragment tab1 = new UsersFragment();

                return tab1;
            case 2:

                SwipeFragment tab2 = new SwipeFragment();

                return tab2;

            case 3:

                ChatsFragment tab3 = new ChatsFragment();

                return tab3;
            case 4:

                FeedsFragment tab4 = new FeedsFragment();

                return tab4;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }


}