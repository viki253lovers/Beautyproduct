package com.example.beautyproduct;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import cz.msebera.android.httpclient.Header;


public class CategoryFragment extends Fragment {

//classname objectname
    androidx.appcompat.widget.SearchView SvCategoryFragment;
    ListView lvShowMultipledata;
    TextView tvNoAvailableCategory;

   List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
   AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        //Objectname =view.findViewById(R.id.Idname);
        pojoGetAllCategoryDetails = new ArrayList<>();
        SvCategoryFragment = view.findViewById(R.id.SvCategoryFragmentSearchByCategory);
        lvShowMultipledata = view.findViewById(R.id.lvCategoryFragmentShowMultipleData);
        tvNoAvailableCategory = view.findViewById(R.id.tvCategoryfragmentNoCategoryAvailable);

        //query=type by the user
       SvCategoryFragment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String query) {
               SvCategoryFragment(query);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String query) {
               SvCategoryFragment(query);
               return false;
           }
       });
        getAllCategorydetailed();
        return view;
    }



    private void SvCategoryFragment(String query) {

        //list class is used for store same type of multiple data
       List<POJOGetAllCategoryDetails> tempCategory = new ArrayList<>();
       tempCategory.clear();          //clear method is used to remove all previous data
        for (POJOGetAllCategoryDetails obj:pojoGetAllCategoryDetails) //for each used to store each right side data to the left side
        {
            if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase()))
            {
                tempCategory.add(obj);
            }
            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(tempCategory,getActivity());

            lvShowMultipledata.setAdapter(adapterGetAllCategoryDetails);
        }
    }

    private void getAllCategorydetailed() {
        //Classname Objectname=new Constructer
        AsyncHttpClient client=new AsyncHttpClient();//this class is used to communicate the server and client over network
        RequestParams params=new RequestParams();//request params class is used to put the data

        client.post("http://192.168.218.191:80/msbteAPI/getAllCategoryDetails.php",
                params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray jsonArray=response.getJSONArray("getAllcategory");
                            if(jsonArray.isNull(0)) {
                                tvNoAvailableCategory.setVisibility(View.VISIBLE);
                            }

                                for (int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                                    String strid=jsonObject.getString("id");
                                    String strcategoryImage=jsonObject.getString("categoryimage");
                                    String strcategoryName=jsonObject.getString("categoryname");

                                    pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strid,strcategoryImage,
                                            strcategoryName));

                                    adapterGetAllCategoryDetails =new AdapterGetAllCategoryDetails(pojoGetAllCategoryDetails,getActivity());

                                    lvShowMultipledata.setAdapter(adapterGetAllCategoryDetails);
                                }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}