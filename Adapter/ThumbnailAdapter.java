package com.example.insta.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.R;
import com.example.insta.interfacee.FiltersListFragmentListener;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.MyViewHolder> {
private List<ThumbnailItem> thumbnailItems;
private FiltersListFragmentListener listener;
private Context context;
private  int selectedIndex=0 ;

    public ThumbnailAdapter(List<ThumbnailItem> thumbnailItems, FiltersListFragmentListener listener, Context context) {
        this.thumbnailItems = thumbnailItems;
        this.listener= listener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     View itemView = LayoutInflater.from(context).inflate(R.layout.thumbnail_item,parent,false);
     return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    ThumbnailItem thumbnailItem=thumbnailItems.get(position);
    holder.thumbnail.setImageBitmap(thumbnailItem.image);
    holder.thumbnail.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        listener.onFilterSelected(thumbnailItem.filter);
        selectedIndex=position;
        notifyDataSetChanged();
    }
});
    holder.filer_name.setText(thumbnailItem.filterName);
    if(selectedIndex==position)
    holder.filer_name.setTextColor(ContextCompat.getColor(context, R.color.selected_filter));
    else
    holder.filer_name.setTextColor(ContextCompat.getColor(context, R.color.normal_filter));
    }

    @Override
    public int getItemCount() {

    return thumbnailItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView filer_name;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail=(ImageView)itemView.findViewById(R.id.thumbnail);
            filer_name=(TextView)itemView.findViewById(R.id.filter_name);
        }
    }
}
