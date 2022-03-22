package com.xcityprime.whatsapp.view.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.xcityprime.whatsapp.R;
import com.xcityprime.whatsapp.databinding.ActivityPhoneLoginBinding;
import com.xcityprime.whatsapp.model.user.Users;
import com.xcityprime.whatsapp.view.MainActivity;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ActivityPhoneLoginBinding binding;
    private FirebaseAuth mAuth;
    private String TAG = "PhoneLoginActivity";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    private ProgressDialog progressDialog;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore fireStore;
    String mVerificationId;
    String[] country = {"Nigeria","India", "USA", "China", "Japan", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_phone_login);

        Spinner spin = findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);


        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on spinner
        spin.setAdapter(aa);

        mAuth = FirebaseAuth.getInstance();

        fireStore = FirebaseFirestore.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            startActivity(new Intent(this, MainActivity.class));
        }

        progressDialog = new ProgressDialog(this);

        binding.nxtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.nxtBtn.getText().equals("Next")) {
                    progressDialog.setMessage("Please Wait..");
                    progressDialog.show();

                    String phone = "+" +binding.edCodeCountry.getText().toString() +binding.edPhone.getText().toString();
                    startPhoneNumberVerification(phone);
                }else{
                    progressDialog.setMessage("Verifying..");
                    progressDialog.show();
                    verifyPhoneNumberWithCode(mVerificationId, binding.edCode.getText().toString() );

                }
            }
        });

        mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerification: Complete");
                signInWithPhoneAuthCredential(phoneAuthCredential);
                progressDialog.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:"+ verificationId);

                mVerificationId = verificationId;
                mResendToken = token;

                binding.nxtBtn.setText("Confirm");
                progressDialog.dismiss();

            }
        };
    }
    private void startPhoneNumberVerification(String phoneNumber){
        progressDialog.setMessage("Sending OTP to "+ phoneNumber);
        progressDialog.show();
        // [Start phone Auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber,60, TimeUnit.SECONDS,this, mCallBack);
        //End start Phone Auth
       // mVerificationInProgress = true;
    }
    private void verifyPhoneNumberWithCode(String verificationId, String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Log.d(TAG, "signInWithCredential: success" );
                    FirebaseUser user = task.getResult().getUser();

                    if (user != null) {
                        String userID = user.getUid();
                        Users users = new Users(userID,
                                "",
                                user.getPhoneNumber(),
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                "");

                        fireStore.collection("Users").document("UserInfo").collection(userID).add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                startActivity(new Intent(PhoneLoginActivity.this, SetUserInfoActivity.class));
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }

                }else{
                    progressDialog.dismiss();
                    Log.w(TAG, "signInWithCredential: failure", task.getException());
                    Toast.makeText(PhoneLoginActivity.this, "OTP Incorrect!", Toast.LENGTH_LONG).show();
                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        Log.d(TAG, "onComplete: Error Code");
                    }
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}