package com.prajwal.textrecognision;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    Button bt_submit;
    EditText et_email, et_password, et_c_password;
    TextView login;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        bt_submit = findViewById(R.id.su_bt_submit);
        et_email = findViewById(R.id.su_et_email);
        et_c_password = findViewById(R.id.su_et_confirm_password);
        et_password = findViewById(R.id.su_et_password);
        login = findViewById(R.id.su_tv_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                finish();
            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    if (et_password.getText().toString().equals(et_c_password.getText().toString())) {
                        signUp(et_email.getText().toString(), et_password.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        makeEmpty();
                    }
                }
                catch (Exception e){
                    Log.e("Signup Error", e.getMessage());
                }
            }
        });
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void signUp(String mail, String password){
        if (mail.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter the field", Toast.LENGTH_SHORT).show();
            makeEmpty();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!(task.isSuccessful())){
                        Toast.makeText(getApplicationContext(), "Registration unsuccessful, Try Again Later", Toast.LENGTH_SHORT).show();
                        makeEmpty();
                        progressBar.setVisibility(View.GONE);
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        finish();
                    }
                }
            });
        }
    }
    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void makeEmpty(){
        et_password.setText("");
        et_c_password.setText("");
        et_email.setText("");
        //et_email.getFocusable();
    }
}