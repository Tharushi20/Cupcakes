package com.example.cupcakes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText firstname_O,lastname_O,emailAddress_O,passWord_O;
    Button register_O;
    boolean valid = true;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        register_O = findViewById(R.id.BTN_registerpage_id);
        firstname_O=findViewById(R.id.TXT_rg_firstname_id);
        lastname_O=findViewById(R.id.TXT_rg_lastname_id);
        emailAddress_O=findViewById(R.id.TXT_rg_email_id);
        passWord_O=findViewById(R.id.TXT_rg_pasword_id);

        register_O.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkField(firstname_O);
                checkField(lastname_O);
                checkField(emailAddress_O);
                checkField(passWord_O);

                if(valid){
                    fAuth.createUserWithEmailAndPassword(emailAddress_O.getText().toString(),passWord_O.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = fAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Account is Created Successfully", Toast.LENGTH_SHORT).show();
                            DocumentReference df = fStore.collection("UserAccount").document(user.getUid());

                            Map<String,Object> userInfo = new HashMap<>();
                            userInfo.put("First_Name",firstname_O.getText().toString());
                            userInfo.put("Last_Name",lastname_O.getText().toString());
                            userInfo.put("UserEmail",emailAddress_O.getText().toString());

                            // access leve;
                            userInfo.put("isUser","1");

                            df.set(userInfo);

                            startActivity( new Intent(getApplicationContext(), LoginActivity.class));
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(RegisterActivity.this, "Failed to Created Account", Toast.LENGTH_SHORT).show();

                        }
                    });

                };



            }
        });

    }

    public boolean checkField(EditText editTextValue){
        if (editTextValue.getText().toString().isEmpty()){
            editTextValue.setError("Error");
            valid = false;
        }else {
            valid = true;
        }
        return valid;
    };
}