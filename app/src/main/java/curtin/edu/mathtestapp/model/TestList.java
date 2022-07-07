package curtin.edu.mathtestapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.util.ArrayList;

import curtin.edu.mathtestapp.databases.StudentDbSchema;
import curtin.edu.mathtestapp.databases.TestDbHelper;
import curtin.edu.mathtestapp.databases.TestDbSchema.*;
import curtin.edu.mathtestapp.databases.TestDbHelper.*;


public class TestList
{
    private ArrayList<TestResult> list;
    private SQLiteDatabase db;

    public TestList()
    {
        list = new ArrayList<TestResult>();
    }

    //get results that have
    public ArrayList<TestResult> getResults(int id)
    {
        ArrayList<TestResult> tests = new ArrayList<TestResult>();
        for (TestResult cur : list)
        {
            if (id == cur.getId())
            {
                tests.add(cur);
            }
        }
        return tests;
    }
    public ArrayList<TestResult> getList()
    {
        ArrayList<TestResult> tests = new ArrayList<TestResult>();
        for (TestResult cur : list)
        {

            tests.add(cur);

        }
        return tests;
    }

    public int getSize()
    {
        return list.size();
    }

    public void addResult(TestResult result)
    {
        list.add(result);
        addTestDb(result);
    }

    public void update(int id, String name)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(list.get(i).getId() == id)
            {
                list.get(i).setName(name);
                updateTestDb(list.get(i), name);
            }
        }
    }

    //delete all tests with student id
    public void deleteStudentResults(int id)
    {
        for(int i = 0; i < list.size(); i++)
        {
            if(id == list.get(i).getId())
            {
                removeTestDb(list.get(i));
                list.remove(list.get(i));
            }
        }
    }

    public void deleteResult(TestResult result)
    {
        list.remove(result);
        removeTestDb(result);
    }


    /*******************
     * Databases
     *******************/
    public void load(Context context)
    {
        this.db = new TestDbHelper(context.getApplicationContext()).getWritableDatabase();
        TestCursor cursor = getCursor();
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                list.add(cursor.getTest());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
    }
    private void addTestDb(TestResult test)
    {
        ContentValues cv = new ContentValues();
        cv.put(TestTable.Cols.ID, test.getId());
        cv.put(TestTable.Cols.DATE, test.getDate());
        cv.put(TestTable.Cols.NAME, test.getName());
        cv.put(TestTable.Cols.SCORE, test.getScore());
        cv.put(TestTable.Cols.TIME, test.getTime());
        db.insert(TestTable.NAME, null, cv);
    }

    private void removeTestDb(TestResult test)
    {
        String[] whereValue = {String.valueOf(test.getId())};
        db.delete(TestTable.NAME, TestTable.Cols.ID + " = ?", whereValue);
    }

    private void updateTestDb(TestResult test, String name)
    {
        ContentValues cv = new ContentValues();
        cv.put(TestTable.Cols.ID, test.getId());
        cv.put(TestTable.Cols.DATE, test.getDate());
        cv.put(TestTable.Cols.NAME, name);
        cv.put(TestTable.Cols.SCORE, test.getScore());
        cv.put(TestTable.Cols.TIME, test.getTime());
        String[] whereValue = {String.valueOf(test.getId())};
        db.update(TestTable.NAME, cv, TestTable.Cols.ID + " = ?", whereValue);
    }

    /***************
     * Cursor
     **************/
    public TestCursor getCursor()
    {
        TestCursor cursor = new TestCursor(db.query(TestTable.NAME,null,null,null,null,null,null));
        return cursor;
    }
    public class TestCursor extends CursorWrapper
    {
        public TestCursor(Cursor cursor)
        {
            super(cursor);
        }
        public TestResult getTest()
        {
            int id = getInt(getColumnIndex(TestTable.Cols.ID));
            String name = getString(getColumnIndex(TestTable.Cols.NAME));
            String score = getString(getColumnIndex(TestTable.Cols.SCORE));
            String date = getString(getColumnIndex(TestTable.Cols.DATE));
            int time = getInt(getColumnIndex(TestTable.Cols.TIME));
            return new TestResult(id, name, date, score, time);
        }

    }


}
