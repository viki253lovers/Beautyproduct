package com.example.beautyproduct.Admin.AdapterClass;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.beautyproduct.POJOGetAllCategoryDetails;
import com.example.beautyproduct.R;

import java.util.List;

public class AdapterGetAllCategoryDetailsRV extends RecyclerView.Adapter<AdapterGetAllCategoryDetailsRV.ViewHolder>  {

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    Activity activity;

    public AdapterGetAllCategoryDetailsRV(List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails, Activity activity) {
        this.pojoGetAllCategoryDetails = pojoGetAllCategoryDetails;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.rv_get_all_category,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetails.get(position);
        viewHolder.tvCategoryName.setText(obj.getCategoryname());

        Glide.with(activity).load("http://192.168.218.191:80/msbteAPI/images/"+obj.getCategoryimage())
                .skipMemoryCache(false)
                .error(R.drawable.no_available_image)
                .into(viewHolder.ivCategoryImage);

    }

    @Override
    public int getItemCount() {
        return pojoGetAllCategoryDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivCategoryImage;
        TextView tvCategoryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvcategoryname);
        }
    }
}
