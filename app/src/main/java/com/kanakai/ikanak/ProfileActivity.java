package com.kanakai.ikanak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    TextView txtName, txtEmail, txtMobile, txtAddress;
    Button btnLogout, btnAddAddress, btnDeleteAddress;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtMobile = findViewById(R.id.txtMobile);
        txtAddress = findViewById(R.id.txtAddress);
        btnLogout = findViewById(R.id.btnLogout);
        btnAddAddress = findViewById(R.id.btnAddAddress);
        btnDeleteAddress = findViewById(R.id.btnDeleteAddress);

        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        // Load user info
        loadUserProfile();

        // Logout
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        // Add address
        btnAddAddress.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, AddressActivity.class));
        });

        // Delete address
        btnDeleteAddress.setOnClickListener(v -> {
            databaseReference.child("address").removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "Address deleted!", Toast.LENGTH_SHORT).show();
                        txtAddress.setText("No Address Added");
                    });
        });
    }

    private void loadUserProfile() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String email = mAuth.getCurrentUser().getEmail();
                    String mobile = snapshot.child("mobile").getValue(String.class);

                    txtName.setText("Name: " + (name != null ? name : ""));
                    txtEmail.setText("Email: " + (email != null ? email : ""));
                    txtMobile.setText("Mobile: " + (mobile != null ? mobile : "Not Added"));

                    if (snapshot.child("address").exists()) {
                        String home = snapshot.child("address").child("home").getValue(String.class);
                        String city = snapshot.child("address").child("city").getValue(String.class);
                        String state = snapshot.child("address").child("state").getValue(String.class);
                        String pincode = snapshot.child("address").child("pincode").getValue(String.class);

                        txtAddress.setText("Address:\n" + home + ", " + "\n" + city + ", " + state + " - " + pincode);
                    } else {
                        txtAddress.setText("No Address Added");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
