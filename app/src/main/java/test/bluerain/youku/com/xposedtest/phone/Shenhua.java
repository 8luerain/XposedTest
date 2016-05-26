package test.bluerain.youku.com.xposedtest.phone;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.bluerain.youku.com.xposedtest.data.ShenhuaUserInfo;
import test.bluerain.youku.com.xposedtest.net.NetworkResponseListener;
import test.bluerain.youku.com.xposedtest.net.RemoteService;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class Shenhua {
    private static final String TAG = "Shenhua";

    //保存登陆信息
    public static ShenhuaUserInfo sShenhuaUserInfo;
    //滴滴
    public static final String HOST = "http://api.shjmpt.com:9002";
    public static final String USER = "mxh222";
    public static final String PASS = "shenhua2650122";
    public static final String DEVELOPER = "md%2fWxWnLcS23LuX%2fBXfvjQ%3d%3d";

    //记录ItemId
    /*185&google-gmail&0.10&4
233&UBER优步&0.48&3
1569&支付宝邮箱注册-支付宝手机注册&0.10&4
148&滴滴打车-滴滴出行-滴滴代驾&0.10&4
2058&滴滴快车司机注册-滴滴专车&0.10&4
35985&滴滴出行（发短信）&0.48&2
40&一号专车&0.10&4
50&快的打车&0.10&1
160&神州专车&0.10&4*/

    public static final String DIDI_GET = "148";
    public static final String DIDI_SEND = "35985";

    //地区
    public static final String AREA_BEIJING = "%E5%8C%97%E4%BA%AC";

    /*PhoneType*/
    public static final String PHONE_TYPE_RANDOM = "0";
    public static final String PHONE_TYPE_CMCC = "1";
    public static final String PHONE_TYPE_CHINA_UNION = "2";
    public static final String PHONE_TYPE_CHINA_DINAXIN = "3";

    private static final int RETRY_TIMES = 30; //重试次数
    private static final int RETRY_EVERY_TIME = 3000; //每次间隔时间3s

    private static int retry = 1;


    public static void login(final LoginListener loginListener) {
        /*http://api.shjmpt.com:9002/pubApi/uLogin?uName=用户名&pWord=密码&Developer=开发者参*/
        String methodName = "/pubApi/uLogin?";
        String param = "uName=" + USER + "&pWord=" + PASS + "&Developer=" + DEVELOPER;
        String url = HOST + methodName + param;
        RemoteService.getInstance().doRequest(url, new NetworkResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response.contains("&")) {
                    sShenhuaUserInfo = new ShenhuaUserInfo();
                    String[] splitOfResponse = response.split("&");
                    sShenhuaUserInfo.token = splitOfResponse[0];
                    sShenhuaUserInfo.sum = splitOfResponse[1];
                    sShenhuaUserInfo.maxLogin = splitOfResponse[2];
                    sShenhuaUserInfo.maxGetNum = splitOfResponse[3];
                    sShenhuaUserInfo.maxGetNumPer = splitOfResponse[4];
                    sShenhuaUserInfo.discount = splitOfResponse[5];
                    if (null != loginListener)
                        loginListener.loginSuccess(sShenhuaUserInfo.token);
                } else {
                    if (null != loginListener)
                        loginListener.loginFailed(response);
                }
                Log.d(TAG, "onSuccess: response[" + response + "]");
            }

            @Override
            public void onFailed(String failMessage) {
                if (null != loginListener)
                    loginListener.loginFailed(failMessage);
                Log.d(TAG, "onSuccess: failMessage[" + failMessage + "]");

            }
        });
    }

    /*
    * 5、获取号码
GET - http://api.shjmpt.com:9002/pubApi/GetPhone?ItemId=项目ID&token=登陆token
5.1、请求参数
参数名	必传	缺省值	描述
token	Y		登录token
ItemId	Y		项目代码
Count	N	1	获取数量 [不填默认1个]
Phone	N		指定号码获取 [不填则 随机]
Area	N		区域 [不填则 随机]
PhoneType	N	0	运营商 [不填为 0] 0 [随机] 1 [移动] 2 [联通] 3 [电信]
onlyKey	N		私人对接Key，与卡商直接对接
5.2、返回值
正确返回：13112345678;13698763743;13928370932;
注意：如果Count数量为20，获取后，确只返回了10个号码，则证明系统已经没有那
    * */
    public static void getPhoneNumByItem(final String itemId, String token, String count, String num, String area, String phoneType,
                                         final GetPhoneListener getPhoneListener) {

        String methodName = "/pubApi/GetPhone?";
        String param = "ItemId=" + itemId + "&token=" + token
                + "&Phone=" + num
                + "&Count=" + count
                + "&Area=" + area
                + "&PhoneType=" + phoneType
                + "&onlyKey=";
        String url = HOST + methodName + param;
        RemoteService.getInstance().doRequest(url, new NetworkResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response.contains(";")) {
                    List<String> phoneNums = new ArrayList<String>();
                    String[] split = response.split(";");
                    for (int i = 0; i < split.length; i++) {
                        phoneNums.add(split[i]);
                    }
                    if (null != getPhoneListener)
                        getPhoneListener.getPhoneSuccess(phoneNums);
                } else {
                    if (null != getPhoneListener)
                        getPhoneListener.getPhoneFailed(response);
                }
                Log.d(TAG, "onSuccess: response[" + response + "]");
            }

            @Override
            public void onFailed(String failMessage) {
                if (null != getPhoneListener)
                    getPhoneListener.getPhoneFailed(failMessage);
                Log.d(TAG, "onSuccess: failMessage[" + failMessage + "]");

            }
        });
    }




    /*
    6、获取消息
GET - http://api.shjmpt.com:9002/pubApi/GMessage?token=登陆token&ItemId=项目ID&Phone=获取的号码
6.1、请求参数
参数名	必传	缺省值	描述
token	Y		登录token
ItemId	Y		项目ID
Phone	Y		获取的号码
6.2、返回值
1.使用该方法获得验证码后，系统自动加黑该号码
2.此接口需要一直调用，建议该方法每5秒调用一次
正确返回：
发送状态介绍：
STATE&项目ID&手机号码&状态信息;
消息内容：
MSG&项目ID&手机号码&短信内容
    * */

    public static void getMessage(final String phoneNum, final String itemId, final String token, final NetworkResponseListener getMessageListener) {
        String methodName = "/pubApi/GMessage?";
        String param = "ItemId=" + itemId + "&token=" + token + "&Phone=" + phoneNum;
        String url = HOST + methodName + param;
        RemoteService.getInstance().doRequest(url, new NetworkResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response.contains("False")) {
                    if ((retry++) <= RETRY_TIMES) {
                        new Thread(new Runnable() { //防止阻塞主线程！
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(RETRY_EVERY_TIME);
                                    getMessage(phoneNum, itemId, token, getMessageListener);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    } else {
                        retry = 1;
                        if (null != getMessageListener)
                            getMessageListener.onFailed("重试次数已经到达上限！");
                        return;
                    }
                } else {
                    retry = 1;
                    if (null != getMessageListener)
                        getMessageListener.onSuccess(response);
                }
                Log.d(TAG, "onSuccess: response[" + response + "]");
            }

            @Override
            public void onFailed(String failMessage) {
                retry = 1;
                if (null != getMessageListener)
                    getMessageListener.onFailed(failMessage);
                Log.d(TAG, "onSuccess: failMessage[" + failMessage + "]");
            }
        });
    }



    /*
    * 9、发送短信
GET - http://api.shjmpt.com:9002/pubApi/SendMessage?token=登陆token&Phone=手机号&ItemId=项目ID&Msg=短信内容
9.1、请求参数
参数名	必传	缺省值	描述
token	Y		登录token
ItemId	Y		项目代码
phone	Y		号码
Msg	Y		短信内容
9.2、返回值
正确返回：Ok
    *
    * */

    public static void sendMessage(String phoneNum, String itemId, String messageBody, String token, final NetworkResponseListener
            sendMessageListener) {
        String methodName = "/pubApi/SendMessage?";
        String param = "ItemId=" + itemId + "&token=" + token + "&Phone=" + phoneNum + "&Msg=" + messageBody;
        String url = HOST + methodName + param;
        RemoteService.getInstance().doRequest(url, new NetworkResponseListener() {
            @Override
            public void onSuccess(String response) {
                if (response.contains("Ok")) {
                    if (null != sendMessageListener)
                        sendMessageListener.onSuccess("发送成功");
                } else {
                    if (null != sendMessageListener)
                        sendMessageListener.onFailed("发送失败");
                }
                Log.d(TAG, "onSuccess: response[" + response + "]");
            }

            @Override
            public void onFailed(String failMessage) {
                if (null != sendMessageListener)
                    sendMessageListener.onFailed(failMessage);
                Log.d(TAG, "onSuccess: failMessage[" + failMessage + "]");

            }
        });
    }


    public interface LoginListener {

        void loginSuccess(String token);

        void loginFailed(String response);
    }

    public interface GetPhoneListener {

        void getPhoneSuccess(List<String> phoneNums);

        void getPhoneFailed(String response);
    }
}
