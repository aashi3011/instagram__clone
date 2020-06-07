package com.example.loginpage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    String users;
    String pwds;
    ParseUser u;
    EditText pwd;
    ConstraintLayout rel;

    public void showl()
    {
        Intent i=new Intent(getApplicationContext(),ListActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pwd = (EditText) findViewById(R.id.pwd);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        pwd.setOnKeyListener(this);
        rel = (ConstraintLayout) findViewById(R.id.relative);
        rel.setOnClickListener(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        if(ParseUser.getCurrentUser()!=null)
            showl();
    }

    public void sp(View v) {


        Button c = (Button) findViewById(R.id.signup);
        int alphaofsignup = (int) c.getAlpha();
        EditText user = (EditText) findViewById(R.id.user);
        users = user.getText().toString();

        pwds = pwd.getText().toString();
        if (users.matches("") || pwds.matches(""))
            Toast.makeText(MainActivity.this, "Username and Password is required", Toast.LENGTH_SHORT).show();

        if (alphaofsignup == 0) {
            ParseUser.logInInBackground(users, pwds, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e == null && user != null) {
                        showl();
                    } else {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            u = new ParseUser();
            // Log.i("username",user.toString());


            u.setUsername(users);
            u.setPassword(pwds);
            u.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null)
                    {
                       showl();
                    }
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }


    }


    public void gotos(View v) {
        TextView t = (TextView) findViewById(R.id.create);
        if (t.getAlpha() == 1)
            t.setAlpha(0);
        else
            t.setAlpha(1);
        TextView tr = (TextView) findViewById(R.id.done);
        if (tr.getAlpha() == 1)
            tr.setAlpha(0);
        else
            tr.setAlpha(1);
        Button c = (Button) findViewById(R.id.signup);
        if (c.getAlpha() == 1)
            c.setAlpha(0);
        else
            c.setAlpha(1);
        Button cd = (Button) findViewById(R.id.login);
        if (cd.getAlpha() == 1)
            cd.setAlpha(0);
        else
            cd.setAlpha(1);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            sp(v);
        }
        return false;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.relative)
        {

            InputMethodManager m=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            if(m!=null)
            {
                m.hideSoftInputFromWindow(v.getWindowToken(),0);
            }


        }

    }
}

