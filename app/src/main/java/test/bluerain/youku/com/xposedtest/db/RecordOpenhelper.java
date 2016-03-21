package test.bluerain.youku.com.xposedtest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import test.bluerain.youku.com.xposedtest.data.RecordTable;

/**
 * Project: LeaksTest.
 * Data: 2016/3/15.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class RecordOpenhelper extends SQLiteOpenHelper {
    private Context mContext;


    private static final String CREATE_TABEL;
    private static final String DROP_TABLE;

    static {
        CREATE_TABEL = "create table "
                + RecordTable.TABLE_NAME + " ("
                + RecordTable._ID+" integer primary key autoincrement, "
                + RecordTable.COLNUM_USER + " text, "
                + RecordTable.COLNUM_PHONE + " text "
                + ")";
        DROP_TABLE = "drop table if exists contact";
    }


    public RecordOpenhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        db.execSQL(CREATE_TABEL);
    }
}
