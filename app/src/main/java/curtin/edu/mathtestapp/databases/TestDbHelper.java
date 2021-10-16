package curtin.edu.mathtestapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import curtin.edu.mathtestapp.databases.TestDbSchema.*;

public class TestDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;;
    private static final String DATABASE_NAME = "tests.db";

    public TestDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TestTable.NAME + "(" +
                TestTable.Cols.ID + " INTEGER, " +
                TestTable.Cols.NAME + " TEXT, " +
                TestTable.Cols.DATE + " TEXT, " +
                TestTable.Cols.SCORE + " TEXT, " +
                TestTable.Cols.TIME + " INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2)
    {

    }
}

