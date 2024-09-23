package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.LogPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.beautyproduct.common.NetworkChangeListener;
import com.example.beautyproduct.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.impl.conn.LoggingSessionInputBuffer;

public class LoginActivity extends AppCompatActivity {

    ImageView ivlogo;
    TextView tvhere,tvForgetPassword,tvnewuser;
    EditText etusername,etpassword;
    CheckBox checkBox;
    Button btnlogin;
    ProgressDialog progressDialog;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    AppCompatButton btnSignWithGoogle;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ivlogo=findViewById(R.id.ivloginlogo);
        tvhere=findViewById(R.id.tvloginhere);
        tvnewuser=findViewById(R.id.tvloginnewuser);
        tvForgetPassword=findViewById(R.id.tvloginforgetpassword);
        etusername=findViewById(R.id.etloginusername);
        etpassword=findViewById(R.id.etloginpassword);
        checkBox=findViewById(R.id.cblogincheckbox);
        btnlogin=findViewById(R.id.btnlogin1);
        btnSignWithGoogle=findViewById(R.id.acLoginsignwthgoogle);
        googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient= GoogleSignIn.getClient(LoginActivity.this,googleSignInOptions);
        btnSignWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etusername.getText().toString().isEmpty())
                {
                    etusername.setError("Enter your username");
                }
                else if(etpassword.getText().toString().isEmpty())
                {
                    etpassword.setError("Enter your password");
                }
                else if(etusername.getText().toString().length()< 8)
                {
                    etusername.setError("Please enter 8 character");
                }
                else if(etpassword.getText().toString().length()< 8)
                {
                    etpassword.setError("Please enter 8 character");
                }
                else if(!etusername.getText().toString().matches(".*[A-Z].*"))
                {
                    etusername.setError("Used 1 Uppercase letter");
                }
                else if(!etusername.getText().toString().matches(".*[a-z].*"))
                {
                    etusername.setError("Used 1 lowercase letter ");
                }
                else if(!etusername.getText().toString().matches(".*[0-9].*"))
                {
                    etusername.setError("Used 1 number");
                }
                else if(!etusername.getText().toString().matches(".*[#,@,%].*"))
                {
                    etusername.setError("Used 1 special symbol");
                }
                else
                {
                   progressDialog=new ProgressDialog(LoginActivity.this);
                   progressDialog.setTitle("Please wait");
                   progressDialog.setMessage("Process is in login");
                   progressDialog.setCanceledOnTouchOutside(true);
                   progressDialog.show();
                   setuserdialog();
                }
            }
        });

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i=new Intent(LoginActivity.this,ConfirmMobilenoActivity.class);
              startActivity(i);
            }
        });
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                etpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else
            {
                etpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    });

    tvnewuser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i=new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(i);
        }


    });
    }




    private void SignIn() {
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==999)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                Intent intent=new Intent(LoginActivity.this,MyProfileActivity.class);
                startActivity(intent);
                finish();
            } catch (ApiException e) {
                Toast.makeText(this, "SomeThing went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setuserdialog() {

        AsyncHttpClient client=new AsyncHttpClient(); //client and server communication
        RequestParams params=new RequestParams();
        params.put("username",etusername.getText().toString());
        params.put("password",etpassword.getText().toString());
        client.post(Urls.loginUserWebService,params,new JsonHttpResponseHandler()

                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        {

                            try {
                                String status=response.getString("sucess");
                                if (status.equals("1"))
                                {
                                    Toast.makeText(LoginActivity.this, "Login successfully done", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(LoginActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "invalid username and password",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }


                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkChangeListener);
    }


}