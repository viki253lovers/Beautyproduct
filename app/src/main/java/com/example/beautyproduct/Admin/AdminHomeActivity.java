package com.example.beautyproduct.Admin;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.beautyproduct.Admin.AdapterClass.AdapterGetAllCategoryDetailsRV;
import com.example.beautyproduct.POJOGetAllCategoryDetails;
import com.example.beautyproduct.R;
import com.example.beautyproduct.common.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {

    RecyclerView rvGetAllCategory;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    AdapterGetAllCategoryDetailsRV adapterGetAllCategoryDetailsRV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        rvGetAllCategory = findViewById(R.id.rvAdminHomeActivityShowMultipleData);

        rvGetAllCategory.setLayoutManager(new GridLayoutManager(AdminHomeActivity.this,2,
                GridLayoutManager.HORIZONTAL, false));  //this layout manager is used to show all data in tabular format
        pojoGetAllCategoryDetails = new ArrayList<>();

        adapterGetAllCategoryDetailsRV = new AdapterGetAllCategoryDetailsRV(pojoGetAllCategoryDetails,this);

        rvGetAllCategory.setAdapter(adapterGetAllCategoryDetailsRV);

        getAllCategoryRV();

    }

    private void getAllCategoryRV() {

        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);  //client server communication

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Urls.getAllCategoryDatailes,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("getAllcategory");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String strId = jsonObject1.getString("id");
                                String strCategoryImage = jsonObject1.getString("categoryimage");
                                String strCategoryName = jsonObject1.getString("categoryname");

                                pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strId,strCategoryImage,strCategoryName));
                            }

                            adapterGetAllCategoryDetailsRV.notifyDataSetChanged();

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminHomeActivity.this, "Server error", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
        
    }
}