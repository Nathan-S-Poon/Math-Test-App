package curtin.edu.mathtestapp.registrationfrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.StudentList;

public class Menu extends Fragment
{
    private Button editStudent;
    private Button testButton;
    private Button viewStudents;


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

        editStudent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentManager fm = getParentFragmentManager();
                RegisterStudentFragment frag = (RegisterStudentFragment) fm.findFragmentById(R.id.registration);
                if(frag == null)
                {
                    frag = new RegisterStudentFragment();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
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

            }
        });

        return view;
    }

}
