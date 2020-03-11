package com.hfad.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class otpver extends AppCompatActivity {
    FirebaseAuth fauth;
    EditText phonenumber,codeenter;
    Button nextbtn;
    CountryCodePicker ccd;
    String phoneNum;
    private static final String TAG = "TAG";
    String verification_id;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean flag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpver);
        fauth=FirebaseAuth.getInstance();
        phonenumber=(EditText)findViewById(R.id.phone);
        codeenter=(EditText)findViewById(R.id.codeEnter);
        ccd=findViewById(R.id.ccp);
        nextbtn=(Button)findViewById(R.id.nextBtn);
        if(fauth.getCurrentUser()!=null){
            Intent intent=new Intent(otpver.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!flag){
                    if(!phonenumber.getText().toString().isEmpty() && phonenumber.getText().toString().length()==10){
                        phoneNum="+"+ccd.getSelectedCountryCode()+phonenumber.getText().toString();
                        Log.d(TAG, "onClick: phone number :"+phoneNum);
                        requestotp(phoneNum);

                    }
                    else{
                        phonenumber.setError("Enter Valid Phone Number");
                    }
                }
                else
                {
                    String userOTP=codeenter.getText().toString();
                    if(!userOTP.isEmpty()&& userOTP.length()==6){
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verification_id,userOTP);
                        verifyAuth(credential);

                    }
                    else {
                        codeenter.setError("Valid OTP is required");
                    }
                }
            }
        });

    }

    private void verifyAuth(PhoneAuthCredential credential) {
        fauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(otpver.this,"Successful",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(otpver.this,MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(otpver.this,"Not Successful",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void requestotp(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60l, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_id=s;
                token=forceResendingToken;
                codeenter.setVisibility(View.VISIBLE);
                flag=true;
                nextbtn.setText("Verify");
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(otpver.this,"Can't create account "+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

}
