package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.Header;

public class RegistrationActivity extends AppCompatActivity {

    ImageView ivRegister;
    TextView tvRegister;
    EditText etName, etMobileNo, etEmailid, etUsername, etPassword;
    Button btnRegister;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ivRegister = findViewById(R.id.ivRegisterlogo);
        tvRegister = findViewById(R.id.tvRegisterHere);
        etName = findViewById(R.id.etRegisterName);
        etMobileNo = findViewById(R.id.etRegisterMobileNo);
        etEmailid = findViewById(R.id.etRegisterEmailid);
        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegisterbutton);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    etName.setError("Enter your name");
                } else if (etMobileNo.getText().toString().isEmpty()) {
                    etMobileNo.setError("Enter your mobile no");
                } else if (etEmailid.getText().toString().isEmpty()) {
                    etEmailid.setError("Enter your emailid");
                } else if (etUsername.getText().toString().isEmpty()) {
                    etUsername.setError("Enter your username");
                } else if (etPassword.getText().toString().isEmpty()) {
                    etPassword.setError("Enter your password");
                } else if (!etUsername.getText().toString().matches(".*[A-Z].*")) {
                    etUsername.setError("Used 1 UpperCase letter");
                } else if (!etUsername.getText().toString().matches(".*[0-9].*")) {
                    etUsername.setError("Used 1 Number");
                } else if (!etUsername.getText().toString().matches(".*[#,@,$].*")) {
                    etUsername.setError("Used 1 special symbol");
                } else {
                    progressDialog = new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Registration is in process");
                    progressDialog.setCanceledOnTouchOutside(true);
                    progressDialog.show();

                    // PhoneAuthProvider class is used to verify the mobile number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + etMobileNo.getText().toString(),
                            60,TimeUnit.SECONDS,RegistrationActivity.this,new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification completed", Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String verification, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent intent=new Intent(RegistrationActivity.this, VerifyOTPActivity.class);
                                    intent.putExtra("verificationcode",verification);
                                    intent.putExtra("name",etName.getText().toString());
                                    intent.putExtra("mobileno",etMobileNo.getText().toString());
                                    intent.putExtra("emailid",etEmailid.getText().toString());
                                    intent.putExtra("username",etUsername.getText().toString());
                                    intent.putExtra("password",etPassword.getText().toString());
                                    startActivity(intent);

                                }
                            }
                    );
           //         userRegisterDetails();
                }
            }
        });
    }

    private void userRegisterDetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name",etName.getText().toString());
        params.put("mobileno",etMobileNo.getText().toString());
        params.put("emailid",etEmailid.getText().toString());
        params.put("username",etUsername.getText().toString());
        params.put("password",etPassword.getText().toString());
        client.post("http://192.168.177.191:80/msbteAPI/userregisteration.php",params,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String str=response.getString("success");
                            if (str.equals("1"))

                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Registration Successfully Done", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(RegistrationActivity.this, "Already data present", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }

        );
    }
}