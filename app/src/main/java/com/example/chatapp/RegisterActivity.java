package com.example.chatapp;
//宸
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //Widgets:
    EditText userET, passET, emailET;
    Button registerBtn;

    //Firebase:
    FirebaseAuth auth;
    DatabaseReference myRef;

    //initialising on Start Widgets and Events
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing Widgets:
        userET = findViewById(R.id.userEditText);
        passET = findViewById(R.id.edittext3);
        emailET = findViewById(R.id.edittext);
        registerBtn = findViewById(R.id.buttonRegister);

        //Firebase Auth:
        auth = FirebaseAuth.getInstance();

        // Adding Event Listener to Button Register

        registerBtn.setOnClickListener(v -> {
            String username_text = userET.getText().toString();
            String email_text = emailET.getText().toString();
            String pass_text = passET.getText().toString();

            //Checking if empty
            if (TextUtils.isEmpty(username_text) || TextUtils.isEmpty(email_text) || TextUtils.isEmpty(pass_text)) {
                Toast.makeText(RegisterActivity.this, "請填滿所有空格/Enter All Fields", Toast.LENGTH_SHORT).show();
            } else {
                RegisterNow(username_text, email_text, pass_text);
            }

        });
    }

    //Register with firebase
    private void RegisterNow(final String username, String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userid = firebaseUser.getUid();
                        myRef = FirebaseDatabase.getInstance().getReference("MyUsers")
                                .child(userid);

                        //HashMap

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username);
                        hashMap.put("imageURL", "default");

                        // Opening Main Activity after Success.
                        myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                    finish();
                                }
                            }

                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "不能為空的信箱或密碼/InValid Text", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}