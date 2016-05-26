package test.bluerain.youku.com.xposedtest.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String random_build_model;
    private String random_build_serial;
    private String random_os_version;
    private String random_mac_address;

    public RandomBean() {
        random_imei = "35" + CommonUtils.getRandomNumString(13);
        random_serial = CommonUtils.getRandomMixString(16);
        random_imsi = "46001" + CommonUtils.getRandomNumString(10);
        random_android = CommonUtils.getRandomMixString(16);
        random_sim_serial = "898601" + CommonUtils.getRandomNumString(14);
        random_phone_num = "132" + CommonUtils.getRandomNumString(8);
        random_build_model = CommonUtils.getRandomMixUpcaseString(5);
        random_build_serial = CommonUtils.getRandomMixString(10);
        random_os_version = "5.0." + CommonUtils.getRandomNumString(1);
        random_mac_address = CommonUtils.getRandomMacAddress();
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

    public String getRandom_build_model() {
        return random_build_model;
    }

    public String getRandom_build_serial() {
        return random_build_serial;
    }

    public String getRandom_os_version() {
        return random_os_version;
    }

    public String getRandom_mac_address() {
        return random_mac_address;
    }

    public class DataBean {
        public String itemName;
        public String itemValue;

        public DataBean(String itemName, String itemValue) {
            this.itemName = itemName;
            this.itemValue = itemValue;
        }
    }

    public List<DataBean> getDataList() {
        List<DataBean> beans = new ArrayList<>();
        beans.add(new DataBean("IMEI: ", this.getRandom_imei()));
        beans.add(new DataBean("IMSI: ", this.getRandom_imsi()));
        beans.add(new DataBean("Android ID: ", this.getRandom_android()));
        beans.add(new DataBean("Serial: ", this.getRandom_serial()));
        beans.add(new DataBean("SIM ID: ", this.getRandom_sim_serial()));
        beans.add(new DataBean("Build serial: ", this.getRandom_build_serial()));
        beans.add(new DataBean("Model: ", this.getRandom_build_model()));
        beans.add(new DataBean("Phone: ", this.getRandom_phone_num()));
        beans.add(new DataBean("MAC: ", this.getRandom_mac_address()));
        return beans;
    }
}
