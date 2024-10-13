package com.example.beautyproduct;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterCategoryWiseProduct extends BaseAdapter {

    List<POJOCategoryWiseProduct> list;
    Activity activity;

    public AdapterCategoryWiseProduct(List<POJOCategoryWiseProduct> pojoCategoryWiseProducts, Activity activity) {
        this.list = pojoCategoryWiseProducts;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {

        final ViewHolder holder;
        final LayoutInflater inflater=(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view==null)
        {
            holder = new ViewHolder();
          view = inflater.inflate(R.layout.lv_categorywise_product,null);

          holder.ivCategoryWiseimage=view.findViewById(R.id.ivCategoryWiseProductimage);
          holder.tvShopname=view.findViewById(R.id.tvCategoryWiseShopName);
          holder.tvProductrating=view.findViewById(R.id.tvCategoryWiserating);
          holder.tvProductname=view.findViewById(R.id.tvCategoryWiseProductName);
          holder.tvProductcategory=view.findViewById(R.id.tvCategoryWiseProductCategory);
          holder.tvProductprice=view.findViewById(R.id.tvCategoryWiseProductPrice);
          holder.tvProductOffer=view.findViewById(R.id.tvCategoryWiseProductOffer);
          holder.tvProductDiscription=view.findViewById(R.id.tvCategoryWiseProductDiscription);

          view.setTag(holder);
        }
        else
        {
            holder=(ViewHolder) view.getTag();
        }
        final POJOCategoryWiseProduct obj=list.get(position);
        holder.tvShopname.setText(obj.getShopname());
        holder.tvProductrating.setText(obj.getProductrating());
        holder.tvProductname.setText(obj.getProductname());
        holder.tvProductcategory.setText(obj.getProductcategory());
        holder.tvProductprice.setText(obj.getProductprice());
        holder.tvProductOffer.setText(obj.getProductoffer());
        holder.tvProductDiscription.setText(obj.getProductdiscription());

        Glide.with(activity)
                .load("http://192.168.218.191:80/msbteAPI/images/"+obj.getProductimage())
                .skipMemoryCache(true)
                .error(R.drawable.no_available_image)
                .into(holder.ivCategoryWiseimage);
        return view;
    }
    class ViewHolder
    {
        ImageView ivCategoryWiseimage;
        TextView tvShopname,tvProductrating,tvProductname,tvProductcategory,tvProductprice,
                tvProductOffer,tvProductDiscription;
    }
}
