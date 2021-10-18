package curtin.edu.mathtestapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import curtin.edu.mathtestapp.databases.StudentDbSchema.*;

public class  StudentDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;;
    private static final String DATABASE_NAME = "students.db";

    public StudentDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
         db.execSQL("CREATE TABLE " + StudentTable.NAME + "(" +
                 StudentTable.Cols.ID + " INTEGER, " +
                 StudentTable.Cols.FIRST + " TEXT, " +
                 StudentTable.Cols.LAST + " TEXT, " +
                 StudentTable.Cols.PHOTO + " TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2)
    {

    }


}
