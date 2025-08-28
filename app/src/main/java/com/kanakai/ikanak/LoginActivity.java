////package com.kanakai.ikanak;
////
////import android.os.Bundle;
////
////import androidx.activity.EdgeToEdge;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////
////public class LoginActivity extends AppCompatActivity {
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        EdgeToEdge.enable(this);
////        setContentView(R.layout.activity_login);
////        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////    }
////}
////
//
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
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.kanakai.ikanak.RegisterActivity;
//
//public class LoginActivity extends AppCompatActivity {
//
//    EditText  edtEmail, edtPassword;
//    Button btnLogin;
//    Button btnRegister;
//
//    FirebaseAuth mAuth;
//    DatabaseReference databaseReference;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        edtEmail = findViewById(R.id.edtEmail);
//        edtPassword = findViewById(R.id.edtPassword);
//        btnLogin = findViewById(R.id.btnLogin);
//        btnRegister = findViewById(R.id.btnRegister);
//
//        mAuth = FirebaseAuth.getInstance();
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//
//        btnLogin.setOnClickListener(v -> loginUser());
////        btnRegister.setOnClickListener(v -> registeruser());
//    }
//
//    private void loginUser() {
//        String email = edtEmail.getText().toString().trim();
//        String password = edtPassword.getText().toString().trim();
//
//        // Validation
//        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            edtEmail.setError("Enter valid email");
//            return;
//        }
//        if (TextUtils.isEmpty(password) || password.length() < 6) {
//            edtPassword.setError("Password must be at least 6 characters");
//            return;
//        }
//
//        // Firebase Authentication while login user
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        // Get user ID
//                        String userId = mAuth.getCurrentUser().getUid();
//
//                        // Save only name in Realtime Database
//                        databaseReference.child(userId).child("name").setValue(email)
//                                .addOnCompleteListener(dbTask -> {
//                                    if (dbTask.isSuccessful()) {
//                                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
//                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                        finish();
//                                    } else {
//                                        Toast.makeText(LoginActivity.this, "Login Error: " + dbTask.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(LoginActivity.this, "Auth Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                    }
//                });
//    }
//}


package com.kanakai.ikanak;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    TextView txtGoRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        txtGoRegister = findViewById(R.id.txtGoRegister);

        mAuth = FirebaseAuth.getInstance();

        // Login button action
        btnLogin.setOnClickListener(v -> loginUser());

        // Redirect to RegisterActivity
        txtGoRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Enter valid email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Enter password");
            return;
        }

        // Authenticate with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
