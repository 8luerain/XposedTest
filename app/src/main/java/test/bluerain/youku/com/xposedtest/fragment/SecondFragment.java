package test.bluerain.youku.com.xposedtest.fragment;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import test.bluerain.youku.com.xposedtest.R;
import test.bluerain.youku.com.xposedtest.adapter.SecondFragmentListViewAdapter;
import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.data.RecordTable;
import test.bluerain.youku.com.xposedtest.provider.ReocrdProvider;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Created by rain on 2016/3/21.
 */
public class SecondFragment extends Fragment {
    private ListView mListView;

    private Button mButtonRefresh;
    private Button mButtonClean;
    private Button mButtonSave;

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
        mButtonSave = (Button) view.findViewById(R.id.id_btn_secondfragment_save);
        mButtonSave.setOnClickListener(new SaveDataListener());
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
            refreshListView(bean);
            cleanDataAndLaunch();
        }
    }

    class SaveDataListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            showFileDialog();

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

    private void showFileDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.getWindow().setContentView(R.layout.layout_dialog_save_file);
        final EditText fileNameText = (EditText) alertDialog.getWindow().findViewById(R.id.id_edt_layout_filename);
        Button ok = (Button) alertDialog.getWindow().findViewById(R.id.id_btn_layout_dialog_ok);
        Button cancle = (Button) alertDialog.getWindow().findViewById(R.id.id_btn_layout_dialog_cancle);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = fileNameText.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getContext(), "文件名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    CommonUtils.copyFile(Profile.sRandomFilePath, Profile.sRandomSaveDirPath + "/" + text);
                    insertData(text);
                    Toast.makeText(getActivity(), "保存成功~~", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "源文件不存在", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }


    private void insertData(String data) {
        ContentValues values = new ContentValues();
        values.put(RecordTable.COLNUM_USER, data + "@relegoule.sends.cn");
        values.put(RecordTable.COLNUM_PHONE, "meiyoumima123");
        getActivity().getContentResolver().insert(ReocrdProvider.RECORED_URI, values);
    }

    public void refreshListView(RandomBean bean) {
        List<RandomBean.DataBean> dataList = bean.getDataList();
        mAdapter.setmDataBeans(dataList);
        mAdapter.notifyDataSetChanged();
    }

}
