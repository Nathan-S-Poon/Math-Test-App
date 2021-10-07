package curtin.edu.mathtestapp;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import curtin.edu.mathtestapp.registrationfrag.Menu;
import curtin.edu.mathtestapp.registrationfrag.RegisterStudentFragment;

public class MainActivity extends AppCompatActivity
{
    private Menu menuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getSupportFragmentManager();
        menuFragment = (Menu) fm.findFragmentById(R.id.menuFrag);
        if (menuFragment == null)
        {
            menuFragment = new Menu();
            fm.beginTransaction().replace(R.id.frame, menuFragment).commit();
        }




    }
}