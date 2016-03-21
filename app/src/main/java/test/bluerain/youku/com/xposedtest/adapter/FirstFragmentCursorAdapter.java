package test.bluerain.youku.com.xposedtest.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

/**
 * Project: XposedTest.
 * Data: 2016/3/21.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class FirstFragmentCursorAdapter extends SimpleCursorAdapter {

    public FirstFragmentCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }


}
