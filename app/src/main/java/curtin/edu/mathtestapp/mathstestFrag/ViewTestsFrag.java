package curtin.edu.mathtestapp.mathstestFrag;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.DescendTestSorter;
import curtin.edu.mathtestapp.model.Student;
import curtin.edu.mathtestapp.model.StudentList;
import curtin.edu.mathtestapp.model.TestList;
import curtin.edu.mathtestapp.model.TestResult;
import curtin.edu.mathtestapp.model.TestSorter;
import curtin.edu.mathtestapp.registrationfrag.Menu;
import curtin.edu.mathtestapp.registrationfrag.RegisterStudentFragment;
import curtin.edu.mathtestapp.registrationfrag.ViewStudents;

public class ViewTestsFrag extends Fragment
{
    private RecyclerView recycleTest;
    private TestList list;
    private Button back;
    private FragmentManager fm;
    private ArrayList<TestResult> curList;
    private Button ascend;
    private Button descend;

    public void setRecycler()
    {
        recycleTest.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        ViewTestAdapter testAdapter = new ViewTestAdapter(curList);
        recycleTest.setAdapter(testAdapter);
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        fm = getParentFragmentManager();
        list = new TestList();
        list.load(getActivity());
        if(bundle != null)
        {

        }
        else
        {
            curList = list.getList();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.view_tests_recycler, ui, false);
        back = (Button) view.findViewById(R.id.backToMenu);
        descend = (Button) view.findViewById(R.id.sortDButton);
        ascend = (Button) view.findViewById(R.id.sortAButton);
        //recyclerview set up
        recycleTest = (RecyclerView) view.findViewById(R.id.viewTestRecycle);
        setRecycler();

        descend.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                curList.sort(new TestSorter());
                setRecycler();
            }
        });

        ascend.setOnClickListener(new View.OnClickListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v)
            {
                curList.sort(new DescendTestSorter());
                setRecycler();
            }
        });

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
    private class ViewTestAdapter extends RecyclerView.Adapter<TestDataHolder>
    {
        private ArrayList<TestResult> pList;
        public ViewTestAdapter(ArrayList<TestResult> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public TestDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.view_test_row, parent, false);
            return new TestDataHolder(view);
        }

        @Override
        public void onBindViewHolder(TestDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    /*****************************
     * MyDataHolder
     */
    private class TestDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private TextView date;
        private TextView time;
        private TextView score;

        public TestDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            date = (TextView) itemView.findViewById(R.id.dateText);
            time = (TextView) itemView.findViewById(R.id.timeText);
            score = (TextView) itemView.findViewById(R.id.scoreText);
        }
        public void bind(TestResult data)
        {
            if (data != null)
            {
                name.setText(data.getName());
                date.setText(data.getDate());
                time.setText(Integer.toString(data.getTime()));
                score.setText(data.getScore());
            }

        }
    }

}
