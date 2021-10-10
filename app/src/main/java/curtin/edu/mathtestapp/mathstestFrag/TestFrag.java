package curtin.edu.mathtestapp.mathstestFrag;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import curtin.edu.mathtestapp.R;

public class TestFrag extends Fragment
{
    private Button multi1;
    private Button multi2;
    private Button multi3;
    private Button multi4;
    private Button multiTri;
    private Button nextOption;
    private Button prevOption;
    private TextView multiAns;
    private TextView question;
    private EditText inputAns;
    private Button next;
    private ArrayList<String> choiceList;
    private int listIndex;
    private int endIndex;

    private void setTwo(String ans1, String ans2)
    {
        multi1.setVisibility(View.VISIBLE);
        multi2.setVisibility(View.VISIBLE);
        multi3.setVisibility(View.INVISIBLE);
        multi4.setVisibility(View.INVISIBLE);
        multiTri.setVisibility(View.INVISIBLE);
        multi1.setText(ans1);
        multi2.setText(ans2);
    }
    private void setThree(String ans1, String ans2, String ans3)
    {
        multi1.setVisibility(View.INVISIBLE);
        multi2.setVisibility(View.INVISIBLE);
        multi3.setVisibility(View.VISIBLE);
        multi4.setVisibility(View.VISIBLE);
        multiTri.setVisibility(View.VISIBLE);
        multi3.setText(ans2);
        multi4.setText(ans3);
        multiTri.setText(ans1);
    }
    private void setFour(String ans1, String ans2, String ans3, String ans4)
    {
        multi1.setVisibility(View.VISIBLE);
        multi2.setVisibility(View.VISIBLE);
        multi3.setVisibility(View.VISIBLE);
        multi4.setVisibility(View.VISIBLE);
        multiTri.setVisibility(View.INVISIBLE);
        multi1.setText(ans1);
        multi2.setText(ans2);
        multi3.setText(ans3);
        multi4.setText(ans4);
    }


    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.test_layout, ui, false);
        multi1 = (Button) view.findViewById(R.id.multi1);
        multi2 = (Button) view.findViewById(R.id.multi2);
        multi3 = (Button) view.findViewById(R.id.multi3);
        multi4 = (Button) view.findViewById(R.id.multi4);
        multiTri = (Button) view.findViewById(R.id.multiTri);
        multiAns = (TextView) view.findViewById(R.id.selectMultiChoice);
        question = (TextView) view.findViewById(R.id.questionText);
        next = (Button) view.findViewById(R.id.nextButton);
        nextOption = (Button) view.findViewById(R.id.nextChoice);
        prevOption = (Button) view.findViewById(R.id.prevChoice);

        //set up multi choice
        if(choiceList.size() < 2)
        {
            //discard
        }
        else if(choiceList.size() == 2)
        {
            setTwo(choiceList.get(0), choiceList.get(1));
            nextOption.setVisibility(View.INVISIBLE);
            prevOption.setVisibility(View.INVISIBLE);
        }
        else if(choiceList.size() == 3)
        {
            setThree(choiceList.get(0), choiceList.get(1), choiceList.get(2));
            nextOption.setVisibility(View.INVISIBLE);
            prevOption.setVisibility(View.INVISIBLE);
        }
        else
        {
            nextOption.setVisibility(View.VISIBLE);
            prevOption.setVisibility(View.VISIBLE);
            listIndex = 0;
            endIndex = 3;
            setFour(choiceList.get(0), choiceList.get(1), choiceList.get(2), choiceList.get(3));
        }

        nextOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(choiceList.size() - endIndex - 1 == 2)//If two options left
                {
                    listIndex = endIndex + 1;
                    endIndex = choiceList.size()-1;
                    setTwo(choiceList.get(listIndex), choiceList.get(endIndex));
                }
                else if (choiceList.size() - endIndex - 1 == 3)
                {
                    listIndex = endIndex + 1;
                    endIndex = choiceList.size()-1;
                    setThree(choiceList.get(listIndex), choiceList.get(listIndex+1), choiceList.get(endIndex));
                }
                else
                {
                    listIndex = endIndex + 1;
                    endIndex = endIndex + 4;
                    setFour(choiceList.get(listIndex), choiceList.get(listIndex+1), choiceList.get(listIndex+2), choiceList.get(endIndex));
                }
            }
        });
        prevOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });


        multi1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                multiAns.setText(multi1.getText());
            }
        });
        multi2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                multiAns.setText(multi2.getText());
            }
        });
        multi3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                multiAns.setText(multi3.getText());
            }
        });
        multi4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                multiAns.setText(multi4.getText());
            }
        });
        multiTri.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                multiAns.setText(multiTri.getText());
            }
        });


        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //validate answer and record score
                //change to next question
            }
        });


        return view;
    }




    /******************************
    Async Task subclass
    ********************************/
    private class FetchQuestion extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... params)
        {
            String result = "";

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {

        }

    }







}
