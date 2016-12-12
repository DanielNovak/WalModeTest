package test.lock.database.walmodetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TestDatabaseHelper databaseHelper =TestDatabaseHelper.getInstance(getApplicationContext());
        databaseHelper.insertValue("1");

        //start long running transaction
        new Thread() {
            @Override
            public void run() {
                databaseHelper.getWritableDatabase().beginTransactionNonExclusive();
                databaseHelper.insertValue("2");
                Log.e("wal", "First thread written 2");

                //make it a long running transaction
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) { }

                databaseHelper.getWritableDatabase().setTransactionSuccessful();
                databaseHelper.getWritableDatabase().endTransaction();
            }
        }.start();

        //start parallel transaction

        new Thread() {
            @Override
            public void run() {
                databaseHelper.getWritableDatabase().beginTransactionNonExclusive();
                Log.e("wal", "Second thread read: " + databaseHelper.readValue());
                databaseHelper.getWritableDatabase().setTransactionSuccessful();
                databaseHelper.getWritableDatabase().endTransaction();
            }
        }.start();
    }
}
