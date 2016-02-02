package test.bluerain.youku.com.xposedtest;

import android.app.ActivityManager;
import android.content.pm.IPackageDataObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import test.bluerain.youku.com.xposedtest.data.RandomBean;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = "Xposed";
    private Handler mHandler;
    private List<String> mStringList;
    private ListView mFileListView;
    private Button mButtonGet;
    private Button mButtonClear;
    /*---编辑区----start*/
    private EditText mEditText_imei;
    private EditText mEditText_imsi;
    private EditText mEditText_android;
    private EditText mEditText_serial;
    private EditText mEditText_sim_id;
    private EditText mEditText_phone_num;
    /*---编辑区----end*/
    private ArrayAdapter<String> mFileInfoAdapter;

    public static final String sRandomFilePath = "/storage/emulated/0/uber_random";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initViews();
    }

    private void initViews() {
        mEditText_imei = (EditText) findViewById(R.id.id_edt_main_imei);
        mEditText_imsi = (EditText) findViewById(R.id.id_edt_main_imsi);
        mEditText_serial = (EditText) findViewById(R.id.id_edt_main_serial);
        mEditText_android = (EditText) findViewById(R.id.id_edt_main_android_id);
        mEditText_sim_id = (EditText) findViewById(R.id.id_edt_main_sim);
        mEditText_phone_num = (EditText) findViewById(R.id.id_edt_main_phone_num);
        mFileListView = (ListView) findViewById(R.id.id_lv_main);
        mFileListView.setAdapter(mFileInfoAdapter);
        mButtonGet = (Button) findViewById(R.id.id_btn_main_get);
        mButtonClear = (Button) findViewById(R.id.id_btn_main_clear);
        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUberCacheFile();
                initEditView();
                saveRandomValue2File();
            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFile();
//                clearAppData();
            }
        });
    }

    private void initData() {
        mHandler = new Handler();
        mStringList = new ArrayList<>();
        mFileInfoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringList);
    }

    class FileInfoHandler implements Handler.Callback {
        public static final int ADD_FILE = 1;
        public static final int CLEAE_DATA = 2;

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "message recivice" + mStringList.size());
                    mFileInfoAdapter.notifyDataSetChanged();
                    break;
                case CLEAE_DATA:
                    if (msg.arg1 == 3) {
                        Log.d(TAG, "clear data success");
                    } else {
                        Log.d(TAG, "clear data failed");
                    }
            }
            return false;
        }
    }


    private void clearFile() {
        if (mStringList != null && mStringList.size() != 0) {
            Iterator<String> iterator = mStringList.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                File file = new File(next);
                if (!file.exists())
                    continue;
                if (file.delete()) {
                    iterator.remove();
                }
            }
            new File(Wori.Handler.filePath).delete();
            mFileInfoAdapter.notifyDataSetChanged();
        }
    }

    private void getUberCacheFile() {
        new GetUberCacheThread().start();
    }

    private void clearAppData() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        try {
            Class<?> aClass = Class.forName("android.app.ActivityManager");
            Method clearApplicationUserData = aClass.getMethod("clearApplicationUserData", String.class, IPackageDataObserver.class);
            clearApplicationUserData.invoke(manager, "com.ubercab", null);
            Log.d(TAG, "clear data");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    class ClearUserDataObserver extends IPackageDataObserver.Stub {
        public void onRemoveCompleted(final String packageName, final boolean succeeded) {
            Log.d(TAG, "clear result is -->" + succeeded);
            final Message msg = mHandler.obtainMessage(FileInfoHandler.CLEAE_DATA);
            msg.arg1 = succeeded ? 3 : 4;
            mHandler.sendMessage(msg);
        }
    }

    class GetUberCacheThread extends Thread {
        BufferedReader mBufferedReader;
        File cacheFile;

        public GetUberCacheThread() {
            try {
                cacheFile = new File(Wori.Handler.filePath);
                if (cacheFile.exists()) {
                    mBufferedReader = new BufferedReader(new FileReader(cacheFile));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        @Override
        public void run() {
            if (null == mBufferedReader)
                return;
            try {
                String tmp = null;
                mStringList.clear();
                while ((tmp = mBufferedReader.readLine()) != null) {
                    Log.d("Xposed", tmp);
                    if (!mStringList.contains(tmp))
                        mStringList.add(tmp);
                }
                Message msg = Message.obtain();
                msg.what = FileInfoHandler.ADD_FILE;
                mHandler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.run();
        }

    }

    public void clearByDir(File dirFile) {
        if (!dirFile.isDirectory())
            return;
        File[] files = dirFile.listFiles();
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }


    }

    public void superUserCommand(String commnd) {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("su");
            DataOutputStream os = new DataOutputStream(proc.getOutputStream());
            commnd = commnd + "\n";
            Log.d(TAG, "command is " + commnd);
            os.writeBytes(commnd);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEditView() {
        RandomBean bean = new RandomBean();
        mEditText_imei.setText(bean.getRandom_imei());
        Log.d("TAG", "random imei is  : " + bean.getRandom_imei());
        mEditText_imsi.setText(bean.getRandom_imsi());
        mEditText_serial.setText(bean.getRandom_serial());
        mEditText_android.setText(bean.getRandom_android());
        mEditText_sim_id.setText(bean.getRandom_sim_serial());
        mEditText_phone_num.setText(bean.getRandom_phone_num());
    }

    private void saveRandomValue2File() {
        File file = new File(sRandomFilePath);
        FileWriter writer = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            writer = new FileWriter(file);
            writer.write(mEditText_imei.getText().toString() + "\n");
            writer.write(mEditText_imsi.getText().toString() + "\n");
            writer.write(mEditText_serial.getText().toString() + "\n");
            writer.write(mEditText_android.getText().toString() + "\n");
            writer.write(mEditText_sim_id.getText().toString() + "\n");
            writer.write(mEditText_phone_num.getText().toString() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
