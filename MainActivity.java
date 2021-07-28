package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.insta.fragment.FavoriteFragment;
import com.example.insta.fragment.HomeFragment;
import com.example.insta.fragment.PersonFragment;
import com.example.insta.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    Fragment SelectedFragment =null;
    BottomNavigationView bottomNavigationView;

    private FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView=findViewById(R.id.botom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        mAuth = FirebaseAuth.getInstance();

        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,new HomeFragment()).commit();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {

                    startActivity(new Intent(MainActivity.this, login.class));
                    finish();
                }
            }
        };


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId())
                    {
                        case R.id.nav_home:
                            SelectedFragment= new HomeFragment();
                            break;
                        case R.id.nav_search:
                            SelectedFragment=new SearchFragment();
                            break;
                        case R.id.nav_add:
                            SelectedFragment= null;
                            startActivity(new Intent(MainActivity.this,GonderiActivity.class));
                            break;
                        case R.id.nav_favorite:
                            SelectedFragment= new FavoriteFragment();
                            break;
                        case R.id.nav_person:
                            SharedPreferences.Editor editor=getSharedPreferences("PRAFS",MODE_PRIVATE).edit();
                            editor.putString("profileid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            SelectedFragment= new PersonFragment();
                            break;
                    }
                    if (SelectedFragment!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayici,SelectedFragment).commit();
                    }


                    return true;
                }

            };


     @Override
     public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
     }

     @Override
     public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
     }

     }