package com.example.hund_hunter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrderCreationActivity extends AppCompatActivity {
    private EditText revard;
    int DIALOG_TIME = 1;
    int myHour = 14;
    int myMinute = 35;
    TextView tvTime;
    String coords;
    String time;

    DatabaseReference ref;
    DatabaseReference usersRef;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_creation);
        tvTime = (TextView) findViewById(R.id.tvTime);

        revard = (EditText) findViewById(R.id.reward);


    }
    public void setLocation(View view){
        Intent set_act = new Intent(OrderCreationActivity.this, SetLocetionActivity.class);
        startActivityForResult(set_act, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        coords = data.getStringExtra("coords");


    }

    public void onclick(View view) {
        showDialog(DIALOG_TIME);


    }

    protected Dialog onCreateDialog(int id) {
        if (id == DIALOG_TIME) {
            TimePickerDialog tpd = new TimePickerDialog(this, myCallBack, myHour, myMinute, true);
            return tpd;
        }
        return super.onCreateDialog(id);
    }

    TimePickerDialog.OnTimeSetListener myCallBack = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            myHour = hourOfDay;
            myMinute = minute;
            String minNull = "";
            String hourNull = "";
            if(myHour <= 10 ){
                hourNull = "0";
            }

            if(myMinute <= 10 ){
                minNull = "0";
            }
            tvTime.setText(hourNull + myHour + " : " + minNull + myMinute );
            time = hourNull + myHour + " : " + minNull + myMinute;
        }
    };

    public void submit(View view){
        EditText comment = findViewById(R.id.comment);
        EditText price = findViewById(R.id.reward);
        String commentTxt = comment.getText().toString();
        String pricetTxt = price.getText().toString();
        if(coords.equals("")||time.equals("")||commentTxt.equals("")||pricetTxt.equals("")){
            return;
        }
        Log.d("yy", "added");
        ref = FirebaseDatabase.getInstance().getReference();
        usersRef = ref.child("orders");
        //DatabaseReference newUsersRef = usersRef.push();
        SharedPreferences mySharedPreferences = getSharedPreferences(StartActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
        usersRef.push().setValue(new Order(mySharedPreferences.getString(StartActivity.APP_PREFERENCES_EMAIL,""), pricetTxt, commentTxt, coords, time));
    }


}