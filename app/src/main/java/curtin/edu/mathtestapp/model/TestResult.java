package curtin.edu.mathtestapp.model;

import android.icu.text.DateFormat;
import java.time.LocalDateTime;

public class TestResult
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

