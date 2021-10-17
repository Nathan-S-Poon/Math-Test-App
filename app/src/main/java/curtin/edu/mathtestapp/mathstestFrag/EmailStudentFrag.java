package curtin.edu.mathtestapp.mathstestFrag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.Student;
import curtin.edu.mathtestapp.model.StudentList;
import curtin.edu.mathtestapp.model.TestResult;
import curtin.edu.mathtestapp.registrationfrag.Menu;

public class EmailStudentFrag extends Fragment
{
    private RecyclerView recycleStudents;
    private RecyclerView recyclerSelect;
    private StudentList list;
    private ArrayList<Student> selectList;
    private ArrayList<TestResult> resultsList;
    private Button back;
    private FragmentManager fm;
    private Toast toast;

    public void setRecycler()
    {
        recycleStudents.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        ViewStuAdapter stuAdapter = new ViewStuAdapter(list.getList());
        recycleStudents.setAdapter(stuAdapter);
    }

    public void setSelectRecycler()
    {
        recyclerSelect.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        ViewSelectAdapter stuAdapter = new ViewSelectAdapter(selectList);
        recyclerSelect.setAdapter(stuAdapter);
    }

    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("selectlist", selectList);
        bundle.putParcelableArrayList("resultslist", resultsList);

    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        fm = getParentFragmentManager();
        list = new StudentList();
        list.load(getActivity());
        if(bundle != null)
        {
            selectList = bundle.getParcelableArrayList("selectlist");
            resultsList = bundle.getParcelableArrayList("resultslist");
        }
        else
        {
            selectList = new ArrayList<Student>();
        }
        getParentFragmentManager().setFragmentResultListener("resultsToEmail", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                resultsList = result.getParcelableArrayList("resultslist");
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.student_email_recycler, ui, false);
        back = (Button) view.findViewById(R.id.backToMenu);
        //recyclerview set up
        recycleStudents = (RecyclerView) view.findViewById(R.id.viewEmailStudents);
        recyclerSelect  = (RecyclerView) view.findViewById(R.id.selectEmailStudents);
        setRecycler();
        setSelectRecycler();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle result = new Bundle();
                result.putParcelableArrayList("resultslist",selectList);
                getParentFragmentManager().setFragmentResult("emailToResults", result);
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
            View view = li.inflate(R.layout.view_list_row, parent, false);
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

        public StudentDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            image = (ImageView) itemView.findViewById(R.id.studentImage);
            row = (LinearLayout) itemView.findViewById(R.id.studentRow);
            delete = (Button) itemView.findViewById(R.id.deleteButton);
            delete.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);

        }
        public void bind(Student data)
        {
            if (data != null)
            {
                System.out.println(data.getFirstName());
                name.setText(data.getFirstName() + " " + data.getLastName());
                //TODO how to store images in database
                row.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        selectList.add(data);
                        setSelectRecycler();
                    }
                });


            }

        }
    }
    /*******************************
     * Select adapter
     */
    private class ViewSelectAdapter extends RecyclerView.Adapter<SelectDataHolder>
    {
        private ArrayList<Student> pList;
        public ViewSelectAdapter(ArrayList<Student> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public SelectDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.view_list_row, parent, false);
            return new SelectDataHolder(view);
        }

        @Override
        public void onBindViewHolder(SelectDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    /*****************************
     * MyDataHolder
     */
    private class SelectDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private ImageView image;
        private LinearLayout row;
        private Button delete;

        public SelectDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            image = (ImageView) itemView.findViewById(R.id.studentImage);
            row = (LinearLayout) itemView.findViewById(R.id.studentRow);
            delete = (Button) itemView.findViewById(R.id.deleteButton);
            image.setVisibility(View.INVISIBLE);

        }
        public void bind(Student data)
        {
            if (data != null)
            {

                name.setText(data.getFirstName() + " " + data.getLastName());
                //TODO how to store images in database
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });


            }

        }
    }
}
