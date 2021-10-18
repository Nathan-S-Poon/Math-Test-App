package curtin.edu.mathtestapp.registrationfrag;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import curtin.edu.mathtestapp.R;
import curtin.edu.mathtestapp.mathstestFrag.StudentSelectionFrag;
import curtin.edu.mathtestapp.mathstestFrag.TestFrag;
import curtin.edu.mathtestapp.model.Student;

public class  OnlinePhotoFrag extends Fragment
{
    private Button searchButton;
    private EditText searchInput;
    private RecyclerView photoRecycler;
    private ArrayList<Bitmap[]> list;
    private Bitmap[] bitArr;
    private int bitCount;
    private int imgCount;
    private int stuID;
    private Bitmap selectedPhoto;
    private Button leave;
    private String firstname;
    private String lastName;
    private ArrayList<Integer> numbers;
    private ArrayList<String> emails;
    private String emailStr;
    private String phoneStr;
    private File photo;
    private boolean isEdit;

    public void setRecycler()
    {
        photoRecycler.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        ViewPhotoAdapter photoAdapter = new ViewPhotoAdapter(list);
        photoRecycler.setAdapter(photoAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        super.onSaveInstanceState(bundle);
        bundle.putString("input", searchInput.getText().toString());
        bundle.putSerializable("list", list);
        bundle.putInt("ID", stuID);
        bundle.putString("first", firstname);
        bundle.putString("last", lastName);
        bundle.putSerializable("photo", photo);
        bundle.putString("emailStr", emailStr);
        bundle.putString("phoneStr", phoneStr);
        bundle.putIntegerArrayList("phone", numbers);
        bundle.putStringArrayList("emails", emails);
        bundle.putBoolean("edit", isEdit);

    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        bitArr = new Bitmap[3];
        list = new ArrayList<Bitmap[]>();
        bitCount = 0;
        if(bundle != null)
        {
            isEdit = bundle.getBoolean("edit");
            stuID = bundle.getInt("ID");
            firstname = bundle.getString("first");
            lastName = bundle.getString("last");
            numbers = bundle.getIntegerArrayList("phone");
            emails = bundle.getStringArrayList("emails");
            photo = (File) bundle.getSerializable("photo");
            emailStr = bundle.getString("emailStr");
            phoneStr = bundle.getString("phoneStr");
            list = (ArrayList<Bitmap[]>) bundle.getSerializable("list");
            searchInput.setText(bundle.getString("input"));
            setRecycler();
        }
        getParentFragmentManager().setFragmentResultListener("regToOnline", this, new FragmentResultListener()
        {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result)
            {
                isEdit = result.getBoolean("isEdit");
                stuID = result.getInt("ID");
                firstname = result.getString("first");
                lastName = result.getString("last");
                numbers = result.getIntegerArrayList("phone");
                emails = result.getStringArrayList("emails");
                emailStr = result.getString("emailStr");
                phoneStr = result.getString("phoneStr");
                photo = (File) result.getSerializable("photo");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.online_photos, ui, false);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        searchInput = (EditText) view.findViewById(R.id.searchInput);
        photoRecycler = (RecyclerView) view.findViewById(R.id.imageRecycler);
        leave = (Button) view.findViewById(R.id.leaveButton);

        leave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle result = new Bundle();
                result.putInt("ID", stuID);
                result.putString("first", firstname);
                result.putString("last", lastName);
                result.putString("emailStr", emailStr);
                result.putString("phoneStr", phoneStr);
                result.putSerializable("photo", photo);
                result.putIntegerArrayList("phone", numbers);
                result.putStringArrayList("emails", emails);
                result.putBoolean("isEdit", isEdit);
                getParentFragmentManager().setFragmentResult("onlineToReg", result);
                FragmentManager fm = getParentFragmentManager();
                RegisterStudentFragment frag = (RegisterStudentFragment) fm.findFragmentById(R.id.registration);
                if(frag == null)
                {
                    frag = new RegisterStudentFragment();
                    fm.beginTransaction().replace(R.id.frame, frag).commit();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!searchInput.getText().toString().equals(""))
                {
                    String searchValues = searchInput.getText().toString();
                    new PixabayTask().execute(searchValues);
                }

            }
        });


        return view;
    }



    public void makeToast(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }


    /************************************8
     * Recycler view
     ************************************/
    private class ViewPhotoAdapter extends RecyclerView.Adapter<PhotoDataHolder>
    {
        private ArrayList<Bitmap[]> pList;
        public ViewPhotoAdapter(ArrayList<Bitmap[]> pList)
        {
            this.pList = pList;
        }
        @Override
        public int getItemCount()
        {
            return pList.size();
        }

        @Override
        public PhotoDataHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LayoutInflater li = LayoutInflater.from(getActivity());
            View view = li.inflate(R.layout.view_list_row, parent, false);
            return new PhotoDataHolder(view);
        }

        @Override
        public void onBindViewHolder(PhotoDataHolder vh, int index)
        {
            vh.bind(pList.get(index));
        }

    }

    /*****************************
     * MyDataHolder
     */
    private class PhotoDataHolder extends RecyclerView.ViewHolder
    {
        private TextView name;
        private ImageView image1;
        private ImageView image2;
        private ImageView image3;

        public PhotoDataHolder(@NonNull View itemView)
        {
            super(itemView);
            //get UI elements
            name = (TextView) itemView.findViewById(R.id.nameText);
            image1 = (ImageView) itemView.findViewById(R.id.image1);
            image2 = (ImageView) itemView.findViewById(R.id.image2);
            image3 = (ImageView) itemView.findViewById(R.id.image3);
        }
        public void bind(Bitmap[] data)
        {
            if (data != null)
            {
                image1.setImageBitmap(data[0]);
                if(data.length > 1)
                {
                    image2.setImageBitmap(data[1]);
                }
                if(data.length == 3)
                {
                    image3.setImageBitmap(data[2]);
                }

                image1.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                image2.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });
                image3.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {

                    }
                });


            }

        }
    }




//TODO reference
    /***********************************
     *  Async tasks
     ********************************/
    private class PixabayTask extends AsyncTask<String, String ,String>
    {
        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            makeToast(values[0]);
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String...params)
        {
            String data = "";
            Uri.Builder urlStr = Uri.parse("https://pixabay.com/api/").buildUpon();
            urlStr.appendQueryParameter("key", "23319229-94b52a4727158e1dc3fd5f2db");
            urlStr.appendQueryParameter("q", params[0]);
            String urlString = urlStr.build().toString();

            HttpURLConnection conn = null;
            try
            {
                URL url = new URL(urlString);
                conn = (HttpURLConnection) url.openConnection();
                if(conn == null)
                {
                    //error
                    throw new IOException("no connection");
                }
                else if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    //throw error
                    throw new IOException("no connection");
                }
                data = downloadToString(conn);

            }
            catch (MalformedURLException e)
            {
                //TODO what to put here
                publishProgress(e.getMessage());
            }
            catch (IOException e)
            {
                publishProgress(e.getMessage());
            }

            return data;
        }
        @Override
        protected void onPostExecute(String data)
        {
            if(!data.equals(""))
            {
                String imageUrl = null;
                try
                {
                    JSONObject jBase = new JSONObject(data);
                    JSONArray jHits = jBase.getJSONArray("hits");
                    if(jHits.length()>0)
                    {
                        JSONObject jHitsItem = jHits.getJSONObject(0);
                        imageUrl = jHitsItem.getString("largeImageURL");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    makeToast(e.getMessage());
                }
                if(imageUrl != null)
                {
                    new ImageURLTask().execute(imageUrl);
                }

            }
            else
            {
                //error
                makeToast("no data");
            }
        }

    }

    //Async task 2
    private class ImageURLTask extends AsyncTask<String, String, Bitmap>
    {
        @Override
        protected void onProgressUpdate(String... values)
        {
            super.onProgressUpdate(values);
            makeToast(values[0]);
        }
        @Override
        protected Bitmap doInBackground(String...params)
        {
            Bitmap image = null;
            Uri.Builder url = Uri.parse(params[0]).buildUpon();
            String  urlString = url.build().toString();
            HttpURLConnection conn = null;
            try
            {
                URL url2 = new URL(urlString);
                conn = (HttpURLConnection) url2.openConnection();
                if(conn == null)
                {
                    //error
                    throw new IOException("no connection");
                }
                else if(conn.getResponseCode()==HttpURLConnection.HTTP_OK)
                {
                    //throw error
                    throw new IOException("no connection");
                }

            }
            catch (MalformedURLException e)
            {
                //TODO what to put here
                publishProgress(e.getMessage());
            }
            catch (IOException e)
            {
                publishProgress(e.getMessage());
            }
            //Input stream
            try
            {
                InputStream inputStream = conn.getInputStream();
                byte[] byteData = null;
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] bData = new byte[4096];
                int progress = 0;
                while ((nRead = inputStream.read(bData, 0, bData.length)) != -1)
                {
                    buffer.write(bData, 0, nRead);
                    progress = progress + nRead;
                    Log.d("Hello", String.valueOf(byteData.length));
                    byteData = buffer.toByteArray();
                    image = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                publishProgress(e.getMessage());
            }

            return image;
        }

        @Override
        protected void onPostExecute(Bitmap image)
        {
            if(image != null)
            {
                //put image
                if(bitCount != 3)
                {
                    bitArr[bitCount] = image;
                    bitCount++;
                    imgCount++;
                    if (imgCount == 50)
                    {
                        list.add(bitArr);
                    }
                }
                else if(bitCount == 3)
                {
                    list.add(bitArr);
                    bitArr = new Bitmap[3];
                    bitArr[0] = image;
                    bitCount = 1;
                    imgCount++;
                }

            }
            else
            {
                //error
                makeToast("no image found");
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String downloadToString(HttpURLConnection conn)
    {
        String data = "";
        try
        {
            InputStream inputStream = conn.getInputStream();
            byte[] byteData = IOUtils.toByteArray(inputStream);
            data = new String(byteData, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {

        }
        return data;
    }



}















