package test.bluerain.youku.com.xposedtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test.bluerain.youku.com.xposedtest.R;
import test.bluerain.youku.com.xposedtest.data.RandomBean;

/**
 * Created by rain on 2016/3/21.
 */
public class SecondFragmentListViewAdapter extends BaseAdapter {
    private List<RandomBean.DataBean> mDataBeans;
    private Context mContext;
    private int mLayoutId;
    private LayoutInflater mInflater;

    public SecondFragmentListViewAdapter(List<RandomBean.DataBean> dataBeans, Context mContext, int layoutId) {
        if (null == dataBeans)
            dataBeans = new ArrayList<>();
        this.mDataBeans = dataBeans;
        this.mContext = mContext;
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setmDataBeans(List<RandomBean.DataBean> mDataBeans) {
        this.mDataBeans = mDataBeans;
    }

    @Override
    public int getCount() {
        return mDataBeans.size();
    }

    @Override
    public RandomBean.DataBean getItem(int position) {
        return mDataBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (null != convertView) {
            view = convertView;
        } else {
            view = mInflater.inflate(mLayoutId, null);
        }
        TextView textView_ItemName = (TextView) view.findViewById(R.id.id_txv_item_name_second_fragment);
        EditText textView_ItemValue = (EditText) view.findViewById(R.id.id_txv_item_value_second_fragment);
        textView_ItemName.setText(mDataBeans.get(position).itemName);
        textView_ItemValue.setText(mDataBeans.get(position).itemValue);

        return view;
    }
}
