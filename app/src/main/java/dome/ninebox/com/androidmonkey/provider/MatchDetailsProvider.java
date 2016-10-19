package dome.ninebox.com.androidmonkey.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import dome.ninebox.com.androidmonkey.db.DotaMaxDBHelper;

/**
 * Created by Administrator on 2016/10/10.
 */

public class MatchDetailsProvider extends ContentProvider {
    private static final String AUTHORITY="dome.ninebox.com.androidmonkey.provider.MatchDetailsProvider";
    public static final Uri URI_DOTA_ALL=Uri.parse("content://"+AUTHORITY+"/Match");

    private DotaMaxDBHelper helper;
    private SQLiteDatabase db;
    private static UriMatcher matcher;

    private static final int DOTA_ALL=0;
    private static final int DOTA_ONE=1;

    static {
        matcher=new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY,"Match/",DOTA_ALL);
        matcher.addURI(AUTHORITY,"Match/#",DOTA_ONE);
    }

    @Override
    public boolean onCreate() {
        helper=DotaMaxDBHelper.getInstance(getContext());
        return true;
    }




    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (matcher.match(uri)){

            case DOTA_ALL:

                break;
            case DOTA_ONE:
                long id= ContentUris.parseId(uri);
                selection="_id=?";
                selectionArgs=new String[]{String.valueOf(id)};
                break;
            default:
                throw new IllegalArgumentException("Wrong Uri:"+uri);
        }

        db=helper.getReadableDatabase();
        Cursor cursor=db.query(DotaMaxDBHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),URI_DOTA_ALL);
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (matcher.match(uri)!=DOTA_ALL){
            throw new IllegalArgumentException("Wrong Uri:"+uri);
        }
        db=helper.getReadableDatabase();
        long rowId = db.insert(DotaMaxDBHelper.TABLE_NAME, null, values);
        if (rowId>0){
            notifyDataSetChanged();
            return ContentUris.withAppendedId(uri,rowId);
        }

        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private void notifyDataSetChanged() {
        getContext().getContentResolver().notifyChange(URI_DOTA_ALL,null);
    }
}
