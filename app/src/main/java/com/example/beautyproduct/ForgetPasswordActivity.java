package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class ForgetPasswordActivity extends AppCompatActivity {

    TextView tvotpmobile, tvotpResend;
    EditText etotpcode1, etotpcode2, etotpcode3, etotpcode4, etotpcode5, etotpcode6;
    AppCompatButton btnotpVerify;
    ProgressDialog progressDialog;
    String strVerificationcode, strmobileno;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otpactivity);

            tvotpmobile=findViewById(R.id.tvVerifyotpmobileno);
            tvotpResend = findViewById(R.id.tvVerifyotpResend);
            etotpcode1 = findViewById(R.id.etVerifyOTPinputcode1);
            etotpcode2 = findViewById(R.id.etVerifyOTPinputcode2);
            etotpcode3 = findViewById(R.id.etVerifyOTPinputcode3);
            etotpcode4 = findViewById(R.id.etVerifyOTPinputcode4);
            etotpcode5 = findViewById(R.id.etVerifyOTPinputcode5);
            etotpcode6 = findViewById(R.id.etVerifyOTPinputcode6);
            btnotpVerify = findViewById(R.id.btnVerifyOTPRetry);

            strVerificationcode = getIntent().getStringExtra("verificationcode");
            strmobileno = getIntent().getStringExtra("mobileno");


            btnotpVerify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (etotpcode1.getText().toString().trim().isEmpty() || etotpcode2.getText().toString().trim().isEmpty() ||
                            etotpcode3.getText().toString().trim().isEmpty() || etotpcode4.getText().toString().trim().isEmpty() ||
                            etotpcode5.getText().toString().trim().isEmpty() || etotpcode6.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(ForgetPasswordActivity.this, "Please enter valid OTP", Toast.LENGTH_SHORT).show();
                    }

                    String otpCode=etotpcode1.getText().toString()+etotpcode2.getText().toString()+
                            etotpcode3.getText().toString()+etotpcode4.getText().toString()+
                            etotpcode5.getText().toString()+etotpcode6.getText().toString();

                    if(strVerificationcode!=null) {
                        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
                        progressDialog.setTitle("Verification OTP");
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        PhoneAuthCredential phoneAuthCredential= PhoneAuthProvider.getCredential(
                                strVerificationcode,
                                otpCode);

                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            progressDialog.dismiss();
                                        Intent intent=new Intent(ForgetPasswordActivity.this,SetUpNewPasswordActivity.class);
                                        intent.putExtra("mobileno",strmobileno);
                                        startActivity(intent);
                                        }
                                        else
                                        {
                                            progressDialog.dismiss();
                                            Toast.makeText(ForgetPasswordActivity.this, "OTP verification fail", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    }
                }
            });


            tvotpResend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + strmobileno,
                            60, TimeUnit.SECONDS,ForgetPasswordActivity.this,new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                    Toast.makeText(ForgetPasswordActivity.this, "Verification completed", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                    Toast.makeText(ForgetPasswordActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String newverification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    strVerificationcode=newverification;
                                }
                            }
                    );
                }
            });
            setverifyotp();
        }

        private void setverifyotp () {
            etotpcode1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        etotpcode2.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etotpcode2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        etotpcode3.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etotpcode3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        etotpcode4.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            etotpcode4.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        etotpcode5.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etotpcode5.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().trim().isEmpty()) {
                        etotpcode6.requestFocus();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }
