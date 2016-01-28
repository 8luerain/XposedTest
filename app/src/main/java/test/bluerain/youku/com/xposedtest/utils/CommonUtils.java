package test.bluerain.youku.com.xposedtest.utils;

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
}
