package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beautyproduct.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SetUpNewPasswordActivity extends AppCompatActivity {

    EditText etnewpassword,etconfirmpassword;
    AppCompatButton btnSetuppassword;
    String StrMobileNo;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_password);
        etnewpassword=findViewById(R.id.etSetUpnewpassword);
        etconfirmpassword=findViewById(R.id.etSetUpconfirmpassword);
        btnSetuppassword=findViewById(R.id.btnSetuppassword1);
        StrMobileNo=getIntent().getStringExtra("mobileno");
        Toast.makeText(SetUpNewPasswordActivity.this,StrMobileNo,Toast.LENGTH_SHORT).show();
        btnSetuppassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etnewpassword.getText().toString().isEmpty()||etconfirmpassword.getText().toString().isEmpty())
                {
                    Toast.makeText(SetUpNewPasswordActivity.this, "Please enter new or confirm password",Toast.LENGTH_SHORT).show();
                } else if (!etnewpassword.getText().toString().equals(etconfirmpassword.getText().toString()))
                {
                    etconfirmpassword.setError("password did not match");
                }
                else
                {
                    progressDialog =new ProgressDialog(SetUpNewPasswordActivity.this);
                    progressDialog.setTitle("Upadating password");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    ForgetPassword();
                }
            }
        });
    }

    private void ForgetPassword() {
        AsyncHttpClient client=new AsyncHttpClient();//this class is used for client server communication
        RequestParams params=new RequestParams();//this class is used to put the data
        params.put("mobileno",StrMobileNo);
        params.put("password",etnewpassword.getText().toString());

        client.post(Urls.UserForgetPassword,params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String status=response.getString("success");
                            if (status.equals("1"))
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "new passsword set up suceessfully", Toast.LENGTH_SHORT).show();
                                Intent intent =new Intent(SetUpNewPasswordActivity.this,LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(SetUpNewPasswordActivity.this, "password not changed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(SetUpNewPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}