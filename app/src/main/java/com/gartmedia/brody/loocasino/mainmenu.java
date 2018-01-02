package com.gartmedia.brody.loocasino;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class mainmenu extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton war;
    private ImageButton blackjack;
    private ImageButton rules;
    private ImageButton contact;
    private ImageButton logout;
    private ImageButton rate;
    private ImageButton settings;
    private TextView cash;
    private TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        Database db = new Database(this);


        war=(ImageButton)
                findViewById(R.id.btnMMWar);
        blackjack=(ImageButton)
                findViewById(R.id.btnMMBlackjack);
        rules =(ImageButton)
                findViewById(R.id.btnRules);
        contact=(ImageButton)
                findViewById(R.id.picLooChip);
        logout=(ImageButton)
                findViewById(R.id.btnLogout);
        rate=(ImageButton)
                findViewById(R.id.btnRate);
        settings=(ImageButton)
                findViewById(R.id.btnSettings);
        cash = (TextView)
                findViewById(R.id.txtCash);
        usernameText = (TextView)
                findViewById(R.id.txtUsername);



        war.setOnClickListener(this);
        blackjack.setOnClickListener(this);
        rules.setOnClickListener(this);
        logout.setOnClickListener(this);
        rate.setOnClickListener(this);
        contact.setOnClickListener(this);
        settings.setOnClickListener(this);


    }

    public void onStart()
    {
        super.onStart();

        TextView cash = (TextView)
                findViewById(R.id.txtCash);
        TextView usernameText = (TextView)
                findViewById(R.id.txtUsername);

        Database db = new Database(this);

        SharedPreferences sharedPrefs = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", "Logout and try again");

        SharedPreferences.Editor editor = sharedPrefs.edit();

        usernameText.setText("User: " + username);

        Cursor cashCursor = db.getCash(username);
        StringBuffer buffer = new StringBuffer();
        if (cashCursor.getCount()==0)
        {
            Toast.makeText(getApplicationContext(), "Accessing cash value failed, Please log out and try again"
                    + username, Toast.LENGTH_LONG).show();
        }

        else
        {

            String cashText = buffer.append(cashCursor.getInt(0)).toString();
            Integer cashInt = Integer.parseInt(cashText);
            editor.putInt("cash", cashInt);
            cash.setText("Balance: $" + cashText);
            editor.apply();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnMMWar:
                Intent intentWar = new Intent(this, wargame.class);
                intentWar.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentWar);
                break;

            case R.id.btnMMBlackjack:
                Intent intentBlackjack = new Intent(this, blackjackgame.class);
                intentBlackjack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentBlackjack);
                break;

            case R.id.btnRules:
                Intent intentrules = new Intent(this, rulespage.class);
                intentrules.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentrules);
                break;
            case R.id.btnLogout:
                SharedPreferences sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("isLoggedIn", false);
                editor.putString("username", "");
                editor.apply();
                Intent intentLogout = new Intent(this, login.class);
                intentLogout.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogout);
                finishAffinity();
                break;
            case R.id.btnRate:
                launchMarket();
                break;
            case R.id.btnSettings:
                Intent intentSettings = new Intent(this, settings.class);
                intentSettings.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentSettings);
                break;
            case R.id.picLooChip:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:brody.gartner@outlook.com");
                intent.setData(data);
                startActivity(intent);
                break;

        }
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
