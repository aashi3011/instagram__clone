package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ImageActivity extends AppCompatActivity {
    LinearLayout li;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);


         li=(LinearLayout)findViewById(R.id.lin);

        Intent in=getIntent();
       String name= in.getStringExtra("username");
       setTitle(name+"'s feed");
        ParseQuery<ParseObject> q=ParseQuery.getQuery("Image");
        q.whereEqualTo("username",name);
        q.addAscendingOrder("createdAt");
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null)
                {
                    if(objects!=null)
                    {
                        for(ParseObject o:objects) {
                            ParseFile fl =o.getParseFile("image");
                            fl.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e==null)
                                    {
                                        Bitmap bmp = BitmapFactory
                                                .decodeByteArray(
                                                        data, 0,
                                                        data.length);
                                        ImageView im=new ImageView(getApplicationContext());

                                        im.setImageBitmap(bmp);
                                        im.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                                        li.addView(im);

                                    }
                                }
                            });

                        }
                    }
                }
            }
        });





    }
}
