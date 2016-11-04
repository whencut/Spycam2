package com.yapps.spycam2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static  int ij=0;
    Intent intent;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fab = (Button) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ij==0) {
                    Toast.makeText(MainActivity.this, "set", Toast.LENGTH_LONG).show();
                    repeatingAlaram();
                    //startService(new Intent(getBaseContext(),CameraService.class));
                   ij=1;
                }else if(ij==1){
                    Toast.makeText(MainActivity.this, "already set", Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ij==1) {
                    //cancelAlaram();
                    alarmManager.cancel(pendingIntent);
                    stopService(new Intent(getApplicationContext(), CameraService.class));
                    Toast.makeText(MainActivity.this, "stopped", Toast.LENGTH_LONG).show();
                    ij=0;
                }else  if(ij==0){
                    Toast.makeText(MainActivity.this, "already stopped", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void repeatingAlaram() {

        intent=new Intent(this,CameraService.class);
        pendingIntent = PendingIntent.getService(
                this.getApplicationContext(), 234324243, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //System.currentTimeMillis()
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),5*60*1000  , pendingIntent);

        // Toast.makeText(this, "Alarm set in " + 20 + " seconds",Toast.LENGTH_LONG).show();
    }
  /*  public void cancelAlaram() {

        Intent intent = new Intent(this, CameraService.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //System.currentTimeMillis()
        alarmManager.cancel(pendingIntent);

        Toast.makeText(this, "Alarm stopped",Toast.LENGTH_LONG).show();
    }*/

}
