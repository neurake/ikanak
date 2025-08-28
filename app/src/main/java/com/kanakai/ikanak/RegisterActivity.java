//package com.kanakai.ikanak;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Patterns;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    EditText edtName, edtEmail, edtPassword;
//    Button btnRegister;
//
//    DatabaseReference databaseReference;  // Firebase reference
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        edtName = findViewById(R.id.edtName);
//        edtEmail = findViewById(R.id.edtEmail);
//        edtPassword = findViewById(R.id.edtPassword);
//        btnRegister = findViewById(R.id.btnRegister);
//
//        // Reference to "Users" node in Firebase Realtime DB
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//
//        btnRegister.setOnClickListener(v -> {
//            String name = edtName.getText().toString().trim();
//            String email = edtEmail.getText().toString().trim();
//            String password = edtPassword.getText().toString().trim();
//
//            // Validation
//            if (TextUtils.isEmpty(name)) {
//                edtName.setError("Enter your name");
//            } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                edtEmail.setError("Enter valid email");
//            } else if (TextUtils.isEmpty(password) || password.length() < 6) {
//                edtPassword.setError("Password must be at least 6 characters");
//            } else {
//                // Generate unique ID for each user
//                String userId = databaseReference.push().getKey();
//
//                // Create User object
//                User user = new User(name, email, password);
//
//                // Save to Firebase
//                if (userId != null) {
//                    databaseReference.child(userId).setValue(user)
//                            .addOnCompleteListener(task -> {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
//                                    // Go to HomeActivity
//                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                } else {
//                                    Toast.makeText(RegisterActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                }
//                            });
//                }
//            }
//        });
//    }
//
//    // User model class
//    public static class User {
//        public String name, email, password;
//
//        public User() {
//        } // Required empty constructor
//
//        public User(String name, String email, String password) {
//            this.name = name;
//            this.email = email;
//            this.password = password;
//        }
//    }
//}
package com.kanakai.ikanak;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName,edtMobile, edtEmail, edtPassword;
    Button btnRegister;

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtName = findViewById(R.id.edtName);
        edtMobile = findViewById(R.id.edtMobile);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String mobile = edtMobile.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // Validation
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Enter your name");
            return;
        }
        if (TextUtils.isEmpty(mobile)||mobile.length()<10) {
            edtMobile.setError("Enter valid Mobile Number");
            return;
        }
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter valid email");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
            return;
        }

        // Register in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Get user ID
                        String userId = mAuth.getCurrentUser().getUid();

                        // Save  name and mobile in Realtime Database
                        databaseReference.child(userId).child("name").setValue(name);
                        databaseReference.child(userId).child("mobile").setValue(mobile);

                                        Toast.makeText(RegisterActivity.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                        finish();


                    } else {
                        Toast.makeText(RegisterActivity.this, "Auth Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
