package com.example.beautyproduct;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.beautyproduct.common.Urls;

import java.util.List;

public class AdapterGetAllCategoryDetails extends BaseAdapter {

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    Activity activity;//java class no front end file as a call by activity object

    public AdapterGetAllCategoryDetails(List<POJOGetAllCategoryDetails> list, Activity activity) {
        this.pojoGetAllCategoryDetails = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoGetAllCategoryDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return pojoGetAllCategoryDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lv_get_all_category, null);
            holder.cvcategorycard = view.findViewById(R.id.cvCategory);
            holder.ivcategoryimage = view.findViewById(R.id.ivCategoryImage);
            holder.tvCategoryname = view.findViewById(R.id.tvcategoryname);

            view.setTag(holder);
        } else
        {
            holder = (ViewHolder) view.getTag();
        }


        final POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetails.get(position);
        holder.tvCategoryname.setText(obj.getCategoryname());

        Glide.with(activity)
                .load("http://192.168.218.191:80/msbteAPI/images/"+obj.getCategoryimage())
                .skipMemoryCache(true)
                .error(R.drawable.no_available_image)
                .into(holder.ivcategoryimage);

        holder.cvcategorycard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, CategoryWiseProductActivity.class);
                intent.putExtra("categoryname",obj.getCategoryname());
                activity.startActivity(intent);
            }
        });
        return view;

    }


    class ViewHolder
    {
        CardView cvcategorycard;
        ImageView ivcategoryimage;
        TextView tvCategoryname;

    }
    //BaseAdapter=multiple views load on the screen
    //AdapterGetAllCategoryDetails multiple data collect load on listtview
}
