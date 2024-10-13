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

public class UpdateProfileActivity extends AppCompatActivity {

    EditText etUpadatename,etUpdatemobileno,etUpdateemailid,etUpdateusername;
    AppCompatButton btnUpdateButton;

    String strname,strmobileno,stremailid,strusername;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        etUpadatename = findViewById(R.id.etUpadateName);
        etUpdatemobileno = findViewById(R.id.etUpadateMobileNo);
        etUpdateemailid = findViewById(R.id.etUpdateEmailid);
        etUpdateusername = findViewById(R.id.etUpdateUsername);
        btnUpdateButton = findViewById(R.id.btnUpdatebutton);

        strname =getIntent().getStringExtra("name");
        strmobileno = getIntent().getStringExtra("mobileno");
        stremailid = getIntent().getStringExtra("emailid");
        strusername = getIntent().getStringExtra("username");

        etUpadatename.setText(strname);
        etUpdatemobileno.setText(strmobileno);
        etUpdateemailid.setText(stremailid);
        etUpdateusername.setText(strusername);

        btnUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               progressDialog =new ProgressDialog(UpdateProfileActivity.this);
               progressDialog.setTitle("Updating Profile");
               progressDialog.setMessage("Please wait...");
               progressDialog.setCanceledOnTouchOutside(true);
               progressDialog.show();
                UpdateProfile();
            }
        });

    }

    private void UpdateProfile() {

        AsyncHttpClient client=new AsyncHttpClient();//This class is used for client server communication over the network
        RequestParams params =new RequestParams();//put the data

        params.put("name",etUpadatename.getText().toString());
        params.put("mobileno",etUpdatemobileno.getText().toString());
        params.put("emailid",etUpdateemailid.getText().toString());
        params.put("username",etUpdateusername.getText().toString());

        client.post(Urls.updateProfileWebService,params,new JsonHttpResponseHandler()

                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            String status=response.getString("success");

                            if(status.equals("1"))
                            {
                                Toast.makeText(UpdateProfileActivity.this, "Upadating Successfully done", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(UpdateProfileActivity.this, MyProfileActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(UpdateProfileActivity.this, "Update not successfully done ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}