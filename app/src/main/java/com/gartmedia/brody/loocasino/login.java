package com.gartmedia.brody.loocasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class login extends AppCompatActivity
implements OnClickListener{

    private EditText nUsername;
    private EditText nPassword;
    private Button login;
    private Button createAccount;

    private String username;
    private String password;

    private int errors=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nUsername =(EditText)
                findViewById(R.id.txtUsername);
        nPassword =(EditText)
                findViewById(R.id.txtPassword);
        login=(Button)
                findViewById(R.id.btnLogin);
        createAccount=(Button)
                findViewById(R.id.btnCreate);

        login.setOnClickListener(this);
        createAccount.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        username=nUsername.getText().toString();
        password=nPassword.getText().toString();
        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();

        switch (v.getId())
        {
            case R.id.btnLogin :
                if(nPassword.length()<5|| nPassword.equals(null))
                {
                    nPassword.setError("Password must be 5 charcters long");
                    errors+=1;
                }
                if(errors==0)
                {
                    Intent intent = new Intent(this, mainmenu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
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
