package test.bluerain.youku.com.xposedtest.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import test.bluerain.youku.com.xposedtest.MainActivity;
import test.bluerain.youku.com.xposedtest.R;
import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.data.RecordTable;
import test.bluerain.youku.com.xposedtest.provider.ReocrdProvider;
import test.bluerain.youku.com.xposedtest.serivces.MyAccessbilityService;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Project: XposedTest.
 * Data: 2016/3/21.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class FirstFragment extends Fragment {

    private ListView mListView;

    private CursorAdapter mCursorAdapter;

    private Button mButtonSetData;

    private Button mButtonLoadDefault;

    public static final int SAVE_FILE_FLAG = 0;
    public static final int RESTORE_FILE_FLAG = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        getLoaderManager().initLoader(-1, null, new CursorHanlder());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_firstpage, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mButtonSetData = (Button) view.findViewById(R.id.id_btn_firstfragment_set_data);
        mButtonLoadDefault = (Button) view.findViewById(R.id.id_btn_firstfragment_get_default);
        mButtonSetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mButtonLoadDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mListView = (ListView) view.findViewById(R.id.id_lv_first_fragemt);
        mCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_listview_firstfragment,
                null,
                new String[]{RecordTable.COLNUM_USER, RecordTable.COLNUM_PHONE},
                new int[]{R.id.id_txv_index_firstfragment, R.id.id_txv_phone_firstfragment},
                -1
        );
        mListView.setAdapter(mCursorAdapter);
        mListView.setOnItemClickListener(new ListViewItemClickListener());
    }

    private void setSecondFragmentListView() {
        RandomBean currentRandomBean = CommonUtils.getRandomBean(Profile.sRandomFilePath);
        MainActivity mainActivity = (MainActivity) getActivity();
        List<Fragment> listFragment = mainActivity.getListFragment();
        if (null != listFragment) {
            SecondFragment secondFragment = (SecondFragment) listFragment.get(1);
            secondFragment.refreshListView(currentRandomBean);
        }
    }


    class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Uri uri = Uri.withAppendedPath(ReocrdProvider.RECORED_URI, position + 1 + "");
            Log.d("TAG", uri.toString());
            Cursor query = getActivity().getContentResolver().query(uri, null,
                    RecordTable._ID + " = ?", null, null);
            if (null == query)
                return;
            if (query.moveToFirst()) {
                int indexUser = query.getColumnIndex(RecordTable.COLNUM_USER);
                int indexPhone = query.getColumnIndex(RecordTable.COLNUM_PHONE);
                String user = query.getString(indexUser);
                String phone = query.getString(indexPhone);
                MyAccessbilityService.mSecondPageData.add(user);
                MyAccessbilityService.mSecondPageData.add(phone);
                String fileName = user.split("@")[0];
                try {
                    String fullFilePath = Profile.sRandomSaveDirPath + fileName;
                    CommonUtils.copyFile(fullFilePath, Profile.sRandomFilePath);
                    setSecondFragmentListView();
                    Toast.makeText(getContext(), "还原文件成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.d("TAG", "还原文件失败" + e.toString());
                    Toast.makeText(getContext(), "还原文件失败...", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), user + " & " + phone, Toast.LENGTH_SHORT).show();
            }
            CommonUtils.launchApp(getContext(), Profile.UBER_PACKAGE_NAME);
        }
    }


    class CursorHanlder implements LoaderManager.LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getContext(), ReocrdProvider.RECORED_URI, null, null, null, null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mCursorAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mCursorAdapter.swapCursor(null);
        }
    }

}
