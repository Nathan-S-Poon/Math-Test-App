package curtin.edu.mathtestapp.model;

import android.icu.text.DateFormat;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;

public class TestResult implements Parcelable
{
    private String name;
    private int id;
    private String date;
    private String score;//Format score/total
    private int time;

    public TestResult(int id, String name, String date, String score, int time)
    {
        this.id = id;
        this.name = name;
        this.date = date;
        this.score = score;
        this.time = time;
    }

    public String toString()
    {
        String str = "name: " + name + " date: " + date + " score: " + score + " time in s: " + time;
        return str;
    }


    /*Parcelable code*/
    public static final Parcelable.Creator<TestResult> CREATOR = new Parcelable.Creator<TestResult>()
    {
        @Override
        public TestResult createFromParcel(Parcel in)
        {
            return new TestResult(in);
        }

        @Override
        public TestResult[] newArray(int size)
        {
            return new TestResult[size];
        }
    };
    protected TestResult(Parcel in)
    {
        this.id = in.readInt();
        this.name = in.readString();
        this.date = in.readString();
        this.score = in.readString();
        this.time = in.readInt();
    }
    public void writeToParcel(Parcel out, int flags)
    {
        out.writeInt(id);
        out.writeString(name);
        out.writeString(date);
        out.writeString(score);
        out.writeInt(time);
    }
    public int describeContents()
    {
        return 0;
    }
    //parcelable code end

    //getters and setters

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }
    public String getScore()
    {
        return score;
    }
    public String getName()
    {
        return name;
    }
    public String getDate()
    {
        return date;
    }
    public int getTime()
    {
        return time;
    }
}

