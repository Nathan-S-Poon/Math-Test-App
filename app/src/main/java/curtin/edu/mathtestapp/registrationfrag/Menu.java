package curtin.edu.mathtestapp.registrationfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.mathstestFrag.StudentSelectionFrag;
import curtin.edu.mathtestapp.mathstestFrag.TestFrag;
import curtin.edu.mathtestapp.mathstestFrag.ViewTestsFrag;
import curtin.edu.mathtestapp.model.StudentList;

public class Menu extends Fragment
{
    private Button editStudent;
    private Button testButton;
    private Button viewStudents;
    private Button viewTests;
    private RegisterStudentFragment addFrag;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.menu, ui, false);
        editStudent = (Button) view.findViewById(R.id.AddStudent);
        testButton = (Button) view.findViewById(R.id.startMathButton);
        viewStudents = (Button) view.findViewById(R.id.viewStudents);
        viewTests = (Button) view.findViewById(R.id.viewTests);

        editStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getParentFragmentManager();
                addFrag = (RegisterStudentFragment) fm.findFragmentById(R.id.registration);
                if(addFrag == null)
                {
                    addFrag = new RegisterStudentFragment();
                    fm.beginTransaction().replace(R.id.frame, addFrag).commit();
                }
            }
        });

        viewStudents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getParentFragmentManager();
                ViewStudents frag = (ViewStudents) fm.findFragmentById(R.id.studentRecyclerLayout);
                if(frag == null)
                {
                    frag = new ViewStudents();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });

        testButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getParentFragmentManager();
                StudentSelectionFrag frag = (StudentSelectionFrag) fm.findFragmentById(R.id.studentRecyclerLayout);
                if(frag == null)
                {
                    frag = new StudentSelectionFrag();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });

        viewTests.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getParentFragmentManager();
                ViewTestsFrag frag = (ViewTestsFrag) fm.findFragmentById(R.id.viewTestRecycle);
                if(frag == null)
                {
                    frag = new ViewTestsFrag();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });

        return view;
    }

}
