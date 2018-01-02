package com.gartmedia.brody.loocasino;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class settings extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private Switch ambiance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ambiance = (Switch)
                findViewById(R.id.swtService);


        ambiance.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPrefs = getSharedPreferences("switchstate", Context.MODE_PRIVATE);
        Boolean isOn =sharedPrefs.getBoolean("isOn", false);

        if(isOn)
        {
            ambiance.setChecked(true);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SharedPreferences sharedPrefs = getSharedPreferences("switchstate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        if (isChecked)
        {
            if(!isMyServiceRunning(ambianceService.class))
            {
                startService(new Intent(this, ambianceService.class));
                editor.putBoolean("isOn", true);
            }
        }
        else
        {
            stopService(new Intent(this, ambianceService.class));
            editor.putBoolean("isOn", false);
        }
        editor.apply();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
