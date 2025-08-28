package com.kanakai.ikanak;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddressActivity extends AppCompatActivity {

    EditText edtHome, edtCity, edtState, edtPincode;
    Button btnSave;

    DatabaseReference databaseReference;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        edtHome = findViewById(R.id.edtHome);
        edtCity = findViewById(R.id.edtCity);
        edtState = findViewById(R.id.edtState);
        edtPincode = findViewById(R.id.edtPincode);
        btnSave = findViewById(R.id.btnSave);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("address");

        btnSave.setOnClickListener(v -> saveAddress());
    }

    private void saveAddress() {
        String home = edtHome.getText().toString().trim();
        String city = edtCity.getText().toString().trim();
        String state = edtState.getText().toString().trim();
        String pincode = edtPincode.getText().toString().trim();

        if (TextUtils.isEmpty(home) || TextUtils.isEmpty(city) || TextUtils.isEmpty(pincode)) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Address address = new Address(home,city, state, pincode);

        databaseReference.setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddressActivity.this, "Address Saved!", Toast.LENGTH_SHORT).show();
                finish(); // return to ProfileActivity
            } else {
                Toast.makeText(AddressActivity.this, "Failed to save address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Address Model
    public static class Address {
        public String home, city, state, pincode;

        public Address() { } // Default constructor

        public Address(String home, String city, String state, String pincode) {
            this.home = home;
            this.city = city;
            this.state = state;
            this.pincode = pincode;
        }
    }
}
