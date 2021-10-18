package curtin.edu.mathtestapp.registrationfrag;

import android.icu.number.IntegerWidth;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.model.Student;
import curtin.edu.mathtestapp.model.StudentList;

public class ViewContacts extends Fragment
{
    private RecyclerView recyclePhones;
    private RecyclerView recycleEmails;
    private ArrayList<Integer> phoneList;
    private ArrayList<String> emailList;
    private StudentList list;
    private Button back;
    private TextView phoneText;
    private TextView emailText;
    private int id;
    private boolean isEdit;
    private FragmentManager fm;
    //student details
    private String firstname;
    private String lastName;
    private ArrayList<Integer> numbers;
    private ArrayList<String> emails;
    private String emailStr;
    private String phoneStr;
    private File photo;


    public void setRecyclers()
    {
        //recyclerview set up
        recyclePhones.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        PhoneAdapter phoneAdapt = new PhoneAdapter(phoneList);
        recyclePhones.setAdapter(phoneAdapt);
        recycleEmails.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        EmailAdapter emailAdapt = new EmailAdapter(emailList);
        recycleEmails.setAdapter(emailAdapt);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putStringArrayList("emails", emailList);
        bundle.putIntegerArrayList("phone", phoneList);
        bundle.putBoolean("isEdit", isEdit);
        bundle.putSerializable("photo", photo);
        bundle.putString("emailStr", emailStr);
        bundle.putString("phoneStr", phoneStr);
        bundle.putString("first", firstname);
        bundle.putString("last", lastName);
        bundle.putInt("id", id);
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
            phoneList = bundle.getIntegerArrayList("phone");
            emailList = bundle.getStringArrayList("emails");
            photo = (File) bundle.getSerializable("photo");
            emailStr = bundle.getString("emailStr");
            phoneStr = bundle.getString("phoneStr");
            firstname = bundle.getString("first");
            lastName = bundle.getString("last");
            id = bundle.getInt("id");
            isEdit = bundle.getBoolean("isEdit");
        }
        getParentFragmentManager().setFragmentResultListener("studentToContacts", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                id = result.getInt("studentID");
                isEdit = result.getBoolean("isEdit");
                firstname = result.getString("first");
                lastName = result.getString("last");
                emailStr = result.getString("emailStr");
                phoneStr = result.getString("phoneStr");
                photo = (File) result.getSerializable("photo");
                phoneList = result.getIntegerArrayList("phones");
                emailList = result.getStringArrayList("emails");
                setRecyclers();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.view_contacts, ui, false);
        back = (Button) view.findViewById(R.id.backToMenu);
        recyclePhones = (RecyclerView) view.findViewById(R.id.viewPhoneRecycle);
        recycleEmails = (RecyclerView) view.findViewById(R.id.viewEmailRecycle);
        phoneText = (TextView) view.findViewById(R.id.phoneText);
        emailText = (TextView) view.findViewById(R.id.emailText);
        setRecyclers();

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle result = new Bundle();
                result.putBoolean("IS_EDIT", isEdit);
                result.putInt("studentID", id);
                result.putString("first", firstname);
                result.putString("last", lastName);
                result.putString("emailStr", emailStr);
                result.putString("phoneStr", phoneStr);
                result.putSerializable("photo", photo);
                result.putStringArrayList("emails", emailList);
                result.putIntegerArrayList("phones", phoneList);
                getParentFragmentManager().setFragmentResult("contactsToStudent", result);
                RegisterStudentFragment frag = (RegisterStudentFragment) fm.findFragmentById(R.id.registration);
                if(frag == null)
                {
                    frag = new RegisterStudentFragment();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });


        return view;
    }

    /*******************************
     * Adapter nested in this class
     */
    private class PhoneAdapter extends RecyclerView.Adapter<PhoneDataHolder>
    {
        private ArrayList<Integer> pList;
        public PhoneAdapter(ArrayList<Integer> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public PhoneDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.contacts_row, parent, false);
            return new PhoneDataHolder(view);
        }

        @Override
        public void onBindViewHolder(PhoneDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    private class EmailAdapter extends RecyclerView.Adapter<EmailDataHolder>
    {
        private ArrayList<String> pList;
        public EmailAdapter(ArrayList<String> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public EmailDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.contacts_row, parent, false);
            return new EmailDataHolder(view);
        }

        @Override
        public void onBindViewHolder(EmailDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    /*****************************
     * email data holder
     */
    private class EmailDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private LinearLayout row;
        private Button delete;

        public EmailDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            row = (LinearLayout) itemView.findViewById(R.id.studentRow);
            delete = (Button) itemView.findViewById(R.id.deleteButton);
        }
        public void bind(String data)
        {
            if (data != null)
            {
                name.setText(data);
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        emailList.remove(data);
                        setRecyclers();
                    }
                });

            }

        }
    }

    /*****************************
     * phone number data holder
     */
    private class PhoneDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private LinearLayout row;
        private Button delete;

        public PhoneDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            row = (LinearLayout) itemView.findViewById(R.id.studentRow);
            delete = (Button) itemView.findViewById(R.id.deleteButton);
        }
        public void bind(Integer data)
        {
            if (data != null)
            {
                name.setText(Integer.toString(data));
                delete.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        phoneList.remove(data);
                        setRecyclers();
                    }
                });

            }

        }
    }
}


