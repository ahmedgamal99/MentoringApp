package com.ahmed.mentor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private EditText userName, password;
    private Button login, register;
    private FirebaseAuth fireBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assign();
        //checkLogin();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User_Name = userName.getText().toString();
                String User_Password = password.getText().toString();
                validate(User_Name, User_Password);
            }
        });
    }


    private void validate(String User_Name, String User_Password)
    {
        fireBase.signInWithEmailAndPassword(User_Name, User_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) startActivity(new Intent(MainActivity.this, FinalActivity.class));
                else Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*private void checkLogin()
    {
        FirebaseUser currentUser = fireBase.getCurrentUser();
        if(currentUser == null)
        {
            startActivity(new Intent(MainActivity.this,FinalActivity.class));
            finish();
        }

    }*/


    private void assign()
    {
        userName = findViewById(R.id.userNamePT); //links the elements in the GUI with the code
        password = findViewById(R.id.passwordP);
        login = findViewById(R.id.loginBTN);
        fireBase = FirebaseAuth.getInstance();

        register = (Button) findViewById(R.id.registerBTN);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                } catch(Exception e){
                    Log.e("Exception Tag", e.getMessage(), e);
                }
            }
        });
    }
}