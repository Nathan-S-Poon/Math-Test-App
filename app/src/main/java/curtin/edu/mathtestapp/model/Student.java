package curtin.edu.mathtestapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import curtin.edu.mathtestapp.databases.StudentDbHelper;

public class Student
{
    private int ID;
    private String firstName;
    private String lastName;
    private int photo;
    private ArrayList<Integer> numbers;
    private ArrayList<String> emails;

    public Student(int ID, String first, String last, int photo)
    {
        this.ID = ID;
        firstName = first;
        lastName = last;
        this.photo = photo;
        numbers = new ArrayList<Integer>();
        emails = new ArrayList<String>();
    }

    public void addPhone(int phone)
    {
        numbers.add(phone);
    }
    public void addEmail(String email)
    {
        emails.add(email);
    }


    //Getters and setters

    public int getID()
    {
        return ID;
    }

    public int getPhoto()
    {
        return photo;
    }
    public ArrayList<Integer> getNumbers()
    {
        return numbers;
    }
    public ArrayList<String> getEmails()
    {
        return emails;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public void setEmails(ArrayList<String> emails)
    {
        this.emails = emails;
    }
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    public void setNumbers(ArrayList<Integer> numbers)
    {
        this.numbers = numbers;
    }
    public void setPhoto(int photo)
    {
        this.photo = photo;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }
}



