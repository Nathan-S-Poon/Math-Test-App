package curtin.edu.mathtestapp.registrationfrag;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private static final int REQUEST_STORAGE_PHOTO = 3;

    private FragmentManager fm;
    private Button onlinePhoto;
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
    private Button viewContacts;
    private Button livePhoto;
    private ImageView photoDisplay;
    private Button photoStorage;
    private StudentList list;
    private File photoFile;
    private Boolean isEdit;
    private ArrayList<Integer> phoneNums;
    private ArrayList<String> emailList;
    private int id;
    private Button back;

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("IS_EDIT", isEdit);
        bundle.putInt("studentID", id);
        bundle.putString("first", firstInput.getText().toString());
        bundle.putString("last", lastInput.getText().toString());
        bundle.putString("emailStr", emailInput.getText().toString());
        bundle.putString("phoneStr", phoneInput.getText().toString());
        bundle.putSerializable("photo", photoFile);
        bundle.putStringArrayList("emails", emailList);
        bundle.putIntegerArrayList("phones", phoneNums);

    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        if(bundle != null)
        {
            fm = getParentFragmentManager();
            isEdit = bundle.getBoolean("IS_EDIT");
            id = bundle.getInt("studentID");
            if(isEdit)
            {
                registerButton.setText("Edit");
            }
            photoFile = (File)bundle.getSerializable("photo");
            emailList = bundle.getStringArrayList("emails");
            phoneNums = bundle.getIntegerArrayList("phones");
        }
        else
        {
            fm = getParentFragmentManager();
            isEdit = false;
            list = new StudentList();
            list.load(getActivity());
            id = list.findHighestId();
            phoneNums = new ArrayList<Integer>();
            photoFile = null;
            emailList = new ArrayList<String>();
        }
        getParentFragmentManager().setFragmentResultListener("viewToStudent", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                isEdit = result.getBoolean("IS_EDIT");
                id = result.getInt("studentID");
                Student curr = list.findStudent(id);
                firstInput.setText(curr.getFirstName());
                lastInput.setText(curr.getLastName());
                photoFile = new File(curr.getPhoto());
                phoneNums = curr.getNumbers();
                emailList = curr.getEmails();
                registerButton.setText("Edit");
                if (photoFile != null)
                {
                    Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
                    photoDisplay.setImageBitmap(photo);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener("contactsToStudent", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                isEdit = result.getBoolean("IS_EDIT");
                id = result.getInt("studentID");
                firstInput.setText(result.getString("first"));
                lastInput.setText(result.getString("last"));
                emailInput.setText(result.getString("emailStr"));
                phoneInput.setText(result.getString("phoneStr"));
                photoFile = (File) result.getSerializable("photo");
                phoneNums = result.getIntegerArrayList("phones");
                emailList = result.getStringArrayList("emails");
                if(isEdit)
                {
                    registerButton.setText("Edit");
                }
                if (photoFile != null)
                {
                    Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
                    photoDisplay.setImageBitmap(photo);
                }
            }
        });
        getParentFragmentManager().setFragmentResultListener("onlineToReg", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                isEdit = result.getBoolean("isEdit");
                id = result.getInt("ID");
                firstInput.setText(result.getString("first"));
                lastInput.setText(result.getString("last"));
                phoneNums = result.getIntegerArrayList("phone");
                emailList = result.getStringArrayList("emails");
                emailInput.setText(result.getString("emailStr"));
                phoneInput.setText(result.getString("phoneStr"));
                photoFile = (File) result.getSerializable("photo");
                if(isEdit)
                {
                    registerButton.setText("Edit");
                }
                if (photoFile != null)
                {
                    Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
                    photoDisplay.setImageBitmap(photo);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultIntent)
    {   //if getting live photo
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_LIVE_PHOTO)
        {
            Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
            photoDisplay.setImageBitmap(photo);
            //add photos to internal storage
            System.out.println(photoFile.toString());
        }
        //if getting photo from storage
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_STORAGE_PHOTO)
        {//TODO put photo in right file and reference
            //https://www.tutorialspoint.com/how-to-write-an-image-file-in-internal-storage-in-android
            Uri image = resultIntent.getData();
            photoDisplay.setImageURI(image);
            //save photo in internal storage
            ContextWrapper cw = new ContextWrapper(getContext());
            File dir = cw.getDir("files", Context.MODE_PRIVATE);
            File file = new File(dir, "photoStore.jpg");
            System.out.println(file.toString());
            if(!file.exists());
            {
                FileOutputStream fos = null;
                try
                {
                    fos = new FileOutputStream(file);
                    Bitmap bit = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), image);
                    bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    photoFile = file;
                }
                catch (IOException e)
                {

                }
            }
        }

        //get contact details
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CONTACT)
        {
          //get ID
            Uri contactUri = resultIntent.getData();
            String[] queryFields = new String[]{
                    ContactsContract.Contacts._ID,
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
                    String whereClause = ContactsContract.CommonDataKinds.StructuredName.CONTACT_ID + "=?";
                    String [] whereValues = new String[]{
                            String.valueOf(id)};
                    Cursor nameC = getActivity().getContentResolver().query(dataUri, queryName,
                            whereClause,whereValues,null);
                    try
                    {
                        if (nameC.getCount() > 0)
                        {
                            nameC.moveToFirst();
                            String first = nameC.getString(0);
                            String last = nameC.getString(1);
                            System.out.println(first + last);
                            firstInput.setText(first);
                            lastInput.setText(last);
                        }
                    }
                    finally
                    {
                        nameC.close();
                    }
                    //get email and phone
                    Cursor cEmail = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            new String[] {ContactsContract.CommonDataKinds.Email.ADDRESS},
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[] {String.valueOf(id)}, null, null);
                    try
                    {
                        emailList = new ArrayList<String>();
                        if(cEmail.getCount() > 0)
                        {
                            cEmail.moveToFirst();
                            String email = cEmail.getString(0);
                            //add emails to list
                            emailList.add(email);
                        }
                    }
                    finally
                    {
                        cEmail.close();
                    }
                    //phones
                    Cursor cPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            new String[] {ContactsContract.CommonDataKinds.Phone.NUMBER},
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[] {String.valueOf(id)}, null, null);
                    try
                    {
                        phoneNums = new ArrayList<Integer>();
                        if(cPhone.getCount() > 0)
                        {
                            cPhone.moveToFirst();
                            String phoneNumber = cPhone.getString(0);
                            phoneNums.add(Integer.parseInt(phoneNumber));
                        }
                    }
                    finally
                    {
                        cPhone.close();
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
        photoStorage = (Button) view.findViewById(R.id.photoStorage);
        back = (Button) view.findViewById(R.id.studToMenu);
        viewContacts = (Button) view.findViewById(R.id.contactsButton);
        onlinePhoto = (Button) view.findViewById(R.id.onlineButton);
        if(bundle != null)
        {
            firstInput.setText(bundle.getString("first"));
            lastInput.setText(bundle.getString("last"));
            emailInput.setText(bundle.getString("emailStr"));
            phoneInput.setText(bundle.getString("phoneStr"));
            if (photoFile != null)
            {
                Bitmap photo = BitmapFactory.decodeFile(photoFile.toString());
                photoDisplay.setImageBitmap(photo);
            }
        }

        viewContacts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle result = new Bundle();
                result.putInt("studentID", id);
                result.putBoolean("isEdit", isEdit);
                result.putIntegerArrayList("phones", phoneNums);
                result.putStringArrayList("emails", emailList);
                result.putString("first", firstInput.getText().toString());
                result.putString("last", lastInput.getText().toString());
                result.putString("emailStr", emailInput.getText().toString());
                result.putString("phoneStr", phoneInput.getText().toString());
                result.putSerializable("photo", photoFile);
                getParentFragmentManager().setFragmentResult("studentToContacts", result);
                ViewContacts frag = (ViewContacts) fm.findFragmentById(R.id.contactsRecycler);
                if(frag == null)
                {
                    frag = new ViewContacts();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isEdit)
                {
                    FragmentManager fm = getParentFragmentManager();
                    ViewStudents frag = (ViewStudents) fm.findFragmentById(R.id.studentRecyclerLayout);
                    if(frag == null)
                    {
                        frag = new ViewStudents();
                        fm.beginTransaction().replace(R.id.frame, frag).commit();
                    }
                }
                else
                {
                    Menu frag = (Menu) fm.findFragmentById(R.id.menuFrag);
                    if (frag == null)
                    {
                        frag = new Menu();
                        fm.beginTransaction().replace(R.id.frame, frag).commit();
                    }
                }

            }
        });

        onlinePhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle result = new Bundle();
                result.putInt("ID", id);
                result.putString("first", firstInput.getText().toString());
                result.putString("last", lastInput.getText().toString());
                result.putIntegerArrayList("phone", phoneNums);
                result.putStringArrayList("emails", emailList);
                result.putBoolean("isEdit", isEdit);
                result.putString("emailStr", emailInput.getText().toString());
                result.putString("phoneStr", phoneInput.getText().toString());
                result.putSerializable("photo", photoFile);
                getParentFragmentManager().setFragmentResult("regToOnline", result);
                OnlinePhotoFrag frag = (OnlinePhotoFrag) fm.findFragmentById(R.id.onlinePhotosLayout);
                if(frag == null)
                {
                    frag = new OnlinePhotoFrag();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }

            }
        });

        photoStorage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent storageIntent = new Intent();
                //MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                storageIntent.setType("image/*");
                storageIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(storageIntent, REQUEST_STORAGE_PHOTO);
            }
        });

        livePhoto.setOnClickListener(new View.OnClickListener()
        {//TODO chekc app exists
            @Override
            public void onClick(View v)
            {
                photoFile = new File(getActivity().getFilesDir(), "photo" + Integer.toString(id) + ".jpg");
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
                    if(phoneNums.size() == 0)
                    {
                        throw new InputException("Need at least 1 phone number");
                    }
                    if(emailList.size() == 0)
                    {
                        throw new InputException("Need at least 1 email");
                    }
                    if(photoFile == null)
                    {
                        throw new InputException("Need a photo");
                    }
                    String newPhoto = photoFile.toString();

                    //Make student and add to database
                    if(!isEdit)
                    {
                        id++;
                        Student newStudent = new Student(id, newFirst, newLast, newPhoto);
                        newStudent.setNumbers(phoneNums);
                        newStudent.setEmails(emailList);
                        list.addStudent(newStudent);
                        phoneNums = new ArrayList<Integer>();
                        emailList = new ArrayList<String>();
                        toast = toast.makeText(getActivity().getApplicationContext(),"student added"
                                , Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        Student newStudent = new Student(id, newFirst, newLast, newPhoto);
                        newStudent.setNumbers(phoneNums);
                        newStudent.setEmails(emailList);
                        list.updateStudent(newStudent);
                        toast = toast.makeText(getActivity().getApplicationContext(), "student updated"
                                , Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
                catch (InputException e)
                {
                    toast = toast.makeText(getActivity().getApplicationContext(), e.getMessage()
                    , Toast.LENGTH_SHORT);
                    toast.show();
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
                    toast = toast.makeText(getActivity().getApplicationContext(), "added number"
                            , Toast.LENGTH_SHORT);
                    toast.show();
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
                    toast = toast.makeText(getActivity().getApplicationContext(), "added email"
                            , Toast.LENGTH_SHORT);
                    toast.show();

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
