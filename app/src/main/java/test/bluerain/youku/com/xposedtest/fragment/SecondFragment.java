package test.bluerain.youku.com.xposedtest.fragment;

import android.os.Bundle;
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

    private SecondFragmentListViewAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_secondpage, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mButtonRefresh = (Button) view.findViewById(R.id.id_btn_secondfragment_refresh);
        mButtonRefresh.setOnClickListener(new RefreshListener());
        mButtonClean = (Button) view.findViewById(R.id.id_btn_secondfragment_clean);
        mAdapter = new SecondFragmentListViewAdapter(CommonUtils.getRandomBean(Profile.sRandomFilePath).getDataList(), getContext(), R.layout.item_listview_secondfragment);
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
        }
    }

}
