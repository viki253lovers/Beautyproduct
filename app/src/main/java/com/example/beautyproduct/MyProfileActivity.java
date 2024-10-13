package com.example.beautyproduct;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beautyproduct.common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    TextView tvname,tvmobileno,tvemail,tvusername;
    AppCompatButton btnupdateProfile,btnSignOut,btnchooseImage;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;

    SharedPreferences preferences;

    String Strusername;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        preferences=PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);
        Strusername=preferences.getString("username","");

       tvname=findViewById(R.id.tvMyProfileName);
       tvmobileno=findViewById(R.id.tvMyProfilemobileNo);
       tvemail=findViewById(R.id.tvMyProfileEmail);
       tvusername=findViewById(R.id.tvMyProfileUsername);
       btnupdateProfile = findViewById(R.id.btnUpdateProfile);
       btnSignOut=findViewById(R.id.btnSignout);
       btnchooseImage=findViewById(R.id.btnMyProfileChooseImage);

       googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
       googleSignInClient= GoogleSignIn.getClient(MyProfileActivity.this,googleSignInOptions);

        GoogleSignInAccount googleSignInAccount=GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount!=null)
        {
            String name=googleSignInAccount.getDisplayName();
            String email=googleSignInAccount.getEmail();
          //  tvname.setText(name);
           // tvemail.setText(email);
            btnSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent=new Intent(MyProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog=new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
        getMyDetails();
    }

    private void getMyDetails() {

        AsyncHttpClient client=new AsyncHttpClient();  //AsyncHttpClient class is used to communication between server and client
        RequestParams params=new RequestParams();     //put data

        params.put("username",Strusername);

        client.post(Urls.getMyDetailsWebService,params,new JsonHttpResponseHandler()

                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();
                        try {
                            JSONArray jsonArray=response.getJSONArray("getMyDetails");
                            for(int i=0;i<jsonArray.length();i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String strid = jsonObject.getString("id");
                                String strname = jsonObject.getString("name");
                                String strmobileno = jsonObject.getString("mobileno");
                                String stremailid = jsonObject.getString("emailid");
                                String strusername = jsonObject.getString("username");

                                tvname.setText(strname);
                                tvmobileno.setText(strmobileno);
                                tvemail.setText(stremailid);
                                tvusername.setText(Strusername);

                                btnupdateProfile.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent i = new Intent(MyProfileActivity.this, UpdateProfileActivity.class);
                                        i.putExtra("name",strname);
                                        i.putExtra("mobileno",strmobileno);
                                        i.putExtra("emailid",stremailid);
                                        i.putExtra("username",Strusername);
                                        startActivity(i);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(MyProfileActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
