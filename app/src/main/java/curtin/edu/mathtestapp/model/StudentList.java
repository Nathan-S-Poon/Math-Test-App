package curtin.edu.mathtestapp.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import curtin.edu.mathtestapp.databases.EmailDbHelper;
import curtin.edu.mathtestapp.databases.StudentAndPhoneDbHelper;
import curtin.edu.mathtestapp.databases.StudentAndPhoneDbHelper.*;
import curtin.edu.mathtestapp.databases.StudentAndPhoneDbSchema.*;
import curtin.edu.mathtestapp.databases.StudentDbHelper;
import curtin.edu.mathtestapp.databases.StudentDbSchema.*;
import curtin.edu.mathtestapp.databases.StudentDbHelper.*;
import java.util.ArrayList;
import curtin.edu.mathtestapp.databases.EmailDbSchema.*;
import curtin.edu.mathtestapp.databases.EmailDbHelper.*;

public class StudentList
{
    private ArrayList<Student> list;
    private SQLiteDatabase db;
    private SQLiteDatabase phDb;//phone database
    private SQLiteDatabase eDb;

    public StudentList()
    {
        list = new ArrayList<Student>();
    }

    //get student based on id
    public Student findStudent(int id)
    {
        Student found = null;
        for(Student cur : list)
        {
            if(id == cur.getID())
            {
                found = cur;
            }
        }
        return found;
    }
    public int getSize()
    {
        return list.size();
    }


    public void addStudent(Student student)
    {
        list.add(student);
        addStudentDb(student);
    }

    public int findHighestId()
    {
        int id = 0;
        for(Student cur : list)
        {
            if(id < cur.getID())
            {
                id = cur.getID();
            }
        }
        return id;
    }

    //Databases
    public void load(Context context)
    {
        this.db = new StudentDbHelper(context.getApplicationContext()).getWritableDatabase();
        this.phDb = new StudentAndPhoneDbHelper(context.getApplicationContext()).getWritableDatabase();
        this.eDb = new EmailDbHelper(context.getApplicationContext()).getWritableDatabase();
        StudentCursor cursor = getCursor();
        EmailCursor eCursor = getEmailCursor();
        PhoneCursor phCursor = getPhoneCursor();
        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                list.add(cursor.getStudent());
                cursor.moveToNext();
            }
        }
        finally
        {
            cursor.close();
        }
        try//add phone numbers
        {
            phCursor.moveToFirst();
            while(!phCursor.isAfterLast())
            {
                phCursor.getPhone();
                phCursor.moveToNext();
            }
        }
        finally
        {
            phCursor.close();
        }
        try //emails add
        {
            eCursor.moveToFirst();
            while(!eCursor.isAfterLast())
            {
                eCursor.getEmail();
                eCursor.moveToNext();
            }
        }
        finally
        {
            phCursor.close();
        }

    }
    /******
     *Database getters and setters
     */
    private void addStudentDb(Student student)
    {
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.Cols.ID, student.getID());
        cv.put(StudentTable.Cols.FIRST, student.getFirstName());
        cv.put(StudentTable.Cols.LAST, student.getLastName());
        cv.put(StudentTable.Cols.PHOTO, student.getPhoto());
        db.insert(StudentTable.NAME, null, cv);
        //add phone numbers in
        addPhoneDb(student);
        addEmailDb(student);
    }
    private void addPhoneDb(Student student)
    {   //add each number
        for(int i = 0; i < student.getNumbers().size(); i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(StudentAndPhoneTable.Cols.ID, student.getID());
            cv.put(StudentAndPhoneTable.Cols.PHONE, student.getNumbers().get(i));
            phDb.insert(StudentAndPhoneTable.NAME, null, cv);
        }
    }
    private void addEmailDb(Student student)
    {   //add each email
        for(int i = 0; i < student.getEmails().size(); i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(EmailTable.Cols.ID, student.getID());
            cv.put(EmailTable.Cols.EMAIL, student.getEmails().get(i));
            eDb.insert(EmailTable.NAME, null, cv);
        }
    }
    private void addAnEmailDb(String name, String num)
    {
        ContentValues cv = new ContentValues();
        cv.put(EmailTable.Cols.ID, name);
        cv.put(EmailTable.Cols.EMAIL, num);
        eDb.insert(EmailTable.NAME, null, cv);
    }

    private void addAPhoneNumDb(String name, String num)
    {
        ContentValues cv = new ContentValues();
        cv.put(StudentAndPhoneTable.Cols.ID, name);
        cv.put(StudentAndPhoneTable.Cols.PHONE, num);
        phDb.insert(StudentAndPhoneTable.NAME, null, cv);
    }

    private void removeStudentDb(Student student)
    {
        String[] whereValue = {String.valueOf(student.getID())};
        db.delete(StudentTable.NAME, StudentTable.Cols.ID + " = ?", whereValue);
        //remove phones
        removePhoneDb(student);
        removeEmailDb(student);
    }
    private void removePhoneDb(Student student)//removes all phone numbers of that student
    {
        String[] whereValue = {String.valueOf(student.getID())};
        phDb.delete(StudentAndPhoneTable.NAME, StudentAndPhoneTable.Cols.ID + " = ?", whereValue);
        removeEmailDb(student);
    }
    private void removeEmailDb(Student student)//removes all phone numbers of that student
    {
        String[] whereValue = {String.valueOf(student.getID())};
        eDb.delete(EmailTable.NAME, EmailTable.Cols.ID + " = ?", whereValue);
    }

    //remove a single number
    private void removeAnEmailDb(String name, String email)
    {
        String[] whereValue = {String.valueOf(name), String.valueOf(email)};
        eDb.delete(EmailTable.NAME, EmailTable.Cols.ID + " = ?" +
                " AND " + EmailTable.Cols.EMAIL + " = ?", whereValue);
    }

    //remove a single number
    private void removeAPhoneNum(String name, String number)
    {
        String[] whereValue = {String.valueOf(name), String.valueOf(number)};
        phDb.delete(StudentAndPhoneTable.NAME ,StudentAndPhoneTable.Cols.ID + " = ?" +
                " AND " + StudentAndPhoneTable.Cols.PHONE + " = ?", whereValue);
    }

    private void updateFactionDb(String old, Student student)
    {
        ContentValues cv = new ContentValues();
        cv.put(StudentTable.Cols.ID, student.getID());
        cv.put(StudentTable.Cols.FIRST, student.getFirstName());
        cv.put(StudentTable.Cols.LAST, student.getLastName());
        cv.put(StudentTable.Cols.PHOTO, student.getPhoto());
        String[] whereValue = {String.valueOf(old)};
        db.update(StudentTable.NAME, cv, StudentTable.Cols.ID + " = ?", whereValue);
        //update phones
        if(!old.equals(student.getID()))
        {
            updatePhoneDb(old, student);
            updateEmailDb(old, student);
        }
    }

    private void updatePhoneDb(String old, Student student)
    {
        for(int i = 0; i < student.getNumbers().size(); i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(StudentAndPhoneTable.Cols.ID, student.getID());
            cv.put(StudentAndPhoneTable.Cols.PHONE, student.getNumbers().get(i));
            String[] whereValue = {String.valueOf(old), String.valueOf(student.getNumbers().get(i))};
            phDb.update(StudentAndPhoneTable.NAME,cv ,StudentAndPhoneTable.Cols.ID + " = ?" +
                    " AND " + StudentAndPhoneTable.Cols.PHONE + " = ?", whereValue);
        }
    }

    private void updateEmailDb(String old, Student student)
    {
        for(int i = 0; i < student.getNumbers().size(); i++)
        {
            ContentValues cv = new ContentValues();
            cv.put(EmailTable.Cols.ID, student.getID());
            cv.put(EmailTable.Cols.EMAIL, student.getEmails().get(i));
            String[] whereValue = {String.valueOf(old), String.valueOf(student.getEmails().get(i))};
            eDb.update(EmailTable.NAME, cv, EmailTable.Cols.ID + " = ?" +
                    " AND " + EmailTable.Cols.EMAIL + " = ?", whereValue);
        }
    }

    /*****************************
     * Cursor subclass
     */
    public StudentCursor getCursor()
    {
        StudentCursor cursor = new StudentCursor(db.query(StudentTable.NAME,
                null,null,null,null,null,null));
        return cursor;
    }
    public class StudentCursor extends CursorWrapper
    {
        public StudentCursor(Cursor cursor)
        {
            super(cursor);
        }
        public Student getStudent()
        {
            int id = getInt(getColumnIndex(StudentTable.Cols.ID));
            String first = getString(getColumnIndex(StudentTable.Cols.FIRST));
            String last = getString(getColumnIndex(StudentTable.Cols.LAST));
            int photo = getInt(getColumnIndex(StudentTable.Cols.PHOTO));
            return new Student(id, first, last, photo);
        }
    }
    //for iterating StudentAndPhone database
    public PhoneCursor getPhoneCursor()
    {
        PhoneCursor cursor = new PhoneCursor(phDb.query(StudentAndPhoneTable.NAME,
                null,null,null,null,null,null));
        return cursor;
    }
    public class PhoneCursor extends CursorWrapper
    {
        public PhoneCursor(Cursor cursor)
        {
            super(cursor);
        }
        public void getPhone()
        {
            int id = getInt(getColumnIndex(StudentAndPhoneTable.Cols.ID));
            int phone = getInt(getColumnIndex(StudentAndPhoneTable.Cols.PHONE));
            findStudent(id).addPhone(phone);
        }
    }

    //for iterating StudentAndPhone database
    public EmailCursor getEmailCursor()
    {
        EmailCursor cursor = new EmailCursor(eDb.query(EmailTable.NAME,
                null,null,null,null,null,null));
        return cursor;
    }
    public class EmailCursor extends CursorWrapper
    {
        public EmailCursor(Cursor cursor)
        {
            super(cursor);
        }
        public void getEmail()
        {
            int id = getInt(getColumnIndex(EmailTable.Cols.ID));
            String email = getString(getColumnIndex(EmailTable.Cols.EMAIL));
            findStudent(id).addEmail(email);
        }
    }




}
