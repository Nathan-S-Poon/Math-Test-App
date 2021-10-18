package curtin.edu.mathtestapp.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import curtin.edu.mathtestapp.databases.StudentDbHelper;

public class Student implements Parcelable
{
    private int ID;
    private String firstName;
    private String lastName;
    private String photo;
    private ArrayList<Integer> numbers;
    private ArrayList<String> emails;

    public Student()
    {
        ID = 0;
        firstName = "";
        lastName = "";
        photo = "";
        numbers = new ArrayList<Integer>();
        emails = new ArrayList<String>();
    }

    public Student(int ID, String first, String last, String photo)
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


    /*Parcelable code*/
    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>()
    {
        @Override
        public Student createFromParcel(Parcel in)
        {
            return new Student(in);
        }

        @Override
        public Student[] newArray(int size)
        {
            return new Student[size];
        }
    };
    protected Student(Parcel in)
    {
        this.ID = in.readInt();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.photo = in.readString();
        this.numbers = in.readArrayList(null);
        this.emails = in.readArrayList(null);
    }
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(ID);
        out.writeString(firstName);
        out.writeString(lastName);
        out.writeString(photo);
        out.writeList(numbers);
        out.writeList(emails);
    }
    public int describeContents()
    {
        return 0;
    }
    //parcelable code end


    //Getters and setters

    public int getID()
    {
        return ID;
    }

    public String getPhoto()
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
    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }
}



