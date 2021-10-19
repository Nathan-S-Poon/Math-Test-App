package curtin.edu.mathtestapp.registrationfrag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.Student;
import curtin.edu.mathtestapp.model.StudentList;
import curtin.edu.mathtestapp.model.TestList;

public class ViewStudents extends Fragment
{
    private RecyclerView recycleStudents;
    private StudentList list;
    private Button back;
    private FragmentManager fm;
    private TestList testList;

    public void setRecycler()
    {
        recycleStudents.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        ViewStuAdapter stuAdapter = new ViewStuAdapter(list.getList());
        recycleStudents.setAdapter(stuAdapter);
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        fm = getParentFragmentManager();
        list = new StudentList();
        list.load(getActivity());
        testList = new TestList();
        testList.load(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.view_list_recycler, ui, false);
        back = (Button) view.findViewById(R.id.backToMenu);
        //recyclerview set up
        recycleStudents = (RecyclerView) view.findViewById(R.id.viewStudentsRecycle);
        setRecycler();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Menu frag = (Menu) fm.findFragmentById(R.id.menuFrag);
                if(frag == null)
                {
                    frag = new Menu();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });


        return view;
    }

    /*******************************
     * Adapter nested in this class
     */
    private class ViewStuAdapter extends RecyclerView.Adapter<StudentDataHolder>
    {
        private ArrayList<Student> pList;
        public ViewStuAdapter(ArrayList<Student> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public StudentDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.view_list_row_student, parent, false);
            return new StudentDataHolder(view);
        }

        @Override
        public void onBindViewHolder(StudentDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    /*****************************
     * MyDataHolder
     */
    private class StudentDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private ImageView image;
        private LinearLayout row;
        private Button delete;
        private Button tests;

        public StudentDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            image = (ImageView) itemView.findViewById(R.id.studentImage);
            row = (LinearLayout) itemView.findViewById(R.id.studentRow);
            delete = (Button) itemView.findViewById(R.id.deleteButton);
            tests = (Button) itemView.findViewById(R.id.viewTests);

        }
        public void bind(Student data)
        {
            if (data != null)
            {

                name.setText(data.getFirstName() + " " + data.getLastName());
                //set image
                Bitmap photo = BitmapFactory.decodeFile(data.getPhoto().toString());
                image.setImageBitmap(photo);
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        list.deleteStudent(data);
                        //delete test info
                        testList.deleteStudentResults(data.getID());
                        setRecycler();
                    }
                });
                tests.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Bundle result = new Bundle();
                        result.putParcelableArrayList("resultslist", testList.getResults(data.getID()));
                        getParentFragmentManager().setFragmentResult("viewStudentResult", result);
                        StudentResultsFrag frag = (StudentResultsFrag) fm.findFragmentById(R.id.StudentTestRecyclerLayout);
                        if(frag == null)
                        {
                            frag = new StudentResultsFrag();
                            fm.beginTransaction().replace(R.id.frame, frag).commit();
                        }
                    }
                });
                //TODO how to store images in database
                row.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Bundle result = new Bundle();
                        result.putBoolean("IS_EDIT", true);
                        result.putInt("studentID", data.getID());
                        getParentFragmentManager().setFragmentResult("viewToStudent", result);
                        RegisterStudentFragment frag = (RegisterStudentFragment) fm.findFragmentById(R.id.registration);
                        if(frag == null)
                        {
                            frag = new RegisterStudentFragment();
                            fm.beginTransaction().replace(R.id.frame, frag).commit();
                        }
                    }
                });


            }

        }
    }
}
