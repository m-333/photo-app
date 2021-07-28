package com.example.insta.utils;

import android.app.backup.FileBackupHelper;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Space;

import com.example.insta.Adapter.ThumbnailAdapter;
import com.example.insta.GonderiActivity;
import com.example.insta.R;
import com.example.insta.interfacee.FiltersListFragmentListener;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;


public class FiltersListFragment extends Fragment implements FiltersListFragmentListener{
    RecyclerView recyclerView;
    ThumbnailAdapter adapter;
    List<ThumbnailItem> thumbnailItems;

    FiltersListFragmentListener listener;

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView= inflater.inflate(R.layout.fragment_filters_list, container, false);
        thumbnailItems= new ArrayList<>();
        adapter= new ThumbnailAdapter(thumbnailItems,this, getActivity());
        recyclerView=(RecyclerView) itemView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space =(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new spacesItemDecoration(space));
        recyclerView.setAdapter(adapter);

        displayThumbnail (null);

        return itemView;
    }

    public void displayThumbnail(Bitmap bitmap) {
        Runnable r= new Runnable() {
            @Override
            public void run() {

                Bitmap thumbImg;
                if(bitmap==null)
                    thumbImg=BitmapUtils.getBitmapFromAssets(getActivity(), GonderiActivity.pictureName,100,100);
                    else
                       thumbImg=Bitmap.createScaledBitmap(bitmap,100,100,false);

                    if(thumbImg==null)
                        return;
                ThumbnailsManager.clearThumbs();
                thumbnailItems.clear();

                ThumbnailItem thumbnailItem=new ThumbnailItem();
                thumbnailItem.image=thumbImg;
                thumbnailItem.filterName="Normal";
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters= FilterPack.getFilterPack(getActivity());
                for (Filter filter:filters){
                    ThumbnailItem tI=new ThumbnailItem();
                    tI.image=thumbImg;
                    tI.filter=filter;
                    tI.filterName=filter.getName();
                    ThumbnailsManager.addThumb(tI);

                }
                thumbnailItems.addAll(ThumbnailsManager.processThumbs(getActivity()));
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        new Thread(r).start();

    }

    @Override
    public void onFilterSelected(Filter filter) {
        if(listener!=null){
        listener.onFilterSelected(filter);
 }
    }
}