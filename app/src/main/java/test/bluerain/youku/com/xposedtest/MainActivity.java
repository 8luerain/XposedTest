package test.bluerain.youku.com.xposedtest;


import android.os.*;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.bluerain.youku.com.xposedtest.adapter.MainPagerFragmentAdapter;
import test.bluerain.youku.com.xposedtest.fragment.DidiFragment;
import test.bluerain.youku.com.xposedtest.fragment.FirstFragment;
import test.bluerain.youku.com.xposedtest.fragment.SecondFragment;

/**
 * Project: XposedTest.
 * Data: 2016/3/21.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class MainActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private MainPagerFragmentAdapter mFragmentPagerAdapter;

    private List<Fragment> mFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("process", "UI process id is:  " + android.os.Process.myPid());
        initData();
        initView();
    }

    private void initData() {
        mFragments = new ArrayList<>();
        mFragments.add(new DidiFragment());
        mFragments.add(new FirstFragment());
        mFragments.add(new SecondFragment());
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_vp_main);
        mViewPager.setOffscreenPageLimit(3);
        mFragmentPagerAdapter = new MainPagerFragmentAdapter(getSupportFragmentManager());
        mFragmentPagerAdapter.setFragments(mFragments);
        mViewPager.setAdapter(mFragmentPagerAdapter);
    }

    public List<Fragment> getListFragment() {
        return mFragments;
    }
}
