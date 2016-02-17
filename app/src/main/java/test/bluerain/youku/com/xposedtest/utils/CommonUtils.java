package test.bluerain.youku.com.xposedtest.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

/**
 * Project: remoteXposedTest.
 * Data: 2016/1/28.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class CommonUtils {

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
        if (split != null) {
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
}
