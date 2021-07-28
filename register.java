package com.example.insta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class register extends AppCompatActivity {

    private EditText nemail, nfullName, npassword;
    private Button nregister;
    private TextView registertologin;
    private ProgressDialog registerProgress;
    private FirebaseAuth fAuth;
    private DatabaseReference nDatabase;
    private FirebaseStorage nfirebasetore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nemail = (EditText) findViewById(R.id.email);
        nfullName = (EditText) findViewById(R.id.fullName);
        npassword = (EditText) findViewById(R.id.npassaword);
        nregister = (Button) findViewById(R.id.register);
        registertologin = (TextView) findViewById(R.id.createText);
        registerProgress = new ProgressDialog(this);
        nfirebasetore=FirebaseStorage.getInstance();


        fAuth = FirebaseAuth.getInstance();


        registertologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginintent = new Intent(register.this, login.class);
                startActivity(loginintent);
            }

        });
        nregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nfullName.getText().toString();
                String password = npassword.getText().toString();
                String email = nemail.getText().toString();
                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(password) || !TextUtils.isEmpty(email)) {
                    registerProgress.setTitle("kaydediliyor");
                    registerProgress.setMessage("hesabınızı oluşturuyoruz, lütfen bekleyiniz");
                    registerProgress.setCanceledOnTouchOutside(false);
                    register_user(name, password, email);

                }
            }

            private void register_user(String name, String password, String email) {

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {


                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser=fAuth.getCurrentUser();
                            String user_id = firebaseUser.getUid();
                            nDatabase= FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
                            HashMap<String, Object> userMap = new HashMap<>();

                            userMap.put("id", user_id);
                            userMap.put("name", name);
                            userMap.put("password",password);
                            userMap.put("email",email);
                            userMap.put("image","https://firebasestorage.googleapis.com/v0/b/insta-9c7fe.appspot.com/o/BeautyPlus_20190216133057424_save.jpg?alt=media&token=da72969a-a497-41bb-9b84-3f6b071d42b9");


                            nDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        registerProgress.dismiss();
                                        Intent mainIntent = new Intent(register.this, MainActivity.class);
                                        mainIntent.addFlags(mainIntent.FLAG_ACTIVITY_CLEAR_TASK| mainIntent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(mainIntent);
                                    }
                                }
                            });

                        } else {
                            registerProgress.dismiss();
                            Toast.makeText(getApplicationContext(), "Hata" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}