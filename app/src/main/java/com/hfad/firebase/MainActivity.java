package com.hfad.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText fn,ag,em,pass;
    Button bt;
    ProgressBar pb;
    FirebaseAuth fauth;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fn=(EditText)findViewById(R.id.et);
        ag=(EditText)findViewById(R.id.et2);
        em=(EditText)findViewById(R.id.et3);
        pass=(EditText)findViewById(R.id.et4);
        bt=(Button) findViewById(R.id.bt1);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        fauth=FirebaseAuth.getInstance();
        t=(TextView)findViewById(R.id.textView);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (MainActivity.this,Login.class);
                startActivity(intent);
            }
        });


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=em.getText().toString().trim();
                String password=pass.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    em.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    pass.setError("Password is Empty");
                    return;
                }
                if(password.length()<6){
                    pass.setError("Password must be greater than or equal to 6 Characters");
                    return;
                }
                pb.setVisibility(View.VISIBLE);
                fauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent (MainActivity.this,nav_drawer.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Error !"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });
    }


}
