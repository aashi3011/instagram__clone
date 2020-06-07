package com.example.loginpage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.view.Menu;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null)
        {
            Uri seli=data.getData();
            try {
                Bitmap b=MediaStore.Images.Media.getBitmap(this.getContentResolver(),seli);

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] bytearr=stream.toByteArray();
                ParseFile file=new ParseFile("image.png",bytearr);
                ParseObject obj=new ParseObject("Image");
                obj.put("image",file);
                obj.put("username",ParseUser.getCurrentUser().getUsername());
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            Log.i("photo","uploaded");
                            Toast.makeText(ListActivity.this, "Photo Uploaded", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Log.i("photo",e.getMessage());
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }

    public void getPhoto()
    {
        Intent ph=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ph,1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_GRANTED)
                {
                    getPhoto();
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if(item.getItemId()==R.id.log) {
            ParseUser.logOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=
            PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
            else
            {
                getPhoto();
            }


        }




        return super.onOptionsItemSelected(item);
    }

    ArrayList<String> m;
    ArrayAdapter<String> a;
    ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



setTitle("User's Feed");

         l=(ListView)findViewById(R.id.list);
         m=new ArrayList<String>();
         a=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,m);

        ParseQuery<ParseUser> q=ParseUser.getQuery();
        q.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        q.orderByAscending("username");

        q.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if( e==null && objects!=null && objects.size()>0)
                {

                    for(ParseUser u:objects)
                    {

                            //Log.i("username", u.getUsername());
                            m.add(u.getUsername());
                            Log.i("username",m.get(0));


                    }
                    l.setAdapter(a);
                }

                else
                {
                    Log.i("username",e.getMessage());
                }
            }
        });
        a.notifyDataSetChanged();
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(getApplicationContext(),ImageActivity.class);
                i.putExtra("username",m.get(position));
                startActivity(i);
            }
        });

    }
}
