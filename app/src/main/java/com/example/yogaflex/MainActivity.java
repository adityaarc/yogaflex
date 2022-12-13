package com.example.yogaflex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity started";
    RelativeLayout layout1,layout2;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            init();
        }catch (Exception e){
            Log.d(TAG, "onCreate: error : "+e);
        }
//        init();
    }

    public void login_fn(View view) {

        TextView emailTv,passwordTv;
        emailTv=findViewById(R.id.email_login_et);
        passwordTv=findViewById(R.id.pass_login_et);
        String email=emailTv.getText().toString();
        String password=passwordTv.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(MainActivity.this,YogaActivity.class));
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: error : "+e);
                    }
                });
    }


    public void goto_register(View view) {
        layout1.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.VISIBLE);
    }

    public void create_acc_btn(View view) {
        Log.d(TAG, "create_acc_btn : ");

        EditText emailET=findViewById(R.id.email_create_et);
        EditText passwordET=findViewById(R.id.pass_create_et);
        EditText nameET=findViewById(R.id.name_create_et);
        EditText ageET=findViewById(R.id.age_create_et);
        String email=emailET.getText().toString();
        String password=passwordET.getText().toString();
        int age=Integer.parseInt(ageET.getText().toString());

        if (18<=age&&age<=65){
            try {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    Log.d(TAG, "createUserWithEmail:success");
                                    String user = mAuth.getCurrentUser().getUid();
                                    mRef.child(user).child("name").setValue(nameET.getText().toString());
                                    mRef.child(user).child("age").setValue(age+"");
                                    startActivity(new Intent(MainActivity.this, YogaActivity.class));

                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                }
                            }
                        });

            } catch (Exception e) {
                Log.d(TAG, "create_acc_btn: error : " + e);
            }
        }else{
            Toast.makeText(this, "the age should be between 18 to 65", Toast.LENGTH_SHORT).show();
        }
    }


    public void goto_login(View view) {
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);
    }


    public void init(){
        layout1=findViewById(R.id.RelativeLayout1);
        layout2=findViewById(R.id.RelativeLayout2);
        mAuth = FirebaseAuth.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference("user");
        if (mAuth.getCurrentUser()!=null)
            startActivity(new Intent(this,YogaActivity.class));
    }
}