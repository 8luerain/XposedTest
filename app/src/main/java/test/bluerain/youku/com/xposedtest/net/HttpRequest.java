package test.bluerain.youku.com.xposedtest.net;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;

import test.bluerain.youku.com.xposedtest.utils.CommonUtils;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class HttpRequest implements Runnable {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public static final int MESSAGE_SUCCESS = 1000;
    public static final int MESSAGE_FAILED = 1001;

    private String mRequestMethod;
    private String mUrl;
    private ContentValues mRequestParam;
    private Handler mHandler;
    private NetworkResponseListener mNetworkResponseListener;

    public static final int CON_TIMEOUT = 5000;

    public HttpRequest() {
    }

    public HttpRequest(String requestMethod, String url, ContentValues requestParam, NetworkResponseListener networkResponseListener) {
        mRequestMethod = requestMethod;
        mUrl = url;
        mRequestParam = requestParam;
        mNetworkResponseListener = networkResponseListener;
    }

    public String getRequestMethod() {
        return mRequestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        mRequestMethod = requestMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public ContentValues getRequestParam() {
        return mRequestParam;
    }

    public void setRequestParam(ContentValues requestParam) {
        mRequestParam = requestParam;
    }

    public NetworkResponseListener getNetworkResponseListener() {
        return mNetworkResponseListener;
    }

    public void setNetworkResponseListener(NetworkResponseListener networkResponseListener) {
        mNetworkResponseListener = networkResponseListener;
    }


    @Override
    public void run() {
        mHandler = new HttpRequestHandler(Looper.getMainLooper());
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(mUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(mRequestMethod);
            httpURLConnection.setConnectTimeout(CON_TIMEOUT);
            if (TextUtils.equals(mRequestMethod, METHOD_POST))
                httpURLConnection.setDoOutput(true);
            if (null != mRequestParam) {
                Set<String> strings = mRequestParam.keySet();
                for (String key : strings) {
                    String value = (String) mRequestParam.get(key);
                    httpURLConnection.setRequestProperty(key, value);
                }
            }
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Message message = Message.obtain();
                message.what = MESSAGE_FAILED;
                message.obj = httpURLConnection.getResponseCode() + httpURLConnection.getRequestMethod();
                mHandler.sendMessage(message);
                return;
            }
            reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            for (String s = reader.readLine(); !TextUtils.isEmpty(s); s = reader.readLine()) {
                builder.append(s);
            }
            Message message = Message.obtain();
            message.what = MESSAGE_SUCCESS;
            message.obj = builder.toString();
            mHandler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
            Message message = Message.obtain();
            message.what = MESSAGE_FAILED;
            message.obj = "网络IO异常";
            mHandler.sendMessage(message);
        } finally {
            CommonUtils.closeQuiltly(reader);
        }
    }

    class HttpRequestHandler extends Handler {

        public HttpRequestHandler() {
            super();
        }

        public HttpRequestHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (null == mNetworkResponseListener)
                return;
            int what = msg.what;
            String data = (String) msg.obj;
            switch (what) {
                case MESSAGE_SUCCESS:
                    mNetworkResponseListener.onSuccess(data);
                    break;
                case MESSAGE_FAILED:
                    mNetworkResponseListener.onFailed(data);
                    break;
            }
        }
    }
}

