package test.bluerain.youku.com.xposedtest.serivces;

import android.accessibilityservice.AccessibilityService;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: XposedTest.
 * Data: 2016/3/21.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class MyAccessbilityService extends AccessibilityService {

    private static String TAG_EVENT = "AccessibilityEventTAG";

    private static String TAG_UBER = "AccessibilityUber";


    private String[] watchPackageNames = {
            "com.ubercab"
    };

    private String[] watchActivityName = {

            "com.ubercab.client.feature.launch.LauncherActivity"
            ,
            "com.ubercab.client.feature.launch.SignInOrRegisterActivity" //uber第一页
            ,
            "com.ubercab.client.feature.signin.SignInActivity" //uber登录页
    };

    private String[] watchStrings = {

            "登录",
            "忘记密码",
            "电话号码或电子邮箱",
            "密码"
    };

    private List<AccessibilityNodeInfo> mNodeInfos = new ArrayList<>();

    private List<AccessibilityNodeInfo> mReturnEdv = new ArrayList<>();

    public static List<String> mSecondPageData = new ArrayList<>();

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();

        String eventText = "";

        uberActionHandler(event);

        switch (eventType) {

            case AccessibilityEvent.TYPE_VIEW_CLICKED:

                eventText = "TYPE_VIEW_CLICKED";

                List<CharSequence> text = event.getText();

                eventText += "& text is " + text;

                break;

            case AccessibilityEvent.TYPE_VIEW_FOCUSED:

                eventText = "TYPE_VIEW_FOCUSED";

                break;

            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:

                eventText = "TYPE_VIEW_LONG_CLICKED";

                break;

            case AccessibilityEvent.TYPE_VIEW_SELECTED:

                eventText = "TYPE_VIEW_SELECTED";

                break;

            case AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED:

                eventText = "TYPE_VIEW_TEXT_CHANGED";

                break;

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                eventText = "TYPE_WINDOW_STATE_CHANGED";

                break;

            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:

                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";

                break;

            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_END:

                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_END";

                break;

            case AccessibilityEvent.TYPE_ANNOUNCEMENT:

                eventText = "TYPE_ANNOUNCEMENT";

                break;

            case AccessibilityEvent.TYPE_TOUCH_EXPLORATION_GESTURE_START:

                eventText = "TYPE_TOUCH_EXPLORATION_GESTURE_START";

                break;

            case AccessibilityEvent.TYPE_VIEW_HOVER_ENTER:

                eventText = "TYPE_VIEW_HOVER_ENTER";

                break;

            case AccessibilityEvent.TYPE_VIEW_HOVER_EXIT:

                eventText = "TYPE_VIEW_HOVER_EXIT";

                break;

            case AccessibilityEvent.TYPE_VIEW_SCROLLED:

                eventText = "TYPE_VIEW_SCROLLED";

                break;

            case AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED:

                eventText = "TYPE_VIEW_TEXT_SELECTION_CHANGED";

                break;

            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:

                eventText = "TYPE_WINDOW_CONTENT_CHANGED";

                break;

        }

        Log.d(TAG_EVENT, "event type is " + eventText + "...");

    }

    @Override
    public void onInterrupt() {

    }


    private void uberActionHandler(AccessibilityEvent event) {
        if (!TextUtils.equals(event.getPackageName(), watchPackageNames[0]))
            return;

        int eventType = event.getEventType();

        switch (eventType) {

            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                String className = event.getClassName().toString();
                Log.d(TAG_EVENT, "----------" + className + "-----------");

                if (TextUtils.equals(className, watchActivityName[1])) {
                    Log.d(TAG_EVENT, "uber first page loaded" + className);
                    uberFirstLogIn();
                } else if (TextUtils.equals(className, watchActivityName[2])) {
                    Log.d(TAG_EVENT, "uber login page loaded" + className);
                    uberLogIn();
                }

                break;

        }
    }

    private void uberFirstLogIn() {
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        Log.d(TAG_EVENT, rootInActiveWindow.getClassName().toString());
        if (null != rootInActiveWindow) {
            List<AccessibilityNodeInfo> accessibilityNodeInfosByText = rootInActiveWindow.findAccessibilityNodeInfosByText(watchStrings[0]);
            accessibilityNodeInfosByText.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }


    private void uberLogIn() {
        if (mSecondPageData.size() == 0)
            return;
        AccessibilityNodeInfo rootInActiveWindow = getRootInActiveWindow();
        List<AccessibilityNodeInfo> loginBtn = rootInActiveWindow.findAccessibilityNodeInfosByText(watchStrings[0]);//登陆
        bianliChild(rootInActiveWindow);
        String user = mSecondPageData.remove(0);
        String passwd = mSecondPageData.remove(0);
        Log.d("TAG", user + " & " + passwd);
        setEditViewText(mReturnEdv.remove(0), user);
        setEditViewText(mReturnEdv.remove(0), passwd);
        loginBtn.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }

    public void bianliChild(AccessibilityNodeInfo info) {
        int childCount = info.getChildCount();
        if (childCount != 0) {
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo child = info.getChild(i);
                if (TextUtils.equals("android.widget.EditText", child.getClassName())) {
                    mReturnEdv.add(child);
                }
                mNodeInfos.add(child);
                Log.d(TAG_EVENT, info.getClassName() + "'s child " + i + " name is ---" + child.getClassName() + " & text is---- " + child.getText());
            }
        }
        if (mNodeInfos.size() != 0) {
            bianliChild(mNodeInfos.remove(0));
        }
    }

    private void setEditViewText(AccessibilityNodeInfo edv, String text) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
        edv.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle);
    }
}
