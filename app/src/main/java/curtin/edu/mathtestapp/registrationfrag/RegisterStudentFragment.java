package curtin.edu.mathtestapp.registrationfrag;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.File;
import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.InputException;
import curtin.edu.mathtestapp.model.Student;
import curtin.edu.mathtestapp.model.StudentList;

public class RegisterStudentFragment extends Fragment
{
    private static final int REQUEST_CONTACT = 0;
    private static final int REQUEST_CONTACT_PERMISSION = 1;
    private static final int REQUEST_LIVE_PHOTO = 2;


    private Button emailButton;
    private Button phoneButton;
    private TextView firstText;
    private EditText firstInput;
    private TextView lastText;
    private EditText lastInput;
    private TextView emailText;
    private EditText emailInput;
    private TextView phoneText;
    private EditText phoneInput;
    private Button registerButton;
    private Toast toast;
    private Button contactButton;
    private Button livePhoto;
    private ImageView photoDisplay;
    private StudentList list;
    private File photoFile;
    private ArrayList<Integer> phoneNums;
    private ArrayList<String> emailList;
    private int id;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        list = new StudentList();
        list.load(getActivity());
        id = list.findHighestId();
        phoneNums = new ArrayList<Integer>();
        emailList = new ArrayList<String>();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {   //if getting live photo
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_LIVE_PHOTO)
        {
            Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
            photoDisplay.setImageBitmap(photo);
        }
        //get contact details
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONTACT)
        {
          //get ID
            Uri contactUri = resultIntent.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts._ID,
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null,
                    null, null);
            try
            {
                if (c.getCount() > 0)
                {
                    c.moveToFirst();
                    int id = c.getInt(0);

                    //get names
                    Uri dataUri = ContactsContract.Data.CONTENT_URI;
                    String[] queryName = new String[]{
                            ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME,
                            ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME
                    };
                    String whereClause = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + "=? and "
                            + ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME + "=?";
                    String [] whereValues = new String[]{
                            String.valueOf(id),
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE};
                    Cursor nameC = getActivity().getContentResolver().query(dataUri, queryName,
                            whereClause,whereValues,null);
                    try
                    {
                        if (nameC.getCount() > 0)
                        {
                            nameC.moveToFirst();
                            String first = nameC.getString(0);
                            String last = nameC.getString(1);
                            firstInput.setText(first);
                            lastInput.setText(last);
                        }
                    }
                    finally
                    {
                        nameC.close();
                    }
                }
            }
            finally
            {
                c.close();
            }

        }

    }

    public void onRequestPermissionsResult(int requestCode, String[]
            permissions, int[] grantResults)
    {
        if(requestCode == REQUEST_CONTACT_PERMISSION)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getActivity(), "Reading permission granted",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.enter_student_details, ui, false);
        emailButton = (Button) view.findViewById(R.id.emailButton);
        phoneButton = (Button) view.findViewById(R.id.phoneNumButton);
        firstText = (TextView) view.findViewById(R.id.firstNameText);
        firstInput = (EditText) view.findViewById(R.id.firstNameInput);
        lastText = (TextView) view.findViewById(R.id.lastNameText);
        lastInput = (EditText) view.findViewById(R.id.lastNameInput);
        phoneText = (TextView) view.findViewById(R.id.phoneNumtext);
        phoneInput = (EditText) view.findViewById(R.id.phoneNumInput);
        emailText = (TextView) view.findViewById(R.id.emailText);
        emailInput = (EditText) view.findViewById(R.id.emailInput);
        registerButton = (Button) view.findViewById(R.id.registerButton);
        contactButton = (Button) view.findViewById(R.id.selectContacts);
        livePhoto = (Button) view.findViewById(R.id.photoLiveButton);
        photoDisplay = (ImageView) view.findViewById(R.id.photoDisplay);

        livePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                photoFile = new File(getActivity().getFilesDir(), "photo.jpg");
                Intent fullPhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri cameraURi = FileProvider.getUriForFile(
                        getActivity(), "edu.curtin.edu.mathsapp.registerstudentfragment",
                        photoFile);
                //permission
                PackageManager pm = getActivity().getPackageManager();
                for(ResolveInfo a : pm.queryIntentActivities(fullPhotoIntent, PackageManager.MATCH_DEFAULT_ONLY))
                {
                    getActivity().grantUriPermission(
                            a.activityInfo.packageName,
                            cameraURi,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                fullPhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraURi);
                startActivityForResult(fullPhotoIntent, REQUEST_LIVE_PHOTO);
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            REQUEST_CONTACT_PERMISSION);
                }
                startActivityForResult(contactIntent, REQUEST_CONTACT);
            }
        });

        //check if valid
        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    //check first and last name
                    String newFirst = firstInput.getText().toString();
                    String newLast = lastInput.getText().toString();
                    if (newFirst.equals(""))
                    {
                        throw new InputException("First name can't be empty");
                    }
                    if (newLast.equals(""))
                    {
                        throw new InputException("Last name can't be empty");
                    }
                    int newPhoto = 0;

                    //Make student and add to database
                    id++;
                    Student newStudent = new Student(id, newFirst, newLast, newPhoto);
                    newStudent.setNumbers(phoneNums);
                    newStudent.setEmails(emailList);
                    list.addStudent(newStudent);
                }
                catch (InputException e)
                {
                    toast = toast.makeText(getActivity().getApplicationContext(), e.getMessage()
                    , Toast.LENGTH_SHORT);
                    toast.show();;
                }

            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String newPhone = phoneInput.getText().toString();
                    if(newPhone.equals(""))
                    {
                        throw new InputException("Must enter a phone number");
                    }
                    if(phoneNums.size() == 10)
                    {
                        throw new InputException("Reached maximum of 10 phone numbers");
                    }
                    phoneNums.add(Integer.parseInt(newPhone));
                }
                catch (InputException e)
                {
                    toast = toast.makeText(getActivity().getApplicationContext(), e.getMessage()
                            , Toast.LENGTH_SHORT);
                    toast.show();;
                }


            }
        });

        //add emails
        emailButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    String newEmail = emailInput.getText().toString();
                    if(newEmail.equals(""))
                    {
                        throw new InputException("Must enter an email");
                    }
                    if(!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches())
                    {
                        throw new InputException("Not a valid email address");
                    }
                    if(emailList.size() == 10)
                    {
                        throw new InputException("Reached maximum of 10 emails");
                    }
                    emailList.add(newEmail);

                }
                catch (InputException e)
                {
                    toast = toast.makeText(getActivity().getApplicationContext(), e.getMessage()
                            , Toast.LENGTH_SHORT);
                    toast.show();;
                }


            }
        });



        return  view;
    }




}
