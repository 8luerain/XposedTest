package test.bluerain.youku.com.xposedtest.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Project: remoteXposedTest.
 * Data: 2016/1/28.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class CommonUtils {


    private static final String BOOHEE_PACKAGE_NAME = "com.ubercab";

    private static final String CMD_CLEAR_APP_DATA = "pm clear ";

    private static final String CMD_CLEAR_APP_DATA_SD = "rm -R /storage/emulated/0/Android/*";

    private static final String CMD_FORCE_STOP_APP = "am force-stop ";


    public static String getRandomNumString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(String.valueOf((int) (Math.random() * 10)));
        }
        return builder.toString();
    }


    public static String getRandomMixString(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomInt = (int) (Math.random() * 10);
            if ((randomInt % 2) == 0) {
                char c = (char) (randomInt + 97);
                builder.append(c);
                continue;
            }
            builder.append(String.valueOf(randomInt));
        }

        return builder.toString();

    }

    public static String getRandomMixUpcaseString(int length) {

        return getRandomMixString(length).toUpperCase();

    }

    public static String getRandomNumByLine(int i) {
        File file = new File("/storage/emulated/0/uber_random");
        LineNumberReader lineNumberReader = null;
        StringBuilder builder = new StringBuilder();
        try {
            if (file.exists()) {
                lineNumberReader = new LineNumberReader(new FileReader(file));
                String tmp = null;
                while ((tmp = lineNumberReader.readLine()) != null) {
                    builder.append(tmp + "\n");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != lineNumberReader) {
                try {
                    lineNumberReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String fileString = builder.toString();
        Log.d("TAG", "Read num is --->" + fileString);
        String[] split = fileString.split("\\n");
        if (split != null && (split.length >= i)) {
            String value = split[i];
            Log.d("TAG", "Read sub is --->" + value);
            return value;
        }
        return null;
    }


    public static void copyFile(String sourceFile, String desFile) {

        Log.d("TAG", "copyFile source file path : " + sourceFile);
        Log.d("TAG", "copyFile des file path : " + desFile);
        Log.d("TAG", "copyFile result is : " + do_exec("cp -f " + sourceFile + " " + desFile));
    }



    /*
    public static void copyFile(String sourceFile, String desFile) throws Exception {
        Log.d("TAG", desFile);
        if (TextUtils.isEmpty(sourceFile) || TextUtils.isEmpty(desFile))
            return;
        File source = new File(sourceFile);
        File des = new File(desFile);
        FileReader reader = null;
        FileWriter writer = null;

        if (!source.exists()) {
            throw new Exception("Source file do not exist");
        }

        if (des.exists())
            des.delete();

        reader = new FileReader(sourceFile);
        writer = new FileWriter(des);
        char[] buf = new char[1024];
        while (reader.read(buf) != -1) {
            writer.write(buf);
        }
        writer.flush();
        reader.close();
        writer.close();
    }

*/


    public static Object getBeanFromFile(String filePath) {
        Object bean = null;
        File file = new File(filePath);
        ObjectInputStream inputStream = null;
        if (!file.exists()) {
            return bean;
        }
        try {
            inputStream = new ObjectInputStream(new FileInputStream(file));
            bean = inputStream.readObject();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeQuiltly(inputStream);
        }

        return bean;
    }

    public static boolean saveBeanToFile(String filePath, Object bean) {
        File file = new File(filePath);
        ObjectOutputStream outputStream = null;
        if (file.exists()) {
            file.delete();
        }

        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(file));
            outputStream.writeObject(bean);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuiltly(outputStream);
        }

        return true;
    }

    /**
     * 启动薄荷App
     *
     * @param context
     */

    public static void launchApp(Context context, String packageName) {
        // 判断是否安装过App，否则去市场下载
        if (isAppInstalled(context, packageName)) {
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(packageName));
        }
    }

    /**
     * 检测某个应用是否安装
     *
     * @param context
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public static String do_exec(String cmd) {
        String s = "/n";
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line + "/n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public static String do_exec_with_root(String cmd) {
        String s = "\n";
        try {
            Process su_p = Runtime.getRuntime().exec("su");
            DataOutputStream dataOutputStream = new DataOutputStream(su_p.getOutputStream());
            dataOutputStream.writeBytes(cmd + "\n");
            dataOutputStream.writeBytes("exit" + "\n");
            dataOutputStream.flush();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(su_p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                s += line + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }


    public static void clearAppData(String packageName) {
        String s = do_exec_with_root(CMD_CLEAR_APP_DATA + packageName);
        Log.d("TAG", "clear data result is " + s + "..........");
    }

    public static void forceStopApp(String packageName) {
        String s = do_exec_with_root(CMD_FORCE_STOP_APP + packageName);
        Log.d("TAG", "force stop result is " + s + "..........");
    }

    public static void closeQuiltly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void changeAirplaneOn() {
        /*
        settings put global airplane_mode_on 1
        am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true
         */
        do_exec_with_root("settings put global airplane_mode_on 1");
        do_exec_with_root("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state true");
    }

    public static void changeAirplaneOff() {
        /*
        settings put global airplane_mode_on 0
        am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false
         */
        do_exec_with_root("settings put global airplane_mode_on 0");
        do_exec_with_root("am broadcast -a android.intent.action.AIRPLANE_MODE --ez state false");
    }


    public static ProgressDialog createProgressDialog(Context activityContext, String msg) {
        ProgressDialog mypDialog = new ProgressDialog(activityContext);
        //实例化
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //设置进度条风格，风格为圆形，旋转的
        mypDialog.setTitle("稍后");
        //设置ProgressDialog 标题
        mypDialog.setMessage(msg + "进行中......");
        mypDialog.setIcon(android.R.drawable.ic_menu_compass);
        mypDialog.setIndeterminate(false);
        //设置ProgressDialog 的进度条是否不明确
        mypDialog.setCancelable(true);
        //设置ProgressDialog 是否可以按退回按键取消
        return mypDialog;
    }

    public static void uberEverythingNew(final Activity activityContext, String msg) {
        final ProgressDialog progressDialog = CommonUtils.createProgressDialog(activityContext, msg);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonUtils.changeAirplaneOn();
                CommonUtils.changeAirplaneOff();
                CommonUtils.forceStopApp(Profile.UBER_PACKAGE_NAME);
                CommonUtils.clearAppData(Profile.UBER_PACKAGE_NAME);
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                CommonUtils.launchApp(activityContext, Profile.UBER_PACKAGE_NAME);
                activityContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
