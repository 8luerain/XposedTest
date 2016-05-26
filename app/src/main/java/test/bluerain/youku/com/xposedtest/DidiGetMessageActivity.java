package test.bluerain.youku.com.xposedtest;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import test.bluerain.youku.com.xposedtest.net.NetworkResponseListener;
import test.bluerain.youku.com.xposedtest.phone.Shenhua;
import test.bluerain.youku.com.xposedtest.utils.MessageContainer;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class DidiGetMessageActivity extends Activity {
    private static final String TAG = "DidiGetMessageActivity";
    private TextView mTextViewMessageBody;

    private String mMessageBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_message);
        initData();
        initView();
        sendMessage(mMessageBody);
    }

    private void initData() {
        mMessageBody = getIntent().getStringExtra("DIDI");
        Log.d(TAG, "initData: get Didi " + mMessageBody + "");
    }

    private void initView() {
        mTextViewMessageBody = (TextView) findViewById(R.id.id_txv_get_message_body);
        mTextViewMessageBody.setText(mMessageBody);
    }

    private void sendMessage(String messageBody) {
        String encode = "";
        try {
            encode = URLEncoder.encode(messageBody, "UTF-8");
            Log.d(TAG, "sendMessage: encode[" + encode + "]");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        final String currentPhoneNum = MessageContainer.sCurrentPhoneNum;
        if (TextUtils.isEmpty(currentPhoneNum))
            return;
        Shenhua.sendMessage(currentPhoneNum, Shenhua.DIDI_SEND, encode, Shenhua.sShenhuaUserInfo.token, new NetworkResponseListener() {
            @Override
            public void onSuccess(String response) {
                Shenhua.getMessage(currentPhoneNum, Shenhua.DIDI_SEND, Shenhua.sShenhuaUserInfo.token, new NetworkResponseListener() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(DidiGetMessageActivity.this, "已经成功，请返回滴滴！", Toast.LENGTH_LONG).show();
                        setResult(1);
                        finish();
                    }

                    @Override
                    public void onFailed(String failMessage) {
                        Toast.makeText(DidiGetMessageActivity.this, "发送失败，请重试！", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }

            @Override
            public void onFailed(String failMessage) {
                Toast.makeText(DidiGetMessageActivity.this, "我擦，提交手机号时竟然出错了，请重试！", Toast.LENGTH_LONG).show();
            }
        });
    }
}
