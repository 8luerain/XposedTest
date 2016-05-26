package test.bluerain.youku.com.xposedtest.data;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class ShenhuaUserInfo {
    /*hoGPZI3zpQ7vNCSwmxG1dNH1LwK0Jy49&4.212&200&50&50&0.99&0.000*/
    /*登录token&账户余额&最大登录客户端个数&最多获取号码数&单个客户端最多获取号码数&折扣*/
    public String token;
    public String sum;
    public String maxLogin;
    public String maxGetNum;
    public String maxGetNumPer;
    public String discount;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getMaxLogin() {
        return maxLogin;
    }

    public void setMaxLogin(String maxLogin) {
        this.maxLogin = maxLogin;
    }

    public String getMaxGetNum() {
        return maxGetNum;
    }

    public void setMaxGetNum(String maxGetNum) {
        this.maxGetNum = maxGetNum;
    }

    public String getMaxGetNumPer() {
        return maxGetNumPer;
    }

    public void setMaxGetNumPer(String maxGetNumPer) {
        this.maxGetNumPer = maxGetNumPer;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
