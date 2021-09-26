package curtin.edu.mathtestapp.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import curtin.edu.mathtestapp.databases.StudentAndPhoneDbHelper.*;
import curtin.edu.mathtestapp.databases.StudentAndPhoneDbSchema.*;

public class StudentAndPhoneDbHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;;
    private static final String DATABASE_NAME = "students_and_phones.db";

    public StudentAndPhoneDbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + StudentAndPhoneTable.NAME + "(" +
                StudentAndPhoneTable.Cols.ID + " INTEGER, " +
                StudentAndPhoneTable.Cols.PHONE + " INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int v1, int v2)
    {

    }

}
