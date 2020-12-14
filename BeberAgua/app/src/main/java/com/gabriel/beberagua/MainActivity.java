package com.gabriel.beberagua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button _btnNotify;
    private EditText _etxIntervalNumber;
    private TimePicker _tpckTime;

    private int _hour;
    private int _minute;
    private int _interval;
    private boolean _activated = false;

    private SharedPreferences _preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this._btnNotify         = findViewById(R.id.btnNotify);
        this._etxIntervalNumber = findViewById(R.id.etxIntervalNumber);
        this._tpckTime          = findViewById(R.id.tpckTime);

        this._tpckTime.setIs24HourView(true);
        this.loadSharedPreferences();
        this.changeNotifyButton(false);
    }

    private void loadSharedPreferences() {
        this._preferences   =  getSharedPreferences("db", Context.MODE_PRIVATE);
        this._activated     = this._preferences.getBoolean("_activated", false);

        if(this._activated){
            this._minute    = this._preferences.getInt("_minute", this._tpckTime.getCurrentMinute());
            this._hour      = this._preferences.getInt("_hour", this._tpckTime.getCurrentHour());
            this._interval  = this._preferences.getInt("_interval", 0);

            this._tpckTime.setCurrentHour(this._hour);
            this._tpckTime.setCurrentMinute(this._minute);
            this._etxIntervalNumber.setText(String.valueOf(this._interval));
        }
    }

    public void notifyClick(View view) {
       String sInterval = this._etxIntervalNumber.getText().toString();

       if(sInterval.isEmpty()){
           Toast.makeText(this, R.string.error_msg, Toast.LENGTH_LONG).show();
           return;
       }

       this._hour       = this._tpckTime.getCurrentHour();
       this._minute     = this._tpckTime.getCurrentMinute();
       this._interval   = Integer.parseInt(sInterval);

        this.changeNotifyButton(true);
        this.savePreferences();

        Log.d("Teste", "hora: "+ this._hour + " minuto: " + this._minute + " intervalo: " + this._interval);
    }

    private void savePreferences() {
        SharedPreferences.Editor editor = _preferences.edit();
        editor.putBoolean("_activated", this._activated);
        editor.putInt("_interval", this._interval);
        editor.putInt("_hour", this._hour);
        editor.putInt("_minute", this._minute);

        if(!this._activated){
         editor.remove("_interval");
         editor.remove("_hour");
         editor.remove("_minute");
        }

        editor.apply();
    }

    private void changeNotifyButton(boolean btnClicked) {
        if(btnClicked){
            this._activated = !this._activated;
        }

        int buttonText = this._activated ? R.string.pause : R.string.notify;

        this._btnNotify.setText(buttonText);

        int colorUnpressed   = ContextCompat.getColor(this, R.color.colorAccent);
        int colorPressed     = ContextCompat.getColor(this, android.R.color.black);
        int color = this._activated ? colorPressed : colorUnpressed;
        this._btnNotify.setBackgroundColor(color);
    }
}