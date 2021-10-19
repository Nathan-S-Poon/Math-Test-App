package curtin.edu.mathtestapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import curtin.edu.mathtestapp.registrationfrag.Menu;
import curtin.edu.mathtestapp.registrationfrag.RegisterStudentFragment;

public class MainActivity extends AppCompatActivity
{
    private static final String LOGIN_TAG = "login_tag";

    private Menu menuFragment;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState == null)
        {
            fm = getSupportFragmentManager();
            menuFragment = (Menu) fm.findFragmentById(R.id.menuFrag);
            if (menuFragment == null)
            {
                menuFragment = new Menu();
                fm.beginTransaction().replace(R.id.frame, menuFragment, LOGIN_TAG).commit();
            }
        }
        else
        {
            menuFragment = (Menu) getSupportFragmentManager().findFragmentByTag(LOGIN_TAG);
        }




    }
}