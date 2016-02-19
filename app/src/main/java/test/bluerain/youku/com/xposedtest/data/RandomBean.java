package test.bluerain.youku.com.xposedtest.data;

import java.io.Serializable;

import test.bluerain.youku.com.xposedtest.utils.CommonUtils;

/**
 * Project: XposedTest.
 * Data: 2016/2/2.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class RandomBean implements Serializable {
    private String random_imei;
    private String random_imsi;
    private String random_android;
    private String random_serial;
    private String random_sim_serial;
    private String random_phone_num;

    public RandomBean() {
        random_imei = "35" + CommonUtils.getRandomNumString(13);
        random_serial = CommonUtils.getRandomMixString(16);
        random_imsi = "46001" + CommonUtils.getRandomNumString(10);
        random_android = CommonUtils.getRandomMixString(16);
        random_sim_serial = "898601" + CommonUtils.getRandomNumString(14);
        random_phone_num = "132" + CommonUtils.getRandomNumString(8);
    }

    public String getRandom_imei() {
        return random_imei;
    }

    public String getRandom_imsi() {
        return random_imsi;
    }

    public String getRandom_android() {
        return random_android;
    }

    public String getRandom_serial() {
        return random_serial;
    }

    public String getRandom_sim_serial() {
        return random_sim_serial;
    }

    public String getRandom_phone_num() {
        return random_phone_num;
    }
}