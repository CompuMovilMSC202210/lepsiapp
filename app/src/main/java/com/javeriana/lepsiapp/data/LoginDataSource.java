package com.javeriana.lepsiapp.data;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.javeriana.lepsiapp.data.model.LoggedInUser;
import com.javeriana.lepsiapp.ui.PreferManag;
import com.javeriana.lepsiapp.ui.login.LoginActivity;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    PreferManag preferenceManager;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreference;
    public static String TAG = "LepsiApp";

    public Result<LoggedInUser> login(String username, String password) {
        firebaseAuth = FirebaseAuth.getInstance();
        // firebase intent

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        try {
            // TODO: handle loggedInUser authentication


            LoggedInUser user = null;
            firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        LoggedInUser user =new LoggedInUser(  java.util.UUID.randomUUID().toString(),
                                        task.getResult().getUser().getDisplayName());

                       // updateUI(firebaseAuth.getCurrentUser());
                    }else{

                        Log.e(TAG,"Autentificación fallida: "+task.getException().toString());
                        //Toast.makeText(LoginActivity.this, task.getException().toString(),Toast.LENGTH_LONG).show();
                    }
                }
            });
            return new Result.Success<>(user);
           // return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
