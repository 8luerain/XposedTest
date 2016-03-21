package test.bluerain.youku.com.xposedtest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Project: XposedTest.
 * Data: 2016/3/21.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class MainPagerFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;


    public MainPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    public List<Fragment> getFragments() {
        return mFragments;
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }
}
