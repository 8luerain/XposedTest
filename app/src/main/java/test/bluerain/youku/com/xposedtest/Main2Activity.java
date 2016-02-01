package test.bluerain.youku.com.xposedtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = "Xposed";
    private Handler mHandler;
    private List<String> mStringList;
    private ListView mFileListView;
    private Button mButtonGet;
    private Button mButtonClear;
    private ArrayAdapter<String> mFileInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler(new FileInfoHandler());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initViews() {
        mFileListView = (ListView) findViewById(R.id.id_lv_main);
        mFileListView.setAdapter(mFileInfoAdapter);
        mButtonGet = (Button) findViewById(R.id.id_btn_main_get);
        mButtonClear = (Button) findViewById(R.id.id_btn_main_clear);
        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUberCacheFile();
            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFile();
            }
        });
    }

    private void initData() {
        mStringList = new ArrayList<>();
        mFileInfoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStringList);
    }

    class FileInfoHandler implements Handler.Callback {
        public static final int ADD_FILE = 1;

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "message recivice" + mStringList.size());
                    mFileInfoAdapter.notifyDataSetChanged();
                    break;
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
                if (file.exists()) {
                    iterator.remove();
                    Log.d(TAG, "find file name is " + file.getAbsolutePath());
                    file.delete();
                }
            }
            new File(Wori.Handler.filePath).delete();
            mFileInfoAdapter.notifyDataSetChanged();
        }
    }

    private void getUberCacheFile() {
        new GetUberCacheThread().start();
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
}
