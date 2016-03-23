package test.bluerain.youku.com.xposedtest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.serivces.MyAccessbilityService;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.Profile;

public class Main2Activity extends AppCompatActivity {
    public static final String TAG = "Xposed";
    private Handler mHandler;
    private List<String> mStringList;
    private ListView mFileListView;
    private Button mButtonGet;
    private Button mButtonClear;
    private Button mButtonSave;
    private Button mButtonRestore;
    private Button mSetDefault;
    private Button mRestoreDefault;
    /*---编辑区----start*/
    private EditText mEditText_imei;
    private EditText mEditText_imsi;
    private EditText mEditText_android;
    private EditText mEditText_serial;
    private EditText mEditText_sim_id;
    private EditText mEditText_phone_num;
    private EditText mEditText_build_serial;
    private EditText mEditText_build_model;
    /*---编辑区----end*/
    private ArrayAdapter<String> mFileInfoAdapter;

    public static final String sRandomFilePath = "/storage/emulated/0/uber_random";
    public static final String sRandomSaveDirPath = "/storage/emulated/0/uber_save";

    public static final int SAVE_FILE_FLAG = 0;
    public static final int RESTORE_FILE_FLAG = 1;

    private RandomBean mRandombean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initViews();
        initService();
    }

    private void initService() {


    }

    private void initViews() {
        mEditText_imei = (EditText) findViewById(R.id.id_edt_main_imei);
        mEditText_imsi = (EditText) findViewById(R.id.id_edt_main_imsi);
        mEditText_serial = (EditText) findViewById(R.id.id_edt_main_serial);
        mEditText_android = (EditText) findViewById(R.id.id_edt_main_android_id);
        mEditText_sim_id = (EditText) findViewById(R.id.id_edt_main_sim);
        mEditText_phone_num = (EditText) findViewById(R.id.id_edt_main_phone_num);
        mEditText_build_serial = (EditText) findViewById(R.id.id_edt_main_build_serial);
        mEditText_build_model = (EditText) findViewById(R.id.id_edt_main_build_model);
        mFileListView = (ListView) findViewById(R.id.id_lv_main);
        mFileListView.setAdapter(mFileInfoAdapter);
        mButtonGet = (Button) findViewById(R.id.id_btn_main_get);
        mButtonClear = (Button) findViewById(R.id.id_btn_main_clear);
        mButtonSave = (Button) findViewById(R.id.id_btn_main_save);
        mButtonRestore = (Button) findViewById(R.id.id_btn_main_restore);
        mSetDefault = (Button) findViewById(R.id.id_btn_main_setDefault);
        mRestoreDefault = (Button) findViewById(R.id.id_btn_main_restroeDefault);
        mButtonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRandombean = new RandomBean();
                CommonUtils.saveBeanToFile(Profile.sRandomFile, mRandombean);
                getUberCacheFile();
                initEditView();
                Intent intent = new Intent(Main2Activity.this, MyAccessbilityService.class);
                startActivity(intent);
                Toast.makeText(Main2Activity.this, "随机成功", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFile();
                CommonUtils.forceStopApp(Profile.UBER_PACKAGE_NAME);
                CommonUtils.clearAppData(Profile.UBER_PACKAGE_NAME);
                CommonUtils.launchApp(Main2Activity.this, Profile.UBER_PACKAGE_NAME);
                Toast.makeText(Main2Activity.this, "清理完成", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(sRandomSaveDirPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                showFileDialog(SAVE_FILE_FLAG);
            }
        });
        mButtonRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileDialog(RESTORE_FILE_FLAG);
            }
        });

        mSetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CommonUtils.copyFile(sRandomFilePath, sRandomSaveDirPath + "/" + "default");
                    Toast.makeText(Main2Activity.this, "设置默认成功~~", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mRestoreDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CommonUtils.copyFile(sRandomSaveDirPath + "/" + "default", sRandomFilePath);
                    mRandombean =(RandomBean) CommonUtils.getBeanFromFile(Profile.sRandomFile);
                    initEditView();
                    Toast.makeText(Main2Activity.this, "恢复默认成功~~", Toast.LENGTH_SHORT).show();
//                    CommonUtils.launchApp(Main2Activity.this, Profile.UBER_PACKAGE_NAME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    private void showFileDialog(final int flag) {
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
                    Toast.makeText(Main2Activity.this, "文件名为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (flag == SAVE_FILE_FLAG) {
                        CommonUtils.copyFile(sRandomFilePath, sRandomSaveDirPath + "/" + text);
                        Toast.makeText(Main2Activity.this, "保存成功~~", Toast.LENGTH_SHORT).show();
                    } else if (flag == RESTORE_FILE_FLAG) {
                        File file = new File(sRandomFilePath);
                        if (file.exists())
                            file.delete();
                        CommonUtils.copyFile(sRandomSaveDirPath + "/" + text, sRandomFilePath);
                        restoreRandomSurface();
                        Toast.makeText(Main2Activity.this, "恢复成功~~", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Main2Activity.this, "源文件不存在", Toast.LENGTH_SHORT).show();
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

    private void restoreRandomSurface() {
        mEditText_imei.setText(mRandombean.getRandom_imei());
        mEditText_imsi.setText(mRandombean.getRandom_imsi());
        mEditText_android.setText(mRandombean.getRandom_android());
        mEditText_serial.setText(mRandombean.getRandom_serial());
        mEditText_sim_id.setText(mRandombean.getRandom_sim_serial());
        mEditText_phone_num.setText(mRandombean.getRandom_phone_num());
    }

    private synchronized void clearFile() {
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
            new File(Wori.FileHandler.filePath).delete();
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
                cacheFile = new File(Wori.FileHandler.filePath);
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
        mEditText_imei.setText(mRandombean.getRandom_imei());
        mEditText_imsi.setText(mRandombean.getRandom_imsi());
        mEditText_serial.setText(mRandombean.getRandom_serial());
        mEditText_android.setText(mRandombean.getRandom_android());
        mEditText_sim_id.setText(mRandombean.getRandom_sim_serial());
        mEditText_phone_num.setText(mRandombean.getRandom_phone_num());
        mEditText_build_serial.setText(mRandombean.getRandom_build_serial());
        mEditText_build_model.setText(mRandombean.getRandom_build_model());
    }

    private void saveRandomValue2File() {
        File file = new File(Profile.sRandomFile);
        FileWriter writer = null;
        try {
            if (file.exists())
                file.delete();
            file.createNewFile();
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
