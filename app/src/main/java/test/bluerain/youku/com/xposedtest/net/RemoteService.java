package test.bluerain.youku.com.xposedtest.net;

import android.content.ContentValues;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Project: XposedTest.
 * Data: 2016/5/26.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class RemoteService {

    private static final RemoteService INSTANCE = new RemoteService();
    private ExecutorService mExecutorService;

    private RemoteService() {
        //no instance
        mExecutorService = Executors.newFixedThreadPool(4);
    }

    public static RemoteService getInstance() {
        return INSTANCE;
    }


    public void doRequest(String url, String requestMethod, ContentValues param, NetworkResponseListener networkResponseListener) {
        HttpRequest httpRequest = new HttpRequest(requestMethod, url, param, networkResponseListener);
        mExecutorService.execute(httpRequest);
    }

    public void doRequest(String url, NetworkResponseListener networkResponseListener) {
        doRequest(url, HttpRequest.METHOD_GET, null, networkResponseListener);
    }

}
