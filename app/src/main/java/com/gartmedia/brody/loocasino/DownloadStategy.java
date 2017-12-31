package com.gartmedia.brody.loocasino;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadStategy extends AppCompatActivity implements View.OnClickListener {

    private Button checkPerm;
    private ImageView imgStrategy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_strategy);
        new DownloadFileFromURL().execute("https://raw.githubusercontent.com/brody7784/LooCasino/" +
                "master/app/src/main/res/drawable/stategy.png");

        checkPerm = (Button)
                findViewById(R.id.checkPermissions);
        imgStrategy = (ImageView)
                findViewById(R.id.imgStrategy);

        checkPerm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.checkPermissions:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                break;
        }

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog= new ProgressDialog(DownloadStategy.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Starting download");
            pDialog.setMessage("Loading... Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                String root = Environment.getExternalStorageDirectory().toString();

                URL url = new URL(f_url[0]);

                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                OutputStream output = new FileOutputStream(root + "/strategy.png");
                byte data[] = new byte[1024];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();

                output.close();
                input.close();

            } catch (Exception e) {
                return "Error downloading file, Try checking app permissions!";
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result!=null)
            {
                checkPerm.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Downloaded!",Toast.LENGTH_LONG).show();
                String pathName = Environment.getExternalStorageDirectory()+"/strategy.png";

                Bitmap bmp = BitmapFactory.decodeFile(pathName);
                imgStrategy.setImageBitmap(bmp);
                imgStrategy.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
                imgStrategy.getLayoutParams().height= RelativeLayout.LayoutParams.MATCH_PARENT;
                imgStrategy.requestLayout();

            }

            pDialog.dismiss();
        }
    }
}
