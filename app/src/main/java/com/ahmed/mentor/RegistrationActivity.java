package com.ahmed.mentor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    //TODO: remove everything related to image uploading!!

    private EditText fName, lName, email, password, Rphone;
    private CheckBox freshman;
    private TextView login;
    private Button register;
    private FirebaseAuth fireBase;
    private FirebaseFirestore fireStore;
    private String userID;
    private boolean isFreshman;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FirebaseApp.initializeApp(this);
        fireBase = FirebaseAuth.getInstance();
        fireStore = FirebaseFirestore.getInstance();
        assign();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { pushToDatabase();}
        });


    }


    private void pushToDatabase()
    {
        if (validate()) {
            final String userEmail = email.getText().toString().trim();
            final String userPassword = password.getText().toString().trim();
            final String firstName = fName.getText().toString().trim();
            final String lastName = lName.getText().toString().trim();
            final String phone = Rphone.getText().toString().trim();

            //   boolean checked = ((CheckBox) view).isChecked();
            if(freshman.isChecked()) isFreshman = true;
            else isFreshman = false;


            fireBase.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                        userID = fireBase.getCurrentUser().getUid();
                        DocumentReference documentReference = fireStore.collection("Users").document(userID);
                        Map<String,Object> user = new HashMap<>();
                        user.put("First Name", firstName);
                        user.put("Last Name", lastName);
                        user.put("Email", userEmail);
                        user.put("Password", userPassword);
                        user.put("Freshman", isFreshman);
                        user.put("Phone", phone);
                        user.put("Matched", "null");
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "successfully created " + userID);
                            }
                        });
                        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    }

                    else Toast.makeText(RegistrationActivity.this, "Registration Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
    private void assign() {

        Rphone = findViewById(R.id.phonePT);
        freshman = findViewById(R.id.freshmanCB);
        fName = findViewById(R.id.firstNamePT);
        lName = findViewById(R.id.lastNamePT);
        email = findViewById(R.id.emailE);
        password = findViewById(R.id.passwordP);
        register = findViewById(R.id.registerBtn);
        // fireBase = FirebaseDatabase.getInstance().getReference().child("User");

        login = findViewById(R.id.LoginPT);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
            }
        });
    }

    private boolean validate() {
        String firstName = fName.getText().toString();
        String lastName = lName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Phone = Rphone.getText().toString();

        // TODO: Check if data is in database
        if (firstName.isEmpty() || lastName.isEmpty() || Email.isEmpty() || Password.isEmpty()) {
            Toast.makeText(this, "Please Enter all Neccesary Details", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
