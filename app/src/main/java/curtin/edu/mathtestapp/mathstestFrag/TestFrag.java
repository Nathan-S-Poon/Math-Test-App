package curtin.edu.mathtestapp.mathstestFrag;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.Student;

public class TestFrag extends Fragment
{
    private static final String TAG = "TestFrag";
    private static final String API_KEY = "01189998819991197253";

    private Toast toast;
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
    private Button endButton;
    private Button next;
    private ProgressBar timerBar;
    private ArrayList<String> choiceList;
    private String name;
    private int listIndex;
    private int endIndex;
    private int questAns;
    private int timeToSolve;
    private int currTime;
    private int score;
    private int totalMarks;
    private TextView totalScore;
    private TextView totalTime;
    private boolean loading;

    private void setTestLayout()
    {
        System.out.println(choiceList.size());
        //set up multi choice
        if(choiceList.size() == 1)
        {
            //discard
        }
        else if(choiceList.size() == 0)
        {
            setInput();
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
    }

    private void setInput()
    {
        multi1.setVisibility(View.INVISIBLE);
        multi2.setVisibility(View.INVISIBLE);
        multi3.setVisibility(View.INVISIBLE);
        multi4.setVisibility(View.INVISIBLE);
        multiTri.setVisibility(View.INVISIBLE);
        inputAns.setVisibility(View.VISIBLE);
    }
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

    private void testError(String message)
    {
        Bundle result = new Bundle();
        result.putString("errormessage", message);
        getParentFragmentManager().setFragmentResult("errorTest", result);
        FragmentManager fm = getParentFragmentManager();
        StudentSelectionFrag frag = (StudentSelectionFrag) fm.findFragmentById(R.id.studentRecyclerLayout);
        if(frag == null)
        {
            frag = new StudentSelectionFrag();
            fm.beginTransaction().replace(R.id.frame, frag).commit();
        }
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
        choiceList = new ArrayList<String>();
        currTime = 0;
        score = 0;
        totalMarks = 0;
        getParentFragmentManager().setFragmentResultListener("viewToTest", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                name = result.getString("firstname");
                name = name + " " + result.getString("lastname");
            }
        });
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
        endButton = (Button) view.findViewById(R.id.endButton);
        timerBar = (ProgressBar) view.findViewById(R.id.timerBar);
        inputAns = (EditText) view.findViewById(R.id.inputAnswer);
        totalScore = (TextView) view.findViewById(R.id.scoreText);
        totalTime = (TextView) view.findViewById(R.id.timeText);
        inputAns.setVisibility(View.INVISIBLE);
        totalTime.setText("0");
        totalScore.setText("0/0");

        //get first question
        loading = true;
        toast = toast.makeText(getActivity().getApplicationContext(),"fetching question"
                , Toast.LENGTH_LONG);
        toast.show();
        new FetchQuestion().execute();

        endButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //store details

                //end test
                testError("Test ended");
            }
        });

        nextOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    //TODO what if only one question left
                    if (choiceList.size() - endIndex - 1 == 2)//If two options left
                    {
                        listIndex = endIndex + 1;
                        endIndex = choiceList.size() - 1;
                        setTwo(choiceList.get(listIndex), choiceList.get(endIndex));
                    } else if (choiceList.size() - endIndex - 1 == 3)
                    {
                        listIndex = endIndex + 1;
                        endIndex = choiceList.size() - 1;
                        setThree(choiceList.get(listIndex), choiceList.get(listIndex + 1), choiceList.get(endIndex));
                    } else
                    {
                        listIndex = endIndex + 1;
                        endIndex = endIndex + 4;
                        setFour(choiceList.get(listIndex), choiceList.get(listIndex + 1), choiceList.get(listIndex + 2), choiceList.get(endIndex));
                    }
                    if (endIndex == choiceList.size() - 1)
                    {
                        nextOption.setVisibility(View.INVISIBLE);
                    }
                    prevOption.setVisibility(View.VISIBLE);
                }
            }
        });
        prevOption.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    listIndex = listIndex - 4;
                    endIndex = endIndex - 4;
                    setFour(choiceList.get(listIndex), choiceList.get(listIndex + 1), choiceList.get(listIndex + 2), choiceList.get(endIndex));
                    if (listIndex == 0)
                    {
                        prevOption.setVisibility(View.INVISIBLE);
                    }
                    nextOption.setVisibility(View.VISIBLE);
                }
            }
        });


        multi1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    multiAns.setText(multi1.getText());
                }
            }
        });
        multi2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    multiAns.setText(multi2.getText());
                }
            }
        });
        multi3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    multiAns.setText(multi3.getText());
                }
            }
        });
        multi4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    multiAns.setText(multi4.getText());
                }
            }
        });
        multiTri.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    multiAns.setText(multiTri.getText());
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!loading)
                {
                    //validate answer and record score
                    String studAns = "";
                    if (choiceList.size() == 0)
                    {
                        studAns = inputAns.getText().toString();
                    } else
                    {
                        studAns = multiAns.getText().toString();
                    }
                    if (studAns.equals(Integer.toString(questAns)))
                    {
                        score = score + 10;
                    } else if (!studAns.equals(""))
                    {
                        score = score - 5;
                    }
                    totalMarks = totalMarks + 10;
                    //update score
                    totalScore.setText(Integer.toString(score) + "/" + Integer.toString(totalMarks));
                    //change to next question
                    loading = true;
                    toast = toast.makeText(getActivity().getApplicationContext(),"fetching question"
                            , Toast.LENGTH_LONG);
                    toast.show();
                    new FetchQuestion().execute();
                }
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
            String result = "";//TODO use emulator ip
            String completeURL = Uri.parse("https://192.168.166.120:8000/random/question/")
                    .buildUpon()
                    .appendQueryParameter("method", "thedata.getit")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .build().toString();
            try
            {
                URL url = new URL(completeURL);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                DownloadUtils.addCertificate(getContext(), conn);//TODO reference from prac 6
                if (conn.getResponseCode() != HttpsURLConnection.HTTP_OK)
                {
                    throw new GeneralSecurityException("error, cannot connect to server");
                }
                try
                {
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int bytesRead = is.read(buffer);
                    int nBytes = 0;
                    while (bytesRead > 0)
                    {
                        //progress bar
                        baos.write(buffer, 0, bytesRead);
                        bytesRead = is.read(buffer);
                        nBytes += bytesRead;
                    }
                    baos.close();
                    result = new String(baos.toByteArray());
                    System.out.println(result);
                }
                catch (IOException e)
                {
                    throw new IOException(e);
                }
                finally
                {
                    conn.disconnect();
                }

            }
            catch (IOException e)
            {
                Log.e(TAG, e.getMessage(),e);
                //testError("error, cannot connect to server");//TODO handle errors connecting
            }
            catch (GeneralSecurityException e)
            {
                Log.e(TAG, e.getMessage(),e);
                //testError("error, cannot connect to server");
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result)
        {
            try
            {
                //JSON
                JSONObject jbase = new JSONObject(result);
                question.setText(jbase.getString("question"));
                questAns = jbase.getInt("result");
                JSONArray opArr = jbase.getJSONArray("options");
                timeToSolve = jbase.getInt("timetosolve");
                for(int i = 0; i < opArr.length(); i++)
                {
                    choiceList.add(Integer.toString(opArr.getInt(i)));
                }
                loading = false;
                setTestLayout();
            }
            catch (JSONException e)
            {
                Log.e(TAG, e.getMessage(), e);
                testError("error, cannot fetch question");
            }
        }

    }






}