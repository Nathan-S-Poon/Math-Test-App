package curtin.edu.mathtestapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import curtin.edu.mathtestapp.databases.EmailDbSchema.*;

public class EmailDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;;
    private static final String DATABASE_NAME = "emails.db";

    public EmailDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + EmailTable.NAME + "(" +
                EmailTable.Cols.ID + " INTEGER, " +
                EmailTable.Cols.EMAIL + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2)
    {

    }


}
