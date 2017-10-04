package com.gartmedia.brody.loocasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class createaccount extends AppCompatActivity
            implements View.OnClickListener {
        private EditText nEmail;
        private EditText nUsername;
        private EditText nPassword;
        private Button create;
        private String email;
        private String username;
        private String password;
        private int errors=0;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_account);

            nEmail =(EditText)
                    findViewById(R.id.txtEmail);
            nUsername =(EditText)
                    findViewById(R.id.txtUsername);
            nPassword =(EditText)
                    findViewById(R.id.txtPassword);



            create=(Button)
                    findViewById(R.id.btnCreate);


            create.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            email=nEmail.getText().toString();
            username=nUsername.getText().toString();
            password=nPassword.getText().toString();

            if(password.length()<5|| password == null)
            {
                nPassword.setError("Password must be 5 characters long");
                errors+=1;
            }
            if(!isValidEmail(email))
            {
                nEmail.setError("Email must be valid");
                errors+=1;
            }

            if (errors==0)
            {
                Intent intent = new Intent(this, login.class);// New activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
            errors=0;
        }

    public final static boolean isValidEmail(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}