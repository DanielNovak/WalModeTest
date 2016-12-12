package test.lock.database.walmodetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class TestDatabaseHelper extends SQLiteOpenHelper {

    private static TestDatabaseHelper sInstance;

    public synchronized static TestDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TestDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private TestDatabaseHelper(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE test2 (_id integer primary key autoincrement, value text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.enableWriteAheadLogging();
    }

    public void insertValue(String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("value", text);
        getWritableDatabase().insert("test2", null, contentValues);
    }

    public String readValue() {
        Cursor cursor = getWritableDatabase().query("test2", new String[]{"_id, value"},null, null, null, null, "_id DESC", "1");
        cursor.moveToFirst();
        final String value = cursor.getString(cursor.getColumnIndex("value"));
        cursor.close();
        return value;
    }
}
