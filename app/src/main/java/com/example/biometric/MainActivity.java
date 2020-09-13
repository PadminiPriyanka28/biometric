package com.example.biometric;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btbio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btbio=findViewById(R.id.btbio);
        btbio.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {

        if(isBiometricAvailable()){

            showBiometricPrompt();
        }

    }

    private void showBiometricPrompt() {

        Executor executor= ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt=new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);

                Toast.makeText(MainActivity.this, errString,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Authentication Success",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onAuthenticationFailed(){
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Authentication Failed",Toast.LENGTH_SHORT).show();
            }
        });
        BiometricPrompt.PromptInfo promptInfo=new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Authenticate with biometric")
                .setNegativeButtonText("Cancel")
                .setConfirmationRequired(true)
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private boolean isBiometricAvailable() {
        BiometricManager biometricManager= BiometricManager.from(this);
        int can=biometricManager.canAuthenticate();

        if(can==BiometricManager.BIOMETRIC_SUCCESS){
            Toast.makeText(this,"Biometric is available.",Toast.LENGTH_SHORT).show();
            return true;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE){
            Toast.makeText(this,"Biometric hardware is not available.",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE){
            Toast.makeText(this,"No Biometric hardware.",Toast.LENGTH_SHORT).show();
            return false;
        }

        else if(can==BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED){
            Toast.makeText(this,"Biometric not enrolled.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}