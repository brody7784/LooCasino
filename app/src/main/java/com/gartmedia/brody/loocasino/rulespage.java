package com.gartmedia.brody.loocasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class rulespage extends AppCompatActivity implements View.OnClickListener {

    private TextView rules;
    private Button downloadLogo;
    private StringBuilder text = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulespage);

        rules();
        rules = (TextView)
                findViewById(R.id.txtRules);
        rules.setText(text);

        downloadLogo = (Button)
                findViewById(R.id.btnDownloadLogo);

        downloadLogo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnDownloadLogo:
                Intent intentDownload = new Intent(this, DownloadStategy.class);
                intentDownload.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentDownload);
                break;
        }
    }

    public void rules() {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("rules.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Error reading file!",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error closing reader!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
