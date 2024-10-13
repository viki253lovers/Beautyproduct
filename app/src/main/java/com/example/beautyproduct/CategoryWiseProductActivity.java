package com.example.beautyproduct;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.beautyproduct.common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.mime.content.StringBody;

public class CategoryWiseProductActivity extends AppCompatActivity {

    SearchView svCategoryWiseSearch;
    ListView lvCategoryWiselist;
    TextView tvCategoryWiseNOAvailable;

    String strCategoryname;

    List<POJOCategoryWiseProduct> pojoCategoryWiseProduct;

    AdapterCategoryWiseProduct adapterCategoryWiseProduct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ObjectName=methodname(R.id.idname);
        setContentView(R.layout.activity_category_wise_product);
        svCategoryWiseSearch=findViewById(R.id.svCetegoryProductWiseSearchview);
        lvCategoryWiselist=findViewById(R.id.lvCategoryProductWisedList);
        tvCategoryWiseNOAvailable=findViewById(R.id.tvCategoryProductWisedNoAvailableImage);

        pojoCategoryWiseProduct=new ArrayList<>();

        strCategoryname=getIntent().getStringExtra("categoryname");

        GetAllCategoryProductWise();

        svCategoryWiseSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                svCategoryWiseSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                svCategoryWiseSearch(query);
                return false;
            }
        });
    }

    private void svCategoryWiseSearch(String query) {

        List<POJOCategoryWiseProduct> templist=new ArrayList<>();
        templist.clear();

        for (POJOCategoryWiseProduct obj:pojoCategoryWiseProduct)
        {
            if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase())||
                obj.getProductname().toUpperCase().contains(query.toUpperCase())||
                obj.getShopname().toUpperCase().contains(query.toUpperCase())||
                obj.getProductprice().toUpperCase().contains(query.toUpperCase())||
                obj.getProductcategory().toUpperCase().contains(query.toUpperCase())||
                obj.getProductrating().toUpperCase().contains(query.toUpperCase()))
            {
                templist.add(obj);
            }

            adapterCategoryWiseProduct = new AdapterCategoryWiseProduct(templist,this);
            
            lvCategoryWiselist.setAdapter(adapterCategoryWiseProduct);

        }

    }

    private void GetAllCategoryProductWise() {
        AsyncHttpClient client=new AsyncHttpClient();//this class is used for client server communication
        RequestParams params=new RequestParams();//this class is used to put the data

        params.put("categoryname",strCategoryname);

        client.post(Urls.getAllCategoryWisedDetails,
                params, new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray=response.getJSONArray("getAllCategoryWiseProduct");
                            if (jsonArray.isNull(0))
                            {
                                lvCategoryWiselist.setVisibility(View.GONE);
                                tvCategoryWiseNOAvailable.setVisibility(View.VISIBLE);
                            }

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String strid=jsonObject.getString("id");
                                String strcategoryname=jsonObject.getString("categoryname");
                                String strshopname=jsonObject.getString("shopname");
                                String strproductcategory=jsonObject.getString("productcategory");
                                String strproductimage=jsonObject.getString("productimage");
                                String strproductname=jsonObject.getString("productname");
                                String strproductprice=jsonObject.getString("productprice");
                                String strproductrating=jsonObject.getString("productrating");
                                String strproductoffer=jsonObject.getString("productoffer");
                                String strproductdiscription=jsonObject.getString("productdiscription");

                                pojoCategoryWiseProduct.add(new POJOCategoryWiseProduct(strid,strcategoryname,strshopname,
                                        strproductcategory,strproductimage
                                ,strproductname,strproductprice,strproductrating,strproductoffer,strproductdiscription));
                            }

                             adapterCategoryWiseProduct =new AdapterCategoryWiseProduct(pojoCategoryWiseProduct,CategoryWiseProductActivity.this);

                            lvCategoryWiselist.setAdapter(adapterCategoryWiseProduct);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(CategoryWiseProductActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}