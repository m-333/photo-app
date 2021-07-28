package com.example.insta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {
    private EditText nfullname,nemail,npassword;
    private Button nlogin;
    private   TextView nlogincreatetext;
    private ProgressDialog loginProgress;
    private FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nemail = (EditText) findViewById(R.id.emaill);
        npassword = (EditText) findViewById(R.id.npassaword);
        nlogin = (Button) findViewById(R.id.login);
        nlogincreatetext = (TextView) findViewById(R.id.logincreatetext);
        loginProgress=new ProgressDialog(this);
        fAuth=FirebaseAuth.getInstance();
        nlogincreatetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(login.this, register.class);
                startActivity(loginIntent);
            }
        });
        nlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=nemail.getText().toString();
                String password=npassword.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {

                    loginProgress.setTitle("Oturum Açılıyor");
                    loginProgress.setMessage("Hesabınıza giriş yapılıyor, Lütfen bekleyiniz...");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    login(email,password);
                }

            }
            private void login(String email, String password) {

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(fAuth.getCurrentUser().getUid());
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    loginProgress.dismiss();
                                    Toast.makeText(getApplicationContext(),"Giriş Başarılı...",Toast.LENGTH_SHORT).show();
                                    Intent mainIntent = new Intent(login.this, MainActivity.class);
                                    startActivity(mainIntent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                        }
                        else{
                            loginProgress.dismiss();
                            Toast.makeText(getApplicationContext(),"Giriş Yapılamadı."+task.getException().getMessage() ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}