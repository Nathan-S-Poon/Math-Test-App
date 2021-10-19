package curtin.edu.mathtestapp.model;

import java.util.Comparator;

public class TestSorter implements Comparator<TestResult>
{
    //-1 if r1 < r2
    //0 if equal
    //+1 if r1 > r2
    @Override
    public int compare(TestResult r1, TestResult r2)
    {
        String[] scoreStr1 = r1.getScore().split("/");
        String[] scoreStr2 = r2.getScore().split("/");
        int score1 = Integer.parseInt(scoreStr1[0]);
        int total1 = Integer.parseInt(scoreStr1[1]);
        int score2 = Integer.parseInt(scoreStr2[0]);
        int total2 = Integer.parseInt(scoreStr2[1]);
        if(total1 == 0 || total2 == 0)
        {
            if(total1 == 0 && total2 ==0)
            {
                return 0;
            }
            if(total1 == 0)
            {
                if(score2 > 0)
                {
                    return 1;
                }
                else if(score2 < 0)
                {
                    return -1;
                }
                else
                {
                    return -1;
                }
            }
            else
            {
                if(score1 > 0)
                {
                    return -1;
                }
                else if(score1 < 0)
                {
                    return 1;
                }
                else
                {
                    return 1;
                }
            }
        }
        if(total1 != 0 && total2 != 0)
        {
            double per1 = score1 / (double) total1;
            double per2 = score2 / (double) total2;
            if (per1 > per2)
            {
                return -1;
            } else if (per2 > per1)
            {
                return 1;
            } else
            {
                return 0;
            }
        }
        return 0;

    }



}
