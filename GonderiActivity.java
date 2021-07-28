package com.example.insta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.insta.Adapter.ViewpagerAdapter;
import com.example.insta.interfacee.EditImageFragmentListener;
import com.example.insta.interfacee.FiltersListFragmentListener;
import com.example.insta.utils.BitmapUtils;
import com.example.insta.utils.EditImageFragment;
import com.example.insta.utils.FiltersListFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.IOException;
import java.util.List;

public class GonderiActivity extends AppCompatActivity implements FiltersListFragmentListener, EditImageFragmentListener {

    public static final String pictureName="flash.jpg";
    public static final int PERMİSSON_PİCK_İMAGE=1000;

    ImageView img_preview;
    TabLayout tabLayout;
    ViewPager viewPager;
    CoordinatorLayout coordinatorLayout;
    Bitmap orjinalBitmap,filterBitmap, finalBitmap;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;

    int brightnessFinal=0;
    float saturationFinal=1.0f;
    float constrantFinal=1.0f;

    static {
        System.loadLibrary("NativeImageProcessor");
        

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gonderi);

        Toolbar toolbar=findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("İnstagram title");

        img_preview=(ImageView) findViewById(R.id.image_preview);
        tabLayout=(TabLayout) findViewById(R.id.tabs);
        viewPager=(ViewPager) findViewById(R.id.viewpager);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinator);

        loadImage();
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void loadImage() {
        orjinalBitmap= BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filterBitmap=orjinalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap=orjinalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        img_preview.setImageBitmap(orjinalBitmap);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter= new ViewpagerAdapter(getSupportFragmentManager());
        filtersListFragment= new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment=new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addfragment(filtersListFragment,"FİLTERS");
        adapter.addfragment(editImageFragment,"EDIT");
        viewPager.setAdapter(adapter);

    }


    @Override
    public void onBrightnessChanged(int brightness) {
    brightnessFinal=brightness;
    Filter myFilter= new Filter();
    myFilter.addSubFilter((new BrightnessSubFilter(brightness)));
    img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onSaturationChanged(float saturation) {
        saturationFinal=saturation;
        Filter myFilter= new Filter();
        myFilter.addSubFilter((new SaturationSubfilter(saturation)));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstrantChanged(float constrant) {
        constrantFinal=constrant;
        Filter myFilter= new Filter();
        myFilter.addSubFilter((new ContrastSubFilter(constrant)));
        img_preview.setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        Bitmap bitmap= filterBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter= new Filter();
        myFilter.addSubFilter((new BrightnessSubFilter(brightnessFinal)));
        myFilter.addSubFilter((new SaturationSubfilter(saturationFinal)));
        myFilter.addSubFilter((new ContrastSubFilter(constrantFinal)));

        finalBitmap= myFilter.processFilter(bitmap);

    }

    @Override
    public void onFilterSelected(Filter filter) {
     resetControl();
     filterBitmap=orjinalBitmap.copy(Bitmap.Config.ARGB_8888,true);
     img_preview.setImageBitmap(filter.processFilter(filterBitmap));
     finalBitmap= filterBitmap.copy(Bitmap.Config.ARGB_8888,true);

    }

    private void resetControl() {
        if(editImageFragment !=null)
            editImageFragment.resetControls();
        brightnessFinal=0;
        saturationFinal=1.0f;
        constrantFinal=1.0f;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gonderi_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_open){
            openImageFromGallery();
            return true;
        }
        if(id==R.id.action_save){

            SaveImageToGallery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SaveImageToGallery() {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener((new MultiplePermissionsListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport repost) {
            if(repost.areAllPermissionsGranted()){
                try {
                    final String path= BitmapUtils.insertImage(getContentResolver(),
                            finalBitmap,
                            System.currentTimeMillis()+"_profile.jpg"
                    ,null);
                    if(!TextUtils.isEmpty(path))
                    {
                        Snackbar snackbar= Snackbar.make(coordinatorLayout,
                                "image saved to gallery",
                                Snackbar.LENGTH_LONG)
                                .setAction("OPEN", v -> openImage(path));
                    }
                    else{

                        Snackbar snackbar= Snackbar.make(coordinatorLayout,
                                "Unable to save image",
                                Snackbar.LENGTH_LONG);

                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(GonderiActivity.this, "Permission Danied ", Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }));
    }

    private void openImage(String path) {
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    private void openImageFromGallery()
    {
        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener((new MultiplePermissionsListener(){


            @Override
            public void onPermissionsChecked(MultiplePermissionsReport repost) {
                if (repost.areAllPermissionsGranted()) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PERMİSSON_PİCK_İMAGE);
                }
                else {
                    Toast.makeText(GonderiActivity.this,"permission denied", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.cancelPermissionRequest();

            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PERMİSSON_PİCK_İMAGE) {
            Bitmap bitmap = BitmapUtils.getBitmapFromGalery(this, data.getData(), 800, 800);
            //clear bitmap memory
            orjinalBitmap.recycle();
            finalBitmap.recycle();
            ;
            filterBitmap.recycle();

            orjinalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            finalBitmap = orjinalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            filterBitmap = orjinalBitmap.copy(Bitmap.Config.ARGB_8888, true);
            img_preview.setImageBitmap(orjinalBitmap);
            bitmap.recycle();
            filtersListFragment.displayThumbnail(orjinalBitmap);
        }
    }
}
