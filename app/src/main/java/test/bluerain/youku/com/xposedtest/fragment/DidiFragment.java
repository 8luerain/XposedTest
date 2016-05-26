package test.bluerain.youku.com.xposedtest.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import test.bluerain.youku.com.xposedtest.R;
import test.bluerain.youku.com.xposedtest.data.RandomBean;
import test.bluerain.youku.com.xposedtest.phone.Shenhua;
import test.bluerain.youku.com.xposedtest.utils.CommonUtils;
import test.bluerain.youku.com.xposedtest.utils.MessageContainer;
import test.bluerain.youku.com.xposedtest.utils.Profile;

/**
 * Project: XposedTest.
 * Data: 2016/5/25.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class DidiFragment extends android.support.v4.app.Fragment {
    private static final String TAG = "DidiFragment";

    private TextView mTextViewBegain;

    private TextView mTextViewGetPhone;
    private TextView mTextViewClearData;

    private ProgressBar mProgressBarGetPhone;
    private ProgressBar mProgressBarClearData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_didi, null);
        initView(view);
        initEventDeliver();
        return view;
    }

    private void initView(View view) {
        mTextViewBegain = (TextView) view.findViewById(R.id.id_txv_didi_title);

        mTextViewGetPhone = (TextView) view.findViewById(R.id.textView_phone_num);
        mTextViewClearData = (TextView) view.findViewById(R.id.textView_clean_data);

        mProgressBarGetPhone = (ProgressBar) view.findViewById(R.id.progressBar_didi_get_phone);
        mProgressBarClearData = (ProgressBar) view.findViewById(R.id.progressBar_didi_clean_data);
    }

    private void initEventDeliver() {
        mTextViewBegain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetViewState();
                getPhoneNum();
            }
        });
    }

    /**
     * 获取手机号
     */
    private void getPhoneNum() {
        mProgressBarGetPhone.setVisibility(View.VISIBLE);
        if (null == Shenhua.sShenhuaUserInfo) {
            Shenhua.login(new Shenhua.LoginListener() {
                @Override
                public void loginSuccess(final String token) {
                    Shenhua.getPhoneNumByItem(Shenhua.DIDI_SEND, token, "1", "", Shenhua.AREA_BEIJING, Shenhua.PHONE_TYPE_RANDOM, new Shenhua
                            .GetPhoneListener() {
                        @Override
                        public void getPhoneSuccess(List<String> phoneNums) {
                            setGetPhoneNumState(phoneNums.get(0));
                            MessageContainer.sCurrentPhoneNum = phoneNums.get(0);
                            cleanData();
                        }

                        @Override
                        public void getPhoneFailed(String response) {
                            setGetPhoneNumFailedState("卧槽，获取号码失败了。。");
                        }
                    });
                }

                @Override
                public void loginFailed(String response) {
                    setGetPhoneNumFailedState("卧槽，登陆都失败了。。");
                }
            });
        } else {
            Shenhua.getPhoneNumByItem(Shenhua.DIDI_SEND, Shenhua.sShenhuaUserInfo.token, "1", "", Shenhua.AREA_BEIJING, Shenhua
                    .PHONE_TYPE_RANDOM, new Shenhua
                    .GetPhoneListener() {
                @Override
                public void getPhoneSuccess(List<String> phoneNums) {
                    setGetPhoneNumState(phoneNums.get(0));
                    MessageContainer.sCurrentPhoneNum = phoneNums.get(0);
                    cleanData();
                }

                @Override
                public void getPhoneFailed(String response) {
                    setGetPhoneNumFailedState("卧槽，获取号码失败了。。");
                }
            });
        }
    }


    private void cleanData() {
        mProgressBarClearData.setVisibility(View.VISIBLE);
        RandomBean bean = new RandomBean();
        CommonUtils.saveBeanToFile(Profile.sRandomFile, bean);
        CommonUtils.didiEverythingNew(getActivity());
        mProgressBarClearData.setVisibility(View.INVISIBLE);
        mTextViewClearData.setText("清理完成，开始登陆");
    }


    private void setGetPhoneNumState(String phoneNum) {
        mProgressBarGetPhone.setVisibility(View.INVISIBLE);
        mTextViewGetPhone.setVisibility(View.VISIBLE);
        mTextViewGetPhone.setText(phoneNum);
        CommonUtils.copy(phoneNum, getContext());
        Toast.makeText(getContext(), "已经复制到剪切板", Toast.LENGTH_SHORT).show();
    }

    private void setGetPhoneNumFailedState(String errMsg) {
        mProgressBarGetPhone.setVisibility(View.INVISIBLE);
        mTextViewGetPhone.setVisibility(View.VISIBLE);
        mTextViewGetPhone.setText(errMsg);
    }

    private void resetViewState() {
        mTextViewGetPhone.setText("");
        mTextViewClearData.setText("");
        mProgressBarGetPhone.setVisibility(View.INVISIBLE);
        mProgressBarClearData.setVisibility(View.INVISIBLE);
    }

}


