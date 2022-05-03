package com.javeriana.lepsiapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.javeriana.lepsiapp.HelpInformation;
import com.javeriana.lepsiapp.MainActivity;
import com.javeriana.lepsiapp.PasswordRecovery;
import com.javeriana.lepsiapp.R;
import com.javeriana.lepsiapp.Register;
import com.javeriana.lepsiapp.ui.PreferManag;
import com.javeriana.lepsiapp.ui.login.LoginViewModel;
import com.javeriana.lepsiapp.ui.login.LoginViewModelFactory;
import com.javeriana.lepsiapp.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;


    //FirebaseAuth
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    PreferManag preferenceManager;
    FirebaseUser firebaseUser;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreference;

    TextView forgotpassword;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    // initiate view's

                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
               loginViewModel.login(usernameEditText.getText().toString(),  passwordEditText.getText().toString());
                homeViewActivity(v);
            }
        });

        TextView recoveryLink = (TextView) findViewById(R.id.recoverPwdLink);
        // perform click event on button's
        recoveryLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoveryPwdViewActivity(view);
            }
        });

        TextView registerLink = (TextView) findViewById(R.id.registerLink);

        // perform click event on button's
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerViewActivity(view);
            }
        });

        TextView helpLink = (TextView) findViewById(R.id.helpLink);
        // perform click event on button's
        helpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpViewActivity(view);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void homeViewActivity(View v){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void registerViewActivity(View v){
        startActivity(new Intent(this, Register.class));
    }
    public void helpViewActivity(View v){
        startActivity(new Intent(this, HelpInformation.class));
    }
    public void recoveryPwdViewActivity(View v){
        startActivity(new Intent(this, PasswordRecovery.class));
    }

}