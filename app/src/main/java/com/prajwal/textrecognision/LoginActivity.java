package com.prajwal.textrecognision;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button bt_submit;
    EditText et_email, et_password;
    TextView register, forgotPassword;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bt_submit = findViewById(R.id.bt_submit);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        register = findViewById(R.id.tv_register);
        firebaseAuth = FirebaseAuth.getInstance();
        forgotPassword = findViewById(R.id.resetPassword);
        progressBar = findViewById(R.id.splash_progress);
        progressBar.setVisibility(View.GONE);
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //progressBar.setVisibility(View.VISIBLE);
                try {
                    logIn(et_email.getText().toString(), et_password.getText().toString());
                }
                catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Error:"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //progressBar.setVisibility(View.GONE);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUpActivity.class));
                finish();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (TextUtils.isEmpty(et_email.getText().toString())) {
                    Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                requestChangePassword();

            }
        });
            }
    private void requestChangePassword(){
        final String[] email = new String[1];
        email[0] = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.myDialog);
        builder.setTitle("Reset Password");
        builder.setCancelable(false);

        final EditText input = new EditText(this);
        input.setBackgroundColor(getColor(R.color.colorBlack));
        input.setTextColor(getColor(R.color.colorPrimary));
        input.setHintTextColor(getColor(R.color.colorGrey));
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Enter your Email");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                email[0] = input.getText().toString();
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.sendPasswordResetEmail(email[0])

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                            }
                        });

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();

    }
    private void logIn(String mail,String password){

        if (mail.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please Enter the field", Toast.LENGTH_SHORT).show();
            makeEmpty();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!(task.isSuccessful())){
                        Toast.makeText(getApplicationContext(), "Login unsuccessful, Try Again Later", Toast.LENGTH_SHORT).show();
                        makeEmpty();
                        User.islogin = false;
                        //finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(),TextRecognition.class));
                        User.islogin = true;
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        User.email = user.getEmail();



                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

    }
    private void makeEmpty(){
        et_password.setText("");
        et_email.setText("");

    }
}