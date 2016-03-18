package test.bluerain.youku.com.xposedtest.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Project: remoteXposedTest.
 * Data: 2016/1/28.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class CommonUtils {


    private static final String BOOHEE_PACKAGE_NAME = "com.ubercab";

    private static final String CMD_CLEAR_APP_DATA = "pm clear ";

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

        if (!des.exists())
            des.createNewFile();

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
}
