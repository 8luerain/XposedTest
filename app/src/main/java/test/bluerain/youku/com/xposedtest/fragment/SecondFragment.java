package test.bluerain.youku.com.xposedtest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import test.bluerain.youku.com.xposedtest.R;
import test.bluerain.youku.com.xposedtest.adapter.SecondFragmentListViewAdapter;
import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Created by rain on 2016/3/21.
 */
public class SecondFragment extends Fragment {
    private ListView mListView;

    private Button mButtonRefresh;
    private Button mButtonClean;

    private Button mButtonSetService;

    private SecondFragmentListViewAdapter mAdapter;
    private List<RandomBean.DataBean> mDataBeanList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secondpage, null);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        RandomBean randomBean = CommonUtils.getRandomBean(Profile.sRandomFilePath);
        if (null != randomBean)
            mDataBeanList = randomBean.getDataList();
    }

    private void initView(View view) {
        mButtonSetService = (Button) view.findViewById(R.id.id_btn_secondfragment_set_service);
        mButtonSetService.setOnClickListener(new SetServiceListener());
        mButtonRefresh = (Button) view.findViewById(R.id.id_btn_secondfragment_refresh);
        mButtonRefresh.setOnClickListener(new RefreshListener());
        mButtonClean = (Button) view.findViewById(R.id.id_btn_secondfragment_clean);
        mButtonClean.setOnClickListener(new CleanDataListener());
        mAdapter = new SecondFragmentListViewAdapter(mDataBeanList, getContext(), R.layout
                .item_listview_secondfragment);
        mListView = (ListView) view.findViewById(R.id.id_lv_second_fragment);
        mListView.setAdapter(mAdapter);

    }


    class RefreshListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            RandomBean bean = new RandomBean();
            CommonUtils.saveRandomBean(Profile.sRandomFilePath, bean);
            List<RandomBean.DataBean> dataList = bean.getDataList();
            mAdapter.setmDataBeans(dataList);
            mAdapter.notifyDataSetChanged();
            cleanDataAndLaunch();
        }
    }

    class SetServiceListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
    }
    class CleanDataListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }

    private void cleanDataAndLaunch() {
        CommonUtils.forceStopApp(Profile.UBER_PACKAGE_NAME);
        CommonUtils.clearAppData(Profile.UBER_PACKAGE_NAME);
        CommonUtils.launchApp(getActivity(), Profile.UBER_PACKAGE_NAME);
    }

}
