package test.bluerain.youku.com.xposedtest.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import test.bluerain.youku.com.xposedtest.data.RecordTable;
import test.bluerain.youku.com.xposedtest.db.RecordOpenhelper;

/**
 * Project: LeaksTest.
 * Data: 2016/3/16.
 * Created by 8luerain.
 * Contact:<a href="mailto:8luerain@gmail.com">Contact_me_now</a>
 */
public class ReocrdProvider extends ContentProvider {

    public static final String AUTHORITY = "test.bluerain.youku.com.xposedtest";

    public static final Uri RECORED_URI = Uri.parse("content://test.bluerain.youku.com.xposedtest/" + RecordTable.TABLE_NAME + "/");

    private static final int MAIN = 1;
    private static final int MAIN_ID = 2;

    private RecordOpenhelper mDataBaseHelper;
    private final UriMatcher mUriMatcher;

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + AUTHORITY + "." + RecordTable.TABLE_NAME;

    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + AUTHORITY + "." + RecordTable.TABLE_NAME;

    public ReocrdProvider() {
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(AUTHORITY, RecordTable.TABLE_NAME, MAIN);
        mUriMatcher.addURI(AUTHORITY, RecordTable.TABLE_NAME + "/#", MAIN_ID);
    }

    @Override
    public boolean onCreate() {
        mDataBaseHelper = new RecordOpenhelper(getContext(), RecordTable.DB_NAME, null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch (mUriMatcher.match(uri)) {
            case MAIN:

                break;
            case MAIN_ID:
                selectionArgs = DatabaseUtils.appendSelectionArgs(selectionArgs,
                        new String[]{uri.getLastPathSegment()});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDataBaseHelper.getReadableDatabase();

        Cursor c = db.query(RecordTable.TABLE_NAME, null, selection,
                selectionArgs, null, null, null);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                return CONTENT_TYPE;
            case MAIN_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (mUriMatcher.match(uri) != MAIN) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();

        long rowId = db.insert(RecordTable.TABLE_NAME, null, values);

        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(
                    RECORED_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        String finalWhere;
        int count = 0;
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                count = db.delete(RecordTable.TABLE_NAME, whereClause, whereArgs);
                break;
            case MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(RecordTable._ID + " = "
                        + ContentUris.parseId(uri), whereClause);
                count = db.delete(RecordTable.TABLE_NAME, finalWhere, whereArgs);
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        int count = 0;
        String finalWhere;
        switch (mUriMatcher.match(uri)) {
            case MAIN:
                count = db.update(RecordTable.TABLE_NAME, values, whereClause,
                        whereArgs);
                break;
            case MAIN_ID:
                finalWhere = DatabaseUtils.concatenateWhere(RecordTable._ID + " = "
                        + ContentUris.parseId(uri), whereClause);
                count = db.update(RecordTable.TABLE_NAME, values, finalWhere,
                        whereArgs);
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
