package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmMobilenoActivity extends AppCompatActivity {


    EditText etconfirmmobileno;
    AppCompatButton btnverify;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_mobileno);
       etconfirmmobileno=findViewById(R.id.etconfirmregistermobileno);
       btnverify=findViewById(R.id.btnregistermobilenoverify);

       btnverify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (etconfirmmobileno.getText().toString().isEmpty())
               {
                   etconfirmmobileno.setError("Please enter mobileno");
               } else if (etconfirmmobileno.getText().toString().length()!=10)
               {
                   etconfirmmobileno.setError("Please enter valid mobileno");
               }
               else
               {
                   PhoneAuthProvider.getInstance().verifyPhoneNumber(
                           "+91" + etconfirmmobileno.getText().toString(),
                           60, TimeUnit.SECONDS,ConfirmMobilenoActivity.this,new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                               @Override
                               public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                   progressDialog.dismiss();
                                   Toast.makeText(ConfirmMobilenoActivity.this, "Verification completed", Toast.LENGTH_SHORT).show();

                               }

                               @Override
                               public void onVerificationFailed(@NonNull FirebaseException e) {
                                   progressDialog.dismiss();
                                   Toast.makeText(ConfirmMobilenoActivity.this,"Verification failed", Toast.LENGTH_SHORT).show();
                               }

                               @Override
                               public void onCodeSent(@NonNull String verification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                   Intent intent=new Intent(ConfirmMobilenoActivity.this,ForgetPasswordActivity.class);
                                   intent.putExtra("verificationcode",verification);
                                   intent.putExtra("mobileno",etconfirmmobileno.getText().toString());

                                   startActivity(intent);

                               }
                           }
                   );
               }
           }
       });
    }
}