package curtin.edu.mathtestapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import curtin.edu.mathtestapp.registrationfrag.RegisterStudentFragment;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        RegisterStudentFragment frag = (RegisterStudentFragment) fm.findFragmentById(R.id.frame);
        if(frag == null)
        {
            frag = new RegisterStudentFragment();
            fm.beginTransaction().replace(R.id.frame, frag).commit();
        }


    }
}