package com.android.dev.sineth.ytsmoviefactory.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.dev.sineth.ytsmoviefactory.Kernel.Core;
import com.android.dev.sineth.ytsmoviefactory.Kernel.NetworkDataRetreiver;
import com.android.dev.sineth.ytsmoviefactory.Network.VolleySingleton;
import com.android.dev.sineth.ytsmoviefactory.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private Button login;
    private Button signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username= (EditText) findViewById(R.id.name);
        password= (EditText) findViewById(R.id.password);
        email= (EditText) findViewById(R.id.email);
        login= (Button) findViewById(R.id.buttonLogin);
        signup= (Button) findViewById(R.id.signUp);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkDataRetreiver retreiver=new NetworkDataRetreiver();
                String url = procecssLoginURL();
                erase();
                Boolean suceess = retreiver.sendJSONRequestHeader(url, Login.this);
//                if (suceess){
//                    MaterialDialog dialog = new MaterialDialog.Builder(Login.this)
//                            .title("Login Success")
//                            .content("Welcome back "+username.getText().toString()).show();
//                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkDataRetreiver retreiver=new NetworkDataRetreiver();
                String url = processSignupURL();
                erase();
                Boolean success = retreiver.sendJSONRequestHeader(url, Login.this);
//                if (success){
//                    MaterialDialog dialog = new MaterialDialog.Builder(Login.this)
//                            .title("Sign up Success")
//                            .content("Welcome "+username.getText().toString()).show();
//                }
            }
        });


    }

    private String procecssLoginURL() {
        return  Core.loginURL+"email="+email.getText().toString()+"&password="+password.getText().toString();
    }
    private String processSignupURL(){
        return Core.signUpURL+"email="+email.getText().toString()+"&password="+password.getText().toString()+"&name="+username.getText().toString();
    }
    private void erase(){
        username.setText("");
        email.setText("");
        password.setText("");
    }
}
