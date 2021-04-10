package com.example.hund_hunter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, familia, email, password;
    boolean find, lost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);//TODO: сделать чтьобы съехжало при появлении клавиатуры
        name = findViewById(R.id.editTextTextPersonName);
        familia = findViewById(R.id.editTextTextPersonName2);
        email = findViewById(R.id.editTextTextEmailAddress2);
        password = findViewById(R.id.editTextTextPassword);

        // Проверка на непустые поля


        Bundle extras = getIntent().getExtras();
        find = extras.getBoolean("find");
        lost = extras.getBoolean("lost");
    }

    public void findButton(View view){
        String nameTxt = name.getText().toString();
        String familiaTxt = familia.getText().toString();
        String emailTxt = email.getText().toString();
        String passwordTxt = password.getText().toString();

        if(!nameTxt.equals("")&&!familiaTxt.equals("")&&!emailTxt.equals("")&&!passwordTxt.equals("")){
            FirebaseDatabase.getInstance().getReference().push().setValue(nameTxt+" "+familiaTxt+" "+emailTxt+" "+passwordTxt);
        }else{
            Toast.makeText(RegistrationActivity.this, "заполните поля", Toast.LENGTH_LONG).show();
            return;
        }

        Intent reg_act = null;
        if(find){
            reg_act = new Intent(RegistrationActivity.this, SeekerActivity.class);
        }else if(lost){
            reg_act = new Intent(RegistrationActivity.this, OrderCreationActivity.class);
        }
        startActivity(reg_act);
    }

    /*public void nullTest(String string){
        try {
            strin
        }

    }*/
}