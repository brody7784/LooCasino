package com.gartmedia.brody.loocasino;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.SharedPreferences;

public class login extends AppCompatActivity
implements OnClickListener{
    Database db;
    private EditText nUsername;
    private EditText nPassword;
    private Button login;
    private Button createAccount;

    private String username;
    private String password;

    private int errors = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database(this);
        nUsername = (EditText)
                findViewById(R.id.txtUsername);
        nPassword = (EditText)
                findViewById(R.id.txtPassword);
        login= (Button)
                findViewById(R.id.btnLogin);
        createAccount = (Button)
                findViewById(R.id.btnCreate);

        login.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        username=nUsername.getText().toString();
        password=nPassword.getText().toString();

        switch (v.getId())
        {
            case R.id.btnLogin :

                String dbPassword = db.login(username);

                if (username.length()<4 ||username.length()>13 || username == null)
                {
                    nUsername.setError("Username must be 4-13 characters");
                    errors +=1;
                }
                if(password.length()<5||password.length()>20 || password == null) {
                    nPassword.setError("Password must be 5-20 characters");
                    errors += 1;
                }
                if(errors==0)
                {
                    if(password.equals(dbPassword))
                    {
                        Intent intent = new Intent(this, mainmenu.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.putString("username", username);
                        editor.apply();

                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Welcome Back " + username, Toast.LENGTH_SHORT).show();

                        finish();
                    }

                    else
                    {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            break;

            case R.id.btnCreate:

                Intent intent = new Intent(this, createaccount.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
        }
        errors=0;
    }
}
