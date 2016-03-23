package test.bluerain.youku.com.xposedtest.data;

import java.io.Serializable;

/**
 * Project: XposedTest.
 * Data: 2016/3/23.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class DefaultUberAccountInfo implements Serializable {
    public String userName ;
    public String password;

    public DefaultUberAccountInfo(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
